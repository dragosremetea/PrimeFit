package com.primefit.tool;

import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.model.Role;
import com.primefit.tool.model.User;
import com.primefit.tool.service.roleservice.RoleService;
import com.primefit.tool.service.userservice.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
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
    public void run(String... args) throws UsernameAlreadyExistsException {
        insertPredefinedAdmin();
    }

    private void insertPredefinedAdmin() throws UsernameAlreadyExistsException {
        String adminData = "admin";

        if (!userService.IsUsernameAlreadyExists(adminData)) {

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

    @Bean
    public CorsFilter corsFilter() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:4200"));
        corsConfiguration.setAllowedHeaders(Arrays.asList("Origin", "Access-Control-Allow-Origin", "Content-Type",
                "Accept", "Authorization", "Origin, Accept", "X-Requested-With",
                "Access-Control-Request-Method", "Access-Control-Request-Headers"));
        corsConfiguration.setExposedHeaders(Arrays.asList("Origin", "Content-Type", "Accept", "Authorization",
                "Access-Control-Allow-Origin", "Access-Control-Allow-Origin", "Access-Control-Allow-Credentials"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        UrlBasedCorsConfigurationSource urlBasedCorsConfigurationSource = new UrlBasedCorsConfigurationSource();
        urlBasedCorsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);
        return new CorsFilter(urlBasedCorsConfigurationSource);
    }
}
