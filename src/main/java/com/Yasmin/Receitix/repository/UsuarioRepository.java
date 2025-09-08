package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Bloqueio;
import com.Yasmin.Receitix.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Integer> {
}
