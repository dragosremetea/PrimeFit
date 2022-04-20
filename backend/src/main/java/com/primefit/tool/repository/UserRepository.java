package com.primefit.tool.repository;


import com.primefit.tool.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    @Query("SELECT u from User u Where u.username = :username")
    Optional<User> getUserByUsername(@Param("username") String username);

    @Query("SELECT u from User u Where u.email = :email")
    Optional<User> getUserByEmail(@Param("email") String email);

    @Transactional
    @Modifying
    @Query("UPDATE User a SET a.enabled = TRUE WHERE a.email = ?1")
    int enableAppUser(String email);
}
