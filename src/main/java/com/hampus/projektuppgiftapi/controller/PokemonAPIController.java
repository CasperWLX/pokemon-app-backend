package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.service.ApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2")
public class PokemonAPIController {

    private final ApiService API_SERVICE;
    @Autowired
    public PokemonAPIController(ApiService API_SERVICE) {
        this.API_SERVICE = API_SERVICE;
    }

    @GetMapping("/populate-database")
    public Mono<ResponseEntity<Boolean>> fetchAndSavePokemon() {
        return API_SERVICE.fetchAndSaveFirst151Pokemon().map(ResponseEntity::ok);
    }

    @GetMapping("/pokemon/{id}")
    public Mono<ResponseEntity<PokemonDTO>> fetchOnePokemon(@PathVariable int id, @RequestParam int evolutionStage) {
        return API_SERVICE.fetchPokemon(id)
                .map(pokemonDTO -> {
                    pokemonDTO.setEvolutionStage(evolutionStage);
                    return pokemonDTO;
                })
                .map(ResponseEntity::ok);
    }
}
