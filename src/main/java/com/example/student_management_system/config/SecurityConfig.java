package com.example.student_management_system.config;

// --- THESE IMPORTS ARE CRITICAL ---
import com.example.student_management_system.service.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider; // <--- THIS FIXES YOUR ERROR
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
// ----------------------------------

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // 1. Password Encoder (Makes passwords secure)
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    // 2. Security Filter Chain (Rules)
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests((requests) -> requests
                        // Allow everyone to see Home, Login, and Signup pages
                        .requestMatchers("/", "/signup", "/register", "/login").permitAll()

                        // RESTRICTED AREAS
                        .requestMatchers("/students/new", "/students/delete/**").hasRole("TEACHER")
                        .requestMatchers("/students/edit/**").hasRole("STUDENT")
                        .requestMatchers("/students").authenticated()

                        // Allow static resources (CSS/JS)
                        .requestMatchers("/css/**", "/js/**").permitAll()
                        .anyRequest().authenticated()
                )
                .formLogin((form) -> form
                       // .loginPage("/login")
                        .defaultSuccessUrl("/students", true)
                        .permitAll()
                )
                .logout((logout) -> logout
                        .logoutSuccessUrl("/")
                        .permitAll()
                );

        return http.build();
    }

    // 3. Connect Database Auth (The Bridge)
    @Bean
    public DaoAuthenticationProvider authenticationProvider(CustomUserDetailsService userDetailsService) {
        // OLD WAY (Causes Error): new DaoAuthenticationProvider();
        // NEW WAY (Fix): Pass the service directly into the parentheses!
        DaoAuthenticationProvider auth = new DaoAuthenticationProvider(userDetailsService);

        // Remove this line because we already did it above:
        // auth.setUserDetailsService(userDetailsService);

        auth.setPasswordEncoder(passwordEncoder());
        return auth;
    }
}