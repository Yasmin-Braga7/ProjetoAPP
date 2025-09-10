package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Bloqueio;
import com.Yasmin.Receitix.entity.Pedido;
import com.Yasmin.Receitix.entity.Produto;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Produto p SET p.status = -1 WHERE p.id = :id")
    void apagarProduto(@Param("id") Integer produtoId);

    @Query("SELECT p FROM Produto p WHERE p.status >= 0")
    List<Produto> listarProdutos();

    @Query("SELECT p FROM Produto p WHERE p.id=:id AND p.status >= 0")
    Produto obterProdutoPeloId(@Param("id") Integer produtoId);
}
