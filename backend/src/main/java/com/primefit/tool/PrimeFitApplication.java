package com.primefit.tool;

import com.primefit.tool.model.Role;
import com.primefit.tool.model.User;
import com.primefit.tool.service.RoleService;
import com.primefit.tool.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication
public class PrimeFitApplication implements CommandLineRunner {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static void main(String[] args) {
        SpringApplication.run(PrimeFitApplication.class, args);
    }

    @Override
    public void run(String... args) {
        insertPredefinedAdmin();
    }

    private void insertPredefinedAdmin() {
        String adminData = "admin";

        if (!userService.checkIfUsernameAlreadyExists(adminData)) {

            Role role = new Role();
            role.setName("ADMIN");
            roleService.save(role);

            User admin = new User();
            Set<Role> set = new HashSet<>();
            set.add(role);

            LocalDate dateOfBirth = LocalDate.of(2000, 12, 12);
            LocalDate GymSubscriptionStartDate = LocalDate.of(2000, 12, 12);

            admin.setUsername(adminData);
            admin.setPassword(passwordEncoder.encode(adminData));
            admin.setFirstName(adminData);
            admin.setLastName(adminData);
            admin.setHeight(185);
            admin.setWeight(91.3f);
            admin.setDateOfBirth(dateOfBirth);
            admin.setGymSubscriptionStartDate(GymSubscriptionStartDate);
            admin.setEmail(adminData);
            admin.setPhoneNumber("1234567890");
            admin.setRoles(set);
            userService.save(admin);
        }
    }
}
