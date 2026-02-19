package com.logicoy.smartprescriptiontracker.controller;

import com.logicoy.smartprescriptiontracker.repository.UserRepository;
import com.logicoy.smartprescriptiontracker.security.JwtService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.*;
import org.springframework.security.crypto.password.
        PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Handles authentication and JWT issuance.
 */
@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication API",
        description = "Login and token generation")
public class AuthController {

    private static final Logger log =
            LoggerFactory.getLogger(AuthController.class);

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthController(
            UserRepository userRepository,
            JwtService jwtService,
            PasswordEncoder passwordEncoder) {

        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    @Operation(summary = "User Login",
            description = "Authenticates user and returns JWT token")
    public Map<String, String> login(
            @RequestParam String username,
            @RequestParam String password) {

        log.info("Login attempt for user={}", username);

        var user =
                userRepository.findByUsername(username)
                        .orElseThrow();

        if (!passwordEncoder.matches(password,
                user.getPassword())) {

            log.warn("Invalid login attempt for user={}", username);
            throw new RuntimeException("Invalid credentials");
        }

        String token =
                jwtService.generateToken(username);

        log.info("JWT issued for user={}", username);

        return Map.of("token", token);
    }
}
