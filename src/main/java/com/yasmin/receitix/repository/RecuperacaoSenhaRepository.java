package com.yasmin.receitix.repository;

import com.yasmin.receitix.entity.RecuperacaoSenha;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface RecuperacaoSenhaRepository extends JpaRepository<RecuperacaoSenha, Integer> {

    // Apaga qualquer solicitação anterior do usuário antes de criar uma nova,
    // assim só existe 1 código/token "ativo" por usuário por vez.
    @Modifying
    @Transactional
    @Query("DELETE FROM RecuperacaoSenha r WHERE r.usuarioId = :usuarioId")
    void apagarPorUsuario(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT r FROM RecuperacaoSenha r WHERE r.usuarioId = :usuarioId")
    RecuperacaoSenha buscarPorUsuarioId(@Param("usuarioId") Integer usuarioId);

    @Query("SELECT r FROM RecuperacaoSenha r WHERE r.resetToken = :resetToken")
    RecuperacaoSenha buscarPorResetToken(@Param("resetToken") String resetToken);
}
