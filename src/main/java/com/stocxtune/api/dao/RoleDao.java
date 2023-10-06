package com.stocxtune.api.dao;

import com.stocxtune.api.model.role.ERole;
import com.stocxtune.api.model.role.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleDao extends JpaRepository<Role,Long> {

    Optional<Role> findByName(ERole name);

}
