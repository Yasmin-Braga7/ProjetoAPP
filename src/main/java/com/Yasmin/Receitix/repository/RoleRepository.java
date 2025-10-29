package com.Yasmin.Receitix.repository;


import com.Yasmin.Receitix.entity.Role;
import com.Yasmin.Receitix.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Optional<Role> findByName (RoleName name);
}
