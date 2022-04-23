package com.primefit.tool;

import com.primefit.tool.exceptions.UsernameAlreadyExistsException;
import com.primefit.tool.model.ConfirmationToken;
import com.primefit.tool.model.Role;
import com.primefit.tool.model.User;
import com.primefit.tool.service.confirmationtokenservive.ConfirmationTokenService;
import com.primefit.tool.service.roleservice.RoleService;
import com.primefit.tool.service.userservice.UserService;
import lombok.AllArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@SpringBootApplication
@AllArgsConstructor
public class PrimeFitApplication implements CommandLineRunner {

    private UserService userService;

    private RoleService roleService;

    private PasswordEncoder passwordEncoder;

    private ConfirmationTokenService confirmationTokenService;

    public static void main(String[] args) {
        SpringApplication.run(PrimeFitApplication.class, args);

    }

    @Override
    public void run(String... args) throws UsernameAlreadyExistsException {
        insertPredefinedAdmin();
    }

    private void insertPredefinedAdmin() throws UsernameAlreadyExistsException {
        String adminData = "admin";
        String adminPhoneNumber = "1234567890";
        String adminEmail = "primefittool@gmail.com";
        LocalDate dateOfBirth = LocalDate.of(2000, 12, 12);
        LocalDate GymSubscriptionStartDate = LocalDate.of(2000, 12, 12);

        if (!userService.IsUsernameAlreadyExists(adminData)) {

            Role role = new Role("ADMIN");
            roleService.save(role);
            Set<Role> set = new HashSet<>();
            set.add(role);

            User admin = new User(adminData, passwordEncoder.encode(adminData), adminData, adminData,
                    185, 91.3f, adminEmail, adminPhoneNumber, dateOfBirth, GymSubscriptionStartDate, set);
            admin.setLocked(false);
            admin.setEnabled(true);

            userService.saveOrUpdate(admin);

            ConfirmationToken adminConfirmationToken = new ConfirmationToken(adminData,
                    LocalDateTime.of(LocalDate.of(2022, 2, 2), LocalTime.of(10, 11, 19)),
                    LocalDateTime.of(LocalDate.of(2022, 2, 2), LocalTime.of(10, 11, 19)),
                    LocalDateTime.of(LocalDate.of(2022, 2, 2), LocalTime.of(10, 11, 19)),
                    admin);

            confirmationTokenService.saveConfirmationToken(adminConfirmationToken);
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
