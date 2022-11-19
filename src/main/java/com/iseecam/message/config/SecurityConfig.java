package com.iseecam.message.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity 
@EnableGlobalMethodSecurity(securedEnabled = true, prePostEnabled = true) 
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable().authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers("/test/**").permitAll()
                .antMatchers("/public/**").permitAll()
                .antMatchers("/auth/**").permitAll()
                .antMatchers("/admin/**").hasAuthority("ADMIN")
                .antMatchers("/**").authenticated().and()
                .oauth2ResourceServer()
                .jwt(jwtConfigurer -> jwtConfigurer.jwtAuthenticationConverter(grantedAuthoritiesExtractor()));
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return http.build();
    }

    private JwtAuthenticationConverter grantedAuthoritiesExtractor() {
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(
                jwt -> {
                    Map<String, Object> claims = jwt.getClaims();
                    List<String> list = (List<String>) claims.getOrDefault("cognito:groups", new ArrayList<>());
                    return list.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
                });
        return jwtAuthenticationConverter;
    }

}
