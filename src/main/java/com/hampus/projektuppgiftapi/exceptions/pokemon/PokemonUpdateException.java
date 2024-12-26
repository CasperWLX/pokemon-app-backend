package com.hampus.projektuppgiftapi.exceptions.pokemon;

public class PokemonUpdateException extends RuntimeException{
    public PokemonUpdateException(String message) {
        super(message);
    }
}
