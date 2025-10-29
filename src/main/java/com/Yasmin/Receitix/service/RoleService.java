package com.Yasmin.Receitix.service;

import com.Yasmin.Receitix.entity.Role;
import com.Yasmin.Receitix.entity.RoleName;
import com.Yasmin.Receitix.repository.RoleRepository;
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