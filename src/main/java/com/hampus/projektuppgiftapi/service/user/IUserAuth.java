package com.hampus.projektuppgiftapi.service.user;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import reactor.core.publisher.Mono;

public interface IUserAuth {
    Mono<UsernamePasswordAuthenticationToken> authenticate(String username, String password);
}
