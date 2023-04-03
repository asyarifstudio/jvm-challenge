package com.asyarifstudio.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    @Bean
    public SecurityWebFilterChain sprinngSecurityWebFilterChain(ServerHttpSecurity serverHttpSecurity){
        serverHttpSecurity
            .csrf().disable()
            .authorizeExchange(exchange -> 
                exchange
                .pathMatchers(HttpMethod.POST,"/api/review")
                .authenticated()
                .pathMatchers(HttpMethod.PUT,"/api/review/**")
                .authenticated()
                .anyExchange()
                .permitAll()
            ).oauth2ResourceServer(ServerHttpSecurity.OAuth2ResourceServerSpec::jwt);

        return serverHttpSecurity.build();
    }
    
}
