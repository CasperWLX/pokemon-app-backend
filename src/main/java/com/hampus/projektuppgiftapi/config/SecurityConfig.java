package com.hampus.projektuppgiftapi.config;

import com.hampus.projektuppgiftapi.filter.JwtAuthenticationFilter;
import com.hampus.projektuppgiftapi.model.user.UserRoles;
import com.hampus.projektuppgiftapi.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.jaas.memory.InMemoryConfiguration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsConfigurationSource;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import static com.hampus.projektuppgiftapi.model.user.UserPermissions.*;

import java.util.List;

@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {

    private final JwtUtil jwtUtil;

    @Autowired
    public SecurityConfig(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Bean
    public SecurityWebFilterChain securityFilterChainConfig(ServerHttpSecurity http) {
        http.cors(Customizer.withDefaults())
                .authorizeExchange(auth -> auth
                        .pathMatchers("/webjars/**", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .pathMatchers(HttpMethod.GET, "/database/v2/pokemon/*").permitAll()
                        .pathMatchers(HttpMethod.POST, "/user/v1/register", "/user/v1/login").permitAll()
                        .pathMatchers(HttpMethod.POST, "/database/v2/pokemon/*").hasAuthority(ADMIN_CAN_WRITE.getPERMISSIONS())
                        .pathMatchers(HttpMethod.DELETE, "/database/v2/pokemon/*").hasAuthority(ADMIN_CAN_WRITE.getPERMISSIONS())
                        .pathMatchers(HttpMethod.GET,"/user/v1/info").hasAuthority(USER_CAN_READ.getPERMISSIONS())
                        .pathMatchers(HttpMethod.PUT, "/user/v1/update").hasAnyAuthority(USER_CAN_WRITE.getPERMISSIONS(), ADMIN_CAN_WRITE.getPERMISSIONS())
                        .pathMatchers(HttpMethod.DELETE, "/user/v1/delete/*").hasAuthority(ADMIN_CAN_WRITE.getPERMISSIONS())
                        .anyExchange().authenticated()
                )
                .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
                .formLogin(ServerHttpSecurity.FormLoginSpec::disable)
                .csrf(ServerHttpSecurity.CsrfSpec::disable)
                .addFilterAt(new JwtAuthenticationFilter(jwtUtil), SecurityWebFiltersOrder.AUTHENTICATION);

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(List.of("http://localhost:8081", "http://192.168.8.152:8081", "https://pokemongame.hampuskallberg.se"));
        corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfiguration.setAllowedHeaders(List.of("Authorization", "Content-Type", "Cookie"));
        corsConfiguration.setExposedHeaders(List.of("Authorization"));
        corsConfiguration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
}
