package com.example.alten.configs;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
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
public class SecurityConfig {

    private final JwtAuthenticationFilter JwtAuthenticationFilter;
    private final UserDetailsService userDetailsService;

    // Constructor injection for required dependencies
    public SecurityConfig(JwtAuthenticationFilter JwtAuthenticationFilter, 
                     UserDetailsService userDetailsService) {
    this.JwtAuthenticationFilter = JwtAuthenticationFilter;
    this.userDetailsService = userDetailsService;
}
    /* 
     * Main security configuration
     * Defines endpoint access rules and JWT filter setup
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            // Disable CSRF (not needed for stateless JWT)
            .csrf(csrf -> csrf.disable())

            // Enable CORS
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))

            // Configure endpoint authorization
            .authorizeHttpRequests(auth -> auth
                // Public endpoints
                .requestMatchers("/account", "/token").permitAll()
                
                // Swagger endpoints
                .requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                
                // Role-based endpoints
                /* .requestMatchers("/auth/user/**").hasAuthority("ROLE_USER")
                .requestMatchers("/auth/admin/**").hasAuthority("ROLE_ADMIN")
                */
                // All other endpoints require authentication
                .anyRequest().authenticated()
            )

            // Stateless session (required for JWT)
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            
            // Set custom authentication provider
            .authenticationProvider(authenticationProvider())
            
            // Add JWT filter before Spring Security's default filter
            .addFilterBefore(JwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /* 
     * Password encoder bean (uses BCrypt hashing)
     * Critical for secure password storage
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /* 
     * Authentication provider configuration
     * Links UserDetailsService and PasswordEncoder
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /* 
     * Authentication manager bean
     * Required for programmatic authentication (e.g., in /generateToken)
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /* 
     * CORS configuration
     * Allows cross-origin requests from specified origins
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        
        // Allow requests from these origins (adjust for production)
        configuration.setAllowedOrigins(Arrays.asList("http://localhost:4200", "http://localhost:3000", "http://localhost:8080"));
        
        // Allow these HTTP methods
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        
        // Allow these headers
        configuration.setAllowedHeaders(Arrays.asList("*"));
        
        // Allow credentials (cookies, authorization headers)
        configuration.setAllowCredentials(true);
        
        // How long the response from a pre-flight request can be cached
        configuration.setMaxAge(3600L);
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        
        return source;
    }
}