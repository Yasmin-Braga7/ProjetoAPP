package com.yasmin.receitix.entity;

import com.yasmin.receitix.enums.CanalRecuperacao;
import jakarta.persistence.*;

import java.time.LocalDateTime;

/**
 * Guarda os dados temporários do fluxo de "Esqueci minha senha":
 * o código de verificação enviado (por e-mail ou SMS), o token de redefinição
 * gerado após a validação do código, e os respectivos prazos de validade.
 *
 * A cada nova solicitação de recuperação para um usuário, os registros
 * anteriores dele são apagados (ver RecuperacaoSenhaRepository.apagarPorUsuario),
 * então só existe no máximo 1 registro "ativo" por usuário por vez.
 */
@Entity(name = "RecuperacaoSenha")
@Table(name = "recuperacao_senha")
public class RecuperacaoSenha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "recuperacao_id")
    private Integer id;

    @Column(name = "usuario_id", nullable = false)
    private Integer usuarioId;

    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "codigo", nullable = false, length = 6)
    private String codigo;

    @Enumerated(EnumType.STRING)
    @Column(name = "canal", nullable = false, length = 10)
    private CanalRecuperacao canal;

    @Column(name = "reset_token", length = 36)
    private String resetToken;

    @Column(name = "codigo_expiracao", nullable = false)
    private LocalDateTime codigoExpiracao;

    @Column(name = "token_expiracao")
    private LocalDateTime tokenExpiracao;

    @Column(name = "codigo_validado", nullable = false)
    private boolean codigoValidado;

    @Column(name = "senha_redefinida", nullable = false)
    private boolean senhaRedefinida;

    @Column(name = "criado", nullable = false)
    private LocalDateTime criado;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public CanalRecuperacao getCanal() {
        return canal;
    }

    public void setCanal(CanalRecuperacao canal) {
        this.canal = canal;
    }

    public String getResetToken() {
        return resetToken;
    }

    public void setResetToken(String resetToken) {
        this.resetToken = resetToken;
    }

    public LocalDateTime getCodigoExpiracao() {
        return codigoExpiracao;
    }

    public void setCodigoExpiracao(LocalDateTime codigoExpiracao) {
        this.codigoExpiracao = codigoExpiracao;
    }

    public LocalDateTime getTokenExpiracao() {
        return tokenExpiracao;
    }

    public void setTokenExpiracao(LocalDateTime tokenExpiracao) {
        this.tokenExpiracao = tokenExpiracao;
    }

    public boolean isCodigoValidado() {
        return codigoValidado;
    }

    public void setCodigoValidado(boolean codigoValidado) {
        this.codigoValidado = codigoValidado;
    }

    public boolean isSenhaRedefinida() {
        return senhaRedefinida;
    }

    public void setSenhaRedefinida(boolean senhaRedefinida) {
        this.senhaRedefinida = senhaRedefinida;
    }

    public LocalDateTime getCriado() {
        return criado;
    }

    public void setCriado(LocalDateTime criado) {
        this.criado = criado;
    }
}
