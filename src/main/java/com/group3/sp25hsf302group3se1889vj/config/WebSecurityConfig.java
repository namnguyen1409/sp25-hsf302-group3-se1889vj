package com.group3.sp25hsf302group3se1889vj.config;


import com.group3.sp25hsf302group3se1889vj.security.filter.JwtAuthenticationFilter;
import com.group3.sp25hsf302group3se1889vj.security.handler.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class WebSecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private final JwtAuthenticationFilter jwtAuthenticationFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(
                csrf -> csrf.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
        ).authorizeHttpRequests(
                authz -> authz
                        .requestMatchers(new LoggingRequestMatcher()).permitAll()
                        .requestMatchers(
                                "/admin/**"
                        ).hasAnyRole("OWNER", "STAFF")
                        .anyRequest().permitAll()
        ).formLogin(AbstractHttpConfigurer::disable
        ).addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }



}
