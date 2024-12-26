package com.hampus.projektuppgiftapi.controller;

import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.service.api.ApiService;
import com.hampus.projektuppgiftapi.service.api.IApiRetrieval;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/v2")
@Tag(name = "API Database", description = "Endpoints for communicating with API")
public class PokemonAPIController {

    private final IApiRetrieval API_RETRIEVAL;
    @Autowired
    public PokemonAPIController(IApiRetrieval apiRetrieval) {
        this.API_RETRIEVAL = apiRetrieval;
    }

    @GetMapping("/populate-database")
    @Operation(summary = "Fetch All Gen1 Pokémon", description = "Fetches the first 151 Pokémon and add to Database")
    public Mono<ResponseEntity<Boolean>> fetchAndSavePokemon() {
        return API_RETRIEVAL.fetchAndSaveFirst151Pokemon().map(ResponseEntity::ok);
    }

    @GetMapping("/pokemon/{id}")
    @Operation(summary = "Fetch a Pokémon", description = "Fetches any Pokémon from the API")
    public Mono<ResponseEntity<PokemonDTO>> fetchOnePokemon(@PathVariable int id, @RequestParam int evolutionStage) {
        return API_RETRIEVAL.fetchPokemonFromApiById(id)
                .map(pokemonDTO -> {
                    pokemonDTO.setEvolutionStage(evolutionStage);
                    return pokemonDTO;
                })
                .map(ResponseEntity::ok);
    }
}
