package com.primefit.tool.service.roleservice;

import com.primefit.tool.model.Role;

import java.util.List;
import java.util.Optional;

public interface RoleService {

    List<Role> findAll();

    void save(Role role);

    void delete(Integer id);

    Optional<Role> get(Integer id);
}
