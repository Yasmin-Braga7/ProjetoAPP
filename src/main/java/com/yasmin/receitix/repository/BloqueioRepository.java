package com.yasmin.receitix.repository;

import com.yasmin.receitix.entity.Bloqueio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BloqueioRepository extends JpaRepository<Bloqueio, Integer> {
}
