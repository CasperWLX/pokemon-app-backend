package com.hampus.projektuppgiftapi.exceptions;

import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonCreationException;
import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonUpdateException;
import com.hampus.projektuppgiftapi.exceptions.user.LogInException;
import com.hampus.projektuppgiftapi.exceptions.user.UserAlreadyExistsException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(PokemonCreationException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePokemonCreationException(PokemonCreationException e) {
        logger.error("Pokemon creation failed: {}", e.getMessage()); // Log the error with exception details
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PokemonNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handlePokemonNotFoundException(PokemonNotFoundException e) {
        logger.error("Pokemon not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(PokemonUpdateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handlePokemonUpdateException(PokemonUpdateException e) {
        logger.error("Pokemon update failed: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(UserNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ResponseEntity<String> handleUsernameNotFound(UserNotFoundException e) {
        logger.error("User not found: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<String> handleUserAlreadyExists(UserAlreadyExistsException e) {
        logger.error("User already exists: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }

    @ExceptionHandler(LogInException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<String> handleInvalidLogin(LogInException e) {
        logger.error("Invalid credentials for user: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    }

    public static class UserNotFoundException extends UsernameNotFoundException {
        public UserNotFoundException(String message){
            super(message);
        }
    }
}
