package com.yasmin.receitix.service;

import com.yasmin.receitix.entity.Role;
import com.yasmin.receitix.entity.RoleName;
import com.yasmin.receitix.repository.RoleRepository;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    private final RoleRepository roleRepository;

    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public Role getRolesByName(RoleName roleName){
        return roleRepository.findByName(roleName).orElse(null);
    }

}