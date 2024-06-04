package ru.gudkova.mongoproject.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final UserDetailsService userDetailsService;
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests((requests) -> requests
                        .requestMatchers("/security/login").permitAll()
                        .requestMatchers("/login/process").permitAll()
                        .anyRequest().authenticated()
        )
                .formLogin((form) -> form.loginPage("/security/login")
                        .defaultSuccessUrl("/home", true)
                        .failureUrl("/security/login")
                        .loginProcessingUrl("/login/process"))
                .logout((logout) -> logout.permitAll())
                .csrf((csrf) -> csrf.disable())
                .userDetailsService(userDetailsService);
        return http.build();
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new PasswordEnconderTest();
    }
}
