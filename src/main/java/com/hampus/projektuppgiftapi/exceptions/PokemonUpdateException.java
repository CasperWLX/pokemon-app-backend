package com.hampus.projektuppgiftapi.exceptions;

public class PokemonUpdateException extends RuntimeException{
    public PokemonUpdateException(String message) {
        super(message);
    }
}
