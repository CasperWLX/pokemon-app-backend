package com.hampus.projektuppgiftapi.exceptions.pokemon;

public class PokemonNotFoundException extends RuntimeException{
    public PokemonNotFoundException(String message) {
        super(message);
    }
}
