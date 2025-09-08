package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Pedido;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer> {
    @Modifying
    @Transactional
    @Query("UPDATE Pedido p SET p.status = -1 WHERE p.id = :id")
    void apagadoLogicoPedido(@Param("id") Integer pedidoId);

    @Query("SELECT p FROM Pedido p WHERE p.status >= 0")
    List<Pedido> listarPedidos();

    @Query("SELECT p FROM Pedido p WHERE p.id=:id AND p.status >= 0")
    Pedido obterPedidoPeloId(@Param("id") Integer participanteId);
}
