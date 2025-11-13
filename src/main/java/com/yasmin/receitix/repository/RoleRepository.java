package com.yasmin.receitix.repository;


import com.yasmin.receitix.entity.Role;
import com.yasmin.receitix.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName (RoleName name);
}
