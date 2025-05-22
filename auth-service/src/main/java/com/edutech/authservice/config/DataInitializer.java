package com.edutech.authservice.config;

import com.edutech.authservice.model.ERole;
import com.edutech.authservice.model.Role;
import com.edutech.authservice.model.User;
import com.edutech.authservice.repository.RoleRepository;
import com.edutech.authservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Inicializar roles
        if (roleRepository.count() == 0) {
            roleRepository.save(new Role(ERole.ROLE_STUDENT));
            roleRepository.save(new Role(ERole.ROLE_INSTRUCTOR));
            roleRepository.save(new Role(ERole.ROLE_ADMIN));
        }

        // Crear usuario administrador por defecto
        if (!userRepository.existsByUsername("admin")) {
            User admin = new User(
                    "admin",
                    "admin@edutech.com",
                    passwordEncoder.encode("admin123")
            );

            Set<Role> roles = new HashSet<>();
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(adminRole);
            admin.setRoles(roles);

            userRepository.save(admin);
        }
    }
}
