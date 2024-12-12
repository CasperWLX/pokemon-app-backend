package com.hampus.projektuppgiftapi.filter;

import com.hampus.projektuppgiftapi.model.user.UserRoles;
import com.hampus.projektuppgiftapi.util.JwtUtil;
import org.springframework.http.HttpCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import java.util.Optional;


public class JwtAuthenticationFilter implements WebFilter {

    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(JwtUtil jwtUtil) {
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String path = exchange.getRequest().getURI().getPath();
        if(path.equals("/user/v1/login") || path.equals("/user/v1/register")){
            return chain.filter(exchange);
        }

        Optional<String> jwtOpt = Optional.ofNullable(exchange.getRequest().getCookies().getFirst("jwtToken"))
                .map(HttpCookie::getValue);

        if (jwtOpt.isEmpty()) {
            return chain.filter(exchange);
        }

        String jwt = jwtOpt.get();
        if (jwtUtil.validateToken(jwt)) {
            String username = jwtUtil.getUsername(jwt);
            UserRoles roles = jwtUtil.extractRoles(jwt);


            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(username, null, roles.getGrantedAuthorities());

            SecurityContext securityContext = new SecurityContextImpl(authentication);

            return chain.filter(exchange).contextWrite(ReactiveSecurityContextHolder.withSecurityContext(Mono.just(securityContext)));
        }
        return chain.filter(exchange);
    }
}