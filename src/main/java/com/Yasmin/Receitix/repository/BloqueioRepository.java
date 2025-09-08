package com.Yasmin.Receitix.repository;

import com.Yasmin.Receitix.entity.Bloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueioRepository extends JpaRepository<Bloqueio, Integer> {
}
