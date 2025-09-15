package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Bloqueio;
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
    @Query("UPDATE Participante p SET p.status = -1 WHERE p.id = :id")
    void apagadoLogicoPedidoItem(@Param("id") Integer pedidoItemId);

    @Query("SELECT p FROM Participante p WHERE p.status >= 0")
    List<PedidoItem> listarPedidosItens();

    @Query("SELECT p FROM Participante p WHERE p.id=:id AND p.status >= 0")
    PedidoItem obterPedidoItemPeloId(@Param("id") Integer pedidoItemId);
}
