package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.exceptions.user.LogInException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;


@Service
public class UserAuthenticationService implements IUserAuth {

    private final Logger LOGGER = LoggerFactory.getLogger(UserAuthenticationService.class);
    private final IUserRetrieval USER_RETRIEVAL;
    private final PasswordEncoder PASSWORD_ENCODER;

    public UserAuthenticationService(IUserRetrieval userRetrieval, PasswordEncoder passwordEncoder) {
        this.USER_RETRIEVAL = userRetrieval;
        this.PASSWORD_ENCODER = passwordEncoder;
    }

    public Mono<UsernamePasswordAuthenticationToken> authenticate(String username, String password) {
        LOGGER.info("Authenticating: {}", username);

        return USER_RETRIEVAL.getUser(username)
                .flatMap(user -> {
                    if (PASSWORD_ENCODER.matches(password, user.getPassword())) {
                        return Mono.just(new UsernamePasswordAuthenticationToken(
                                user.getUsername(), user.getPassword(), user.getAuthorities()));
                    } else {
                        return Mono.error(new LogInException(username));
                    }
                });
    }
}
