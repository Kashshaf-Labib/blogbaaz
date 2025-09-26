package com.blogbaaz.PostService.config;

import com.blogbaaz.PostService.filters.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
                        .requestMatchers("/api/posts/search").permitAll()
                        .requestMatchers("/api/posts/published").permitAll()
                        .requestMatchers("/api/posts/paginated/published").permitAll()
                        .requestMatchers("/api/posts/category/**").permitAll()
                        .requestMatchers("/api/posts/paginated/category/**").permitAll()
                        .requestMatchers("/api/posts/tags").permitAll()
                        .requestMatchers("/api/posts/{postId}").permitAll()
                        .requestMatchers("/api/posts/slug/**").permitAll()
                        .requestMatchers("/api/posts").permitAll() // GET all posts
                        .requestMatchers("/actuator/**").permitAll()
                        // Protected endpoints (authentication required)
                        .requestMatchers("/api/posts", "POST").authenticated() // Create post
                        .requestMatchers("/api/posts/{postId}", "PUT").authenticated() // Update post
                        .requestMatchers("/api/posts/{postId}", "DELETE").authenticated() // Delete post
                        .requestMatchers("/api/posts/{postId}/publish").authenticated() // Publish post
                        .requestMatchers("/api/posts/{postId}/archive").authenticated() // Archive post
                        .requestMatchers("/api/posts/{postId}/like").authenticated() // Like post
                        .requestMatchers("/api/posts/author/**").authenticated() // Author posts
                        .requestMatchers("/api/posts/paginated/author/**").authenticated() // Author posts paginated
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
