package com.primefit.tool.service.roleservice;

import com.primefit.tool.model.Role;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleService {

    /**
     * @param role -  the role we want to save into the DB
     * @return - the saved tole
     */
    Role save(Role role);
}
