package com.todolist.api.security;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
public class SecurityTestConfig {

    @Bean
    @Primary
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(authorize -> authorize
                .anyRequest().permitAll() // Permite todas as requisições
            )
            .csrf(csrf -> csrf.disable()); // Desativa a proteção CSRF para testes
        return http.build();
    }
}