package com.blogbaaz.CommentService.config;

import com.blogbaaz.CommentService.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        // Public endpoints (no authentication required)
                        .requestMatchers("/api/comments/post/**").permitAll()
                        .requestMatchers("/api/comments/search").permitAll()
                        .requestMatchers("/api/comments/{commentId}").permitAll() // GET by ID
                        .requestMatchers("/api/comments/{commentId}/replies").permitAll() // GET replies
                        .requestMatchers("/api/comments/post/{postId}/count").permitAll() // GET count
                        .requestMatchers("/api/comments/{commentId}/reply-count").permitAll() // GET reply count
                        .requestMatchers("/actuator/**").permitAll()
                        // Protected endpoints (authentication required)
                        .requestMatchers(HttpMethod.POST, "/api/comments").authenticated() // Create comment
                        .requestMatchers(HttpMethod.PUT, "/api/comments/{commentId}").authenticated() // Update comment
                        .requestMatchers(HttpMethod.DELETE, "/api/comments/{commentId}").authenticated() // Delete comment
                        .requestMatchers(HttpMethod.POST, "/api/comments/{commentId}/like").authenticated() // Like comment
                        .requestMatchers(HttpMethod.POST, "/api/comments/{commentId}/unlike").authenticated() // Unlike comment
                        .requestMatchers("/api/comments/author/**").authenticated() // Get user's comments
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

