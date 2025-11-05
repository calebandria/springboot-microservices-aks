package com.ecom.userservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import javax.crypto.spec.SecretKeySpec;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
public class ResourceServerConfig {

    // IMPORTANT: This key MUST match the secret key used by the Auth Service to sign JWTs.
    // In production, this would be loaded from a secret manager/environment variable.
    private static final String JWT_SECRET = "your-very-long-and-secure-secret-key-that-is-at-least-32-bytes"; 

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // 1. Disable CSRF and Session Management (Standard for REST APIs)
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))

            // 2. Authorization Rules
            .authorizeHttpRequests(auth -> auth
                // Allow internal communication from the Auth Service (if needed for internal health checks)
                // Use .authenticated() for all exposed endpoints.
                // Replace /api/** with your specific public paths (e.g., /users/**)
                .requestMatchers("/actuator/**").permitAll() // Example: allow health checks
                .anyRequest().authenticated() 
            )

            // 3. Configure Resource Server (JWT Validation)
            // This is the core configuration for checking the Bearer token.
            .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> jwt.decoder(jwtDecoder())));

        return http.build();
    }

    /**
     * Defines the decoder to validate the JWT signature using the shared secret key.
     */
    @Bean
    public JwtDecoder jwtDecoder() {
        // Create the secret key specification
        SecretKeySpec secretKey = new SecretKeySpec(JWT_SECRET.getBytes(), 0, JWT_SECRET.getBytes().length, "HmacSHA256");
        
        // Build and return the decoder
        return NimbusJwtDecoder.withSecretKey(secretKey).build();
    }
}