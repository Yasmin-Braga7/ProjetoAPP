package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.PedidoItem;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PedidoItemRepository extends JpaRepository<PedidoItem, Integer> {

    @Modifying
    @Transactional
    @Query("UPDATE PedidoItem pi SET pi.subtotal = -1 WHERE pi.id = :id")
    void apagadoLogicoPedidoItem(@Param("id") Integer pedidoItemId);

    @Query("SELECT pi FROM PedidoItem pi WHERE pi.subtotal >= 0")
    List<PedidoItem> listarPedidoItens();

    @Query("SELECT pi FROM PedidoItem pi WHERE pi.id=:id AND pi.subtotal >= 0")
    PedidoItem obterPedidoItemPeloId(@Param("id") Integer pedidoItemId);
}

