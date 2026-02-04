package com.example.student_management_system.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // 1. ALLOW HOME PAGE (This is the new line!)
                        .requestMatchers("/").permitAll()

                        // RULE 1: Only TEACHERS can delete or add students
                        .requestMatchers("/students/new", "/students/delete/**").hasRole("TEACHER")

                        // RULE: Only STUDENTS can Edit
                        .requestMatchers("/students/edit/**").hasRole("STUDENT")

                        // RULE 2: Everyone (Teachers & Students) can view the list
                        .requestMatchers("/students").authenticated()

                        // Allow CSS/JS to load for everyone
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                        .defaultSuccessUrl("/students", true) // Go here after login
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/") // Go back to Home Page after logout
                        .permitAll()
                );

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // Define a STUDENT user
        UserDetails student = User.withDefaultPasswordEncoder()
                .username("student")
                .password("password")
                .roles("STUDENT")
                .build();

        // Define a TEACHER user
        UserDetails teacher = User.withDefaultPasswordEncoder()
                .username("teacher")
                .password("password")
                .roles("TEACHER")
                .build();

        return new InMemoryUserDetailsManager(student, teacher);
    }
}