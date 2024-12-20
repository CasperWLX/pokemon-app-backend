package com.hampus.projektuppgiftapi.model.user;

import java.util.List;

public class UpdateRequest {
    private List<String> guessedPokemon;

    public List<String> getGuessedPokemon() {
        return guessedPokemon;
    }

    public void setGuessedPokemon(List<String> guessedPokemon) {
        this.guessedPokemon = guessedPokemon;
    }
}
