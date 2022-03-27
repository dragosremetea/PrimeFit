package com.primefit.tool.repository;


import com.primefit.tool.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT u from User u Where u.username = :username")
    Optional<User> getUserByUsername(@Param("username") String username);

    @Query("SELECT u from User u Where u.email = :email")
    Optional<User> getUserByEmail(@Param("email") String email);
}
