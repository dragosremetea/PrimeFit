package com.primefit.tool.service.roleservice;

import com.primefit.tool.model.Role;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleService {

    /**
     *
     * @param role -  the role we want to save into the DB
     * @return - the saved tole
     */
    Role save(Role role);
}
