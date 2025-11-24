package com.yasmin.receitix.repository;

import com.yasmin.receitix.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Usuario u SET u.status = -1 WHERE u.id = :id")
    void apagadoLogicoUsuario(@Param("id") Integer usuarioId);

    @Query("SELECT u FROM Usuario u WHERE u.status >= 0")
    List<Usuario> listarUsuarios();

    @Query("SELECT u FROM Usuario u WHERE u.id=:id AND u.status >= 0")
    Usuario obterUsuarioPeloId(@Param("id") Integer usuarioId);

    @Query("SELECT u FROM Usuario u WHERE u.email=:email AND u.status >= 0")
    Usuario findByEmail(@Param("email") String email);
}
