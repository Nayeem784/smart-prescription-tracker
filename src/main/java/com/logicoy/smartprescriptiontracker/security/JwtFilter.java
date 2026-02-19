package com.logicoy.smartprescriptiontracker.security;

import com.logicoy.smartprescriptiontracker.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.
        UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.
        SecurityContextHolder;
import org.springframework.security.core.authority.
        SimpleGrantedAuthority;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Validates JWT in Authorization header.
 * If valid, sets authentication in SecurityContext.
 */
@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger log =
            LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;
    private final UserRepository userRepository;

    public JwtFilter(JwtService jwtService,
                     UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header =
                request.getHeader("Authorization");

        if (header != null &&
                header.startsWith("Bearer ")) {

            String token = header.substring(7);

            String username =
                    jwtService.extractUsername(token);

            log.info("Validating JWT for user={}", username);

            userRepository.findByUsername(username)
                    .ifPresent(user -> {

                        var auth =
                                new UsernamePasswordAuthenticationToken(
                                        username,
                                        null,
                                        List.of(
                                                new SimpleGrantedAuthority(
                                                        user.getRole()
                                                )
                                        )
                                );

                        SecurityContextHolder.getContext()
                                .setAuthentication(auth);
                    });
        }

        filterChain.doFilter(request, response);
    }
}
