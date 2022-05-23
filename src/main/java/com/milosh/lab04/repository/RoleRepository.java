package com.milosh.lab04.repository;

import com.milosh.lab04.models.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findByType(Role.Types role);
}
