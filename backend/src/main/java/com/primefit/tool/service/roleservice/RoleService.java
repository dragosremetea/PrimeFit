package com.primefit.tool.service.roleservice;

import com.primefit.tool.model.Role;
import org.springframework.stereotype.Repository;

/**
 * Interface used for declaring the methods signatures that can be performed with a role
 */
@Repository
public interface RoleService {

    /**
     * Save a role in the DB
     *
     * @param role -  the role we want to save into the DB
     * @return - the saved tole
     */
    Role save(Role role);
}
