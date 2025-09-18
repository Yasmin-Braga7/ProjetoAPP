package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Categoria;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoriaRepository extends JpaRepository<Categoria, Integer> {
    @Modifying
    @ Transactional
    @Query("UPDATE Categoria c SET c.status = -1 WHERE c.id = :id")
    void apagadoLogicoCategoria(@Param("id") Integer categoriaId);

    @Query("SELECT c FROM Categoria c WHERE c.status >= 0")
    List<Categoria> listarCategorias();

    @Query("SELECT c FROM Categoria c WHERE c.id=:id AND c.status >= 0")
    Categoria obterCategoriaPeloId(@Param("id") Integer categoriaId);
}
