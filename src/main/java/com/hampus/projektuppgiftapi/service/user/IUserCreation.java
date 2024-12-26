package com.hampus.projektuppgiftapi.service.user;

import com.hampus.projektuppgiftapi.model.user.AuthRequest;
import com.hampus.projektuppgiftapi.model.user.CustomUser;
import reactor.core.publisher.Mono;

public interface IUserCreation {
    Mono<Void> saveUserToDB(AuthRequest authRequest);
    Mono<Boolean> usernameIsAlreadyTaken(String username);
    Mono<CustomUser> convertUserToDBUser(AuthRequest authRequest);
}
