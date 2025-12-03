package com.yasmin.receitix.config;

import org.springframework.beans.factory.annotation.Autowired;
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
            "/api/usuario/login", // Url que usaremos para fazer login
            "/api/usuario/criar", // Url que usaremos para criar um usuário
            // 🔓 Swagger/OpenAPI UI
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };

    // Endpoints que requerem autenticação para serem acessados
    public static final String [] ENDPOINTS_WITH_AUTHENTICATION_REQUIRED = {

    };

    // Endpoints que só podem ser acessador por usuários com permissão de cliente
    public static final String [] ENDPOINTS_CLIENT = {
            "/api/usuario/atualizar/{usuarioId}",
            "/api/pedido/atualizar",
            "/api/pedido/listar",
            "/api/produto/listar",
            "/api/categoria/listar",
    };

    // Endpoints que só podem ser acessador por usuários com permissão de administrador
    public static final String [] ENDPOINTS_ADMIN = {
            "/api/categoria/atualizar/{categoriaId}",
            "/api/categoria/criar",
            "/api/categoria/atualizarStatus/{categoriaId}",
            "/api/categoria/listar",
            "/api/categoria/listarPorCategoriaId/{categoriaId}",
            "/api/categoria/apagar/{categoriaId}",

            "/api/usuario/atualizarStatus",
            "/api/usuario/listar",
            "/api/usuario/listarPorUsuarioId/{usuarioId}",
            "/api/usuario/apagar/{usuarioId}",

            "/api/pedidoItem/criar",
            "/api/pedidoItem/listar",
            "/api/pedidoItem/listarPorId/{pedidoItemId}",
            "/api/pedidoItem/apagar/{pedidoItemId}",

            "/api/pedido/atualizar/{pedidoId}",
            "/api/pedido/criar",
            "/api/pedido/atualizarStatus/{pedidoId}",
            "/api/pedido/listar",
            "/api/pedido/listarPorPedidoId/{pedidoId}",
            "/api/pedido/apagar/{pedidoId}",

            "/api/produto/atualizar/{produtoId}",
            "/api/produto/criar",
            "/api/produto/atualizarStatus/{produtoId}",
            "/api/produto/listar",
            "/api/produto/listarPorProdutoId/{produtoId}",
            "/api/produto/apagar/{produtoId}"
    };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_NOT_REQUIRED).permitAll()
                        .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll() //adicionado para funcionamento do swagger
                        .requestMatchers(ENDPOINTS_ADMIN).hasAuthority("ROLE_CLIENTE")
                        .requestMatchers(ENDPOINTS_CLIENT).hasAuthority("ROLE_ADMINISTRADOR")
                        .requestMatchers(ENDPOINTS_WITH_AUTHENTICATION_REQUIRED).authenticated()
                        .anyRequest().denyAll()
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
