package com.shiproutepro.backend.config;



import com.shiproutepro.backend.constant.UrlConstants;
import com.shiproutepro.backend.security.JwtAuthenticationEntryPoint;
import com.shiproutepro.backend.security.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@Configuration
@RequiredArgsConstructor
public class SecurityConfig {


    private final CustomAuthenticationProvider authenticationProvider;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(configurer -> configurer.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .authorizeHttpRequests(authorize -> {
                    authorize
                            .requestMatchers(UrlConstants.WHITE_LIST_URLS).permitAll()
                            .requestMatchers(UrlConstants.ADMIN_ONLY_URLS)
                            .hasAuthority(UrlConstants.ADMIN_AUTHORITY)
                            .requestMatchers(UrlConstants.SECURITY_GUARDS_ONLY_URLS)
                            .hasAuthority(UrlConstants.SECURITY_GUARDS_AUTHORITY)
                            .requestMatchers(UrlConstants.ADMIN_AND_RESIDENT_ONLY_URLS)
                            .hasAnyAuthority(UrlConstants.ADMIN_AUTHORITY, UrlConstants.RESIDENT_AUTHORITY)
                            .anyRequest().authenticated();
                })
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> {
                    logout
                            .logoutUrl("/api/v1/auth/logout")
                            .logoutSuccessHandler((request, response, authentication) ->
                                    SecurityContextHolder.clearContext());
                });

        return http.build();
    }
}
