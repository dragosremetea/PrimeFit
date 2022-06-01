package com.primefit.tool.service.roleservice.impl;

import com.primefit.tool.model.Role;
import com.primefit.tool.repository.RoleRepository;
import com.primefit.tool.service.roleservice.RoleService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Service class used for managing roles.
 */
@Service
@AllArgsConstructor
public class RoleServiceImpl implements RoleService {

    private RoleRepository roleRepository;

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
