package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Usuario;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Usuario p SET p.status = -1 WHERE p.id = :id")
    void apagadoLogicoUsuario(@Param("id") Integer usuarioId);

    @Query("SELECT p FROM Usuario p WHERE p.status >= 0")
    List<Usuario> listarUsuarios();

    @Query("SELECT p FROM Usuario p WHERE p.id=:id AND p.status >= 0")
    Usuario obterUsuarioPeloId(@Param("id") Integer usuarioId);
}
