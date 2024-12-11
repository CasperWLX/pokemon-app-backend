package com.hampus.projektuppgiftapi.exceptions;

public class PokemonCreationException extends RuntimeException {
    public PokemonCreationException(String message) {
        super(message);
    }
}
