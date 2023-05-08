package com.katia.spring.security.services;

import com.katia.spring.security.entities.Role;
import com.katia.spring.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RoleService {
    private final RoleRepository roleRepository;

    @Autowired
    public RoleService(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public List<Role> findByRoleNames(List<String> roleNames) {
        return roleRepository.findByRoleNameIn(roleNames);
    }

}
