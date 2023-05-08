package com.katia.spring.security.repositories;

import com.katia.spring.security.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findAll();
    List<Role> findByRoleNameIn(List<String> roleNames);
}


