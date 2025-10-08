package com.sairajtravels.site.config;

import com.sairajtravels.site.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Value("${app.cors.allowed-origins:*}")
    private String allowedOrigins;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // Allow all requests for testing
                .anyRequest().permitAll()
                // Public endpoints
                .requestMatchers("/api/auth/**").permitAll()
                .requestMatchers("/api/contact/**").permitAll()
                .requestMatchers("/api/enquiries/**").permitAll()
                .requestMatchers("/api/vehicles/public/**").permitAll()
                .requestMatchers("/api/drivers/public/**").permitAll()
                .requestMatchers("/api/packages/public/**").permitAll()
                .requestMatchers("/api/gallery/public/**").permitAll()
                .requestMatchers("/api/testimonials/public/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                .requestMatchers("/").permitAll()
                .requestMatchers("/health").permitAll()
                .requestMatchers("/test").permitAll()
                
                // Static resources
                .requestMatchers("/images/**").permitAll()
                .requestMatchers("/static/**").permitAll()
                
                // Admin endpoints require authentication
                .requestMatchers("/api/admin/**").authenticated()
                .requestMatchers("/api/vehicles/**").authenticated()
                .requestMatchers("/api/drivers/**").authenticated()
                .requestMatchers("/api/bookings/**").authenticated()
                .requestMatchers("/api/packages/**").authenticated()
                .requestMatchers("/api/gallery/**").authenticated()
                .requestMatchers("/api/testimonials/**").authenticated()
                .requestMatchers("/api/routes/**").authenticated()
                .requestMatchers("/api/users/**").authenticated()
                .requestMatchers("/api/roles/**").authenticated()
                
                // All other requests require authentication
                .anyRequest().authenticated()
            )
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Configure allowed origins
        if ("*".equals(allowedOrigins)) {
            configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        } else {
            configuration.setAllowedOrigins(Arrays.asList(allowedOrigins.split(",")));
        }
        
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        configuration.setExposedHeaders(Arrays.asList("Authorization"));
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", configuration);
        return source;
    }
}