package com.logicoy.smartprescriptiontracker.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

/**
 * CorrelationIdFilter
 *
 * Assigns a unique correlation ID to every incoming request.
 * The ID is:
 *  - Read from request header (if present)
 *  - Generated if missing
 *  - Added to response header
 *  - Added to logging context (MDC)
 */
@Component
public class CorrelationIdFilter extends OncePerRequestFilter {

    public static final String CORRELATION_ID = "X-Correlation-Id";

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        // Read correlation ID from request header
        String correlationId =
                request.getHeader(CORRELATION_ID);

        // Generate new ID if not present
        if (correlationId == null || correlationId.isBlank()) {
            correlationId = UUID.randomUUID().toString();
        }

        // Store correlation ID in MDC for logging
        MDC.put(CORRELATION_ID, correlationId);

        // Send correlation ID back in response header
        response.setHeader(CORRELATION_ID, correlationId);

        try {
            // Continue filter chain
            filterChain.doFilter(request, response);
        } finally {
            // Clear MDC to avoid memory leaks
            MDC.clear();
        }
    }
}
