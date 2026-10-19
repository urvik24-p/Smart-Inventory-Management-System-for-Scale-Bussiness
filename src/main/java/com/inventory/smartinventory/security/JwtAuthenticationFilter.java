package com.inventory.smartinventory.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Intercepts every incoming HTTP request BEFORE it reaches the controller.
 *
 * How it works:
 *   1. Reads the "Authorization" header from the request
 *   2. If the header starts with "Bearer ", extracts the JWT token
 *   3. Uses JwtUtil to extract the username from the token
 *   4. Loads the full user details from the database
 *   5. Validates the token (signature + expiry + username match)
 *   6. If valid, sets the authentication in Spring Security's context
 *      so the request is treated as authenticated
 *
 * If there is no token, or the token is invalid, the request continues
 * without authentication - Spring Security will then block it
 * if the endpoint requires authentication (configured in SecurityConfig).
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil,
                                   CustomUserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // --- Step 1: Read the Authorization header ---
        String authHeader = request.getHeader("Authorization");

        // If no header or wrong format, skip JWT processing
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // --- Step 2: Extract the token (everything after "Bearer ") ---
        String token = authHeader.substring(7);

        // --- Step 3: Extract the username from the token ---
        String username;
        try {
            username = jwtUtil.extractUsername(token);
        } catch (Exception ignored) {
            filterChain.doFilter(request, response);
            return;
        }

        // --- Step 4: If username found and not already authenticated ---
        if (username != null &&
                SecurityContextHolder.getContext().getAuthentication() == null) {

            // Load user from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            // --- Step 5: Validate the token ---
            if (jwtUtil.isTokenValid(token, userDetails.getUsername())) {

                // --- Step 6: Set authentication in the security context ---
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );

                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}
