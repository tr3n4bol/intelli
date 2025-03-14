package com.example.sem2_LR1;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/register", "/login").permitAll() // Разрешаем доступ к регистрации и статическим ресурсам
                        .requestMatchers("/add", "/delete", "/update/1").hasRole("ADMIN") // Только администратор может добавлять, удалять и редактировать
                        .anyRequest().authenticated() // Все остальные запросы требуют аутентификации
                )
                .formLogin(form -> form
                        .loginPage("/login") // Страница входа
                        .defaultSuccessUrl("/view", true)
                        .permitAll() // Разрешаем доступ к странице входа всем
                )
                .logout(logout -> logout
                        .logoutSuccessUrl("/login") // Перенаправление после выхода
                        .permitAll()
                );
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}