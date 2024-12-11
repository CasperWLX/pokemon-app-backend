package com.hampus.projektuppgiftapi.components;

import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import org.springframework.stereotype.Component;

@Component
public class EvolutionTracker {
    private PokemonDTO previousPokemonDTO;
    private int previousEvolutionStage;

    public EvolutionTracker() {
        this.previousEvolutionStage = 1;
    }

    // Check evolution stage based on the comparison logic
    public int calculateEvolutionStage(PokemonDTO currentPokemon) {
        if (previousPokemonDTO == null) {
            //if no previous Pokémon
            previousPokemonDTO = currentPokemon;
            return previousEvolutionStage;
        }

        if (previousEvolutionStage == 3){
            previousEvolutionStage = 0;
        }

        if (currentPokemon.getTypes().getFirst().getType().getName().equals(previousPokemonDTO.getTypes().getFirst().getType().getName())){
            previousEvolutionStage++;
        } else {
            previousEvolutionStage = 1;
        }

        //Specific cases that are difficult to handle with pure logic
        switch(currentPokemon.getId()){
            case 21,48,84,107,118,120,131,133,140,142,151 -> previousEvolutionStage = 1;
            case 134,135,136 -> previousEvolutionStage = 2;
        }

        previousPokemonDTO = currentPokemon;
        return previousEvolutionStage;
    }
}
