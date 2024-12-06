package com.br.cuidaidoso.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @SuppressWarnings("deprecation")
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests(requests -> requests
                        .requestMatchers("/", "/home", "/register/**", "/cuidador/cadastro",
                                "/cuidador/endereco/cadastro", "/cliente/endereco/cadastro", "/cliente/cadastro",
                                "/css/**", "/images/**", "/js/**", "cliente/cadastro-cliente",
                                "cuidador/cadastro-endereco", "cliente/cadastro-endereco",
                                "/cuidador/cadastro-cuidador", "/endereco/cadastro", "/endereco/cadastro-endereco")
                        .permitAll()
                        .anyRequest().authenticated())
                .formLogin(login -> login
                        .loginPage("/login")
                        .failureHandler(authenticationFailureHandler())
                        .successHandler(authenticationSuccessHandler())
                        .permitAll())
                .logout(logout -> logout
                        .permitAll());
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return (request, response, authentication) -> {
            String role = authentication.getAuthorities().iterator().next().getAuthority();
            if (role.equals("ROLE_CUIDADOR")) {
                response.sendRedirect("/cuidador/list-cuidadores");
            } else if (role.equals("ROLE_CLIENTE")) {
                response.sendRedirect("/cliente/list-clientes");
            } else if (role.equals("ROLE_ADMIN")) {
                response.sendRedirect("/admin/list-admin");
            } else {
                response.sendRedirect("/home");
            }
        };
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return (request, response, exception) -> {
            response.sendRedirect("/login?error");
        };
    }
}