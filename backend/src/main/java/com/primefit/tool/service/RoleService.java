package com.primefit.tool.service;

import com.primefit.tool.model.Role;
import com.primefit.tool.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public List<Role> findAll() {
        return roleRepository.findAll();
    }

    public void save(Role role) {
        roleRepository.save(role);
    }

    public void delete(Integer id) {
        roleRepository.deleteById(id);
    }

    public Optional<Role> get(Integer id) {
        return roleRepository.findById(id);

    }
}
