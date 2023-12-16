package com.example.yeobee.config;

import com.example.yeobee.auth.jwt.filter.JwtAuthenticationFilter;
import com.example.yeobee.auth.jwt.provider.AuthTokenProvider;
import com.example.yeobee.auth.jwt.securityUserDetails.SecurityUserDetailsService;
import com.example.yeobee.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig{

    private final AuthTokenProvider authTokenProvider;
    private final UserService userService;

    @Bean
    public WebSecurityCustomizer configure() {
        return (web) -> web.ignoring().requestMatchers(
                "/api/v1/auth/apple/login",
                "/api/v1/auth/refresh",
                "/swagger-ui/**",
                "/v3/api-docs/**"
        );
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .sessionManagement((sessionManagement) ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/v1/**").authenticated();
                    request.anyRequest().permitAll();
                })
                .anonymous(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .with(new JwtFilter(), AbstractHttpConfigurer::disable);
        return http.build();
    }
    public class JwtFilter extends AbstractHttpConfigurer<JwtFilter, HttpSecurity> {
        @Override
        public void configure(HttpSecurity http) {
            SecurityUserDetailsService userDetailsService = new SecurityUserDetailsService(userService);
            JwtAuthenticationFilter filter = new JwtAuthenticationFilter(authTokenProvider, userDetailsService);
            http.addFilterBefore(filter, BasicAuthenticationFilter.class);
        }
    }
}
