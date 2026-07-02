package com.yasmin.receitix.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Autowired
    private UserAuthenticationFilter userAuthenticationFilter;

    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED = {
            "/",
            "/api/usuario/login", // Url que usaremos para fazer login
            "/api/usuario/criar", // Url que usaremos para criar um usuário

            // 🔓 Fluxo de "Esqueci minha senha" (usuário ainda não está logado)
            "/api/usuario/recuperarSenha/verificarEmail",
            "/api/usuario/recuperarSenha/enviarCodigo",
            "/api/usuario/recuperarSenha/validarCodigo",
            "/api/usuario/recuperarSenha/redefinirSenha",

            // 🔓 Swagger/OpenAPI UI
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",

            "/download.html",
            "/index.html",
            "/home",
            "/imagens/**",
            "/imagens/*",
            "/imagens"
    };

    // Endpoints que requerem autenticação para serem acessados
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {

    };

    public static final String [] ENDPOINTS_SHARED = {
            "/api/produto/listar",
            "/api/produto/listarPorProdutoId/{produtoId}",
            "/api/categoria/listar",
            "/api/categoria/listarPorCategoriaId/{categoriaId}",
            "/api/pedido/listar",
            "/api/pedido/listar/usuario/{usuarioId}",
            "/api/pedido/listarPorPedidoId/{pedidoId}",
            "/api/usuario/listarPorUsuarioId/{usuarioId}",
            "/api/usuario/atualizar/{usuarioId}",
            "/api/usuario/foto/upload/{usuarioId}",
            "/api/usuario/foto/remover/{usuarioId}",
            "/api/pedido/atualizarStatus/{pedidoId}",
            "/api/pedidoItem/listar",
            "/api/v1/images/foto/{idProdutofoto}",
            "/api/usuario/pushToken/{usuarioId}",
            "/api/usuario/sessao/{usuarioId}",
    };

    // Endpoints que só podem ser acessador por usuários com permissão de cliente
    public static final String [] ENDPOINTS_CLIENT = {
            "/api/pedido/criar", // O cliente cria o pedido
            "/api/pedido/atualizar",
            "/api/pedidoItem/criar"
    };

    // Endpoints que só podem ser acessador por usuários com permissão de administrador
    public static final String [] ENDPOINTS_ADMIN = {
            "/api/categoria/atualizar/{categoriaId}",
            "/api/categoria/criar",
            "/api/categoria/atualizarStatus/{categoriaId}",
            "/api/categoria/apagar/{categoriaId}",

            "/api/usuario/atualizarStatus",
            "/api/usuario/listar",
            "/api/usuario/apagar/{usuarioId}",

             // Talvez o cliente precise ver itens do seu pedido? Se sim, mova para SHARED
            "/api/pedidoItem/listarPorId/{pedidoItemId}",
            "/api/pedidoItem/apagar/{pedidoItemId}",

            "/api/pedido/atualizar/{pedidoId}",
            // "/api/pedido/criar", Cliente que pode criar pedido
            "/api/pedido/apagar/{pedidoId}",

            "/api/produto/atualizar/{produtoId}",
            "/api/produto/criar",
            "/api/produto/atualizarStatus/{produtoId}",
            "/api/produto/apagar/{produtoId}",

            "/api/v1/images/foto/upload/{idProduto}",
            "/api/v1/images/{idProdutofoto}",
    };


    @Bean
    public FilterRegistrationBean<UserAuthenticationFilter> preventGlobalAuthenticationFilter(UserAuthenticationFilter filter) {
        FilterRegistrationBean<UserAuthenticationFilter> registration = new FilterRegistrationBean<>(filter);
        registration.setEnabled(false);
        return registration;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                        .requestMatchers(ENDPOINTS_SHARED).hasAnyAuthority("ROLE_ADMINISTRADOR", "ROLE_CLIENTE")
                        .requestMatchers(ENDPOINTS_ADMIN).hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(ENDPOINTS_CLIENT).hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(userAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
