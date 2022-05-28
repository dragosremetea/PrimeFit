package com.primefit.tool.service.roleservice.impl;

import com.primefit.tool.model.Role;
import com.primefit.tool.repository.RoleRepository;
import com.primefit.tool.service.roleservice.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role save(Role role) {
        return roleRepository.save(role);
    }
}
