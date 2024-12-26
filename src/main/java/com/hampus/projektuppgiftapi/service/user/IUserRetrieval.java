package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.model.user.CustomUser;
import reactor.core.publisher.Mono;

public interface IUserRetrieval {
    Mono<CustomUser> getUser(String username);
}
