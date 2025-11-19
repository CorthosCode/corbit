package ru.corthos.auth.web;

import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;

@TestConfiguration
@EnableWebSecurity
@Profile("test")
public class SecurityTestConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth-page", "/profile").authenticated()
                        .requestMatchers("/auth-check").permitAll()
                        .anyRequest().denyAll()
                )
                .formLogin(form -> form
                        .loginPage("/login") // <-- Указываем страницу логина
                        .permitAll() // <-- Разрешаем всем доступ к /login
                )
                .oauth2Login(oauth -> oauth.disable()) // <-- Отключаем OAuth2
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/")
                        .permitAll()
                );
        return http.build();
    }

}
