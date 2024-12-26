package com.hampus.projektuppgiftapi.controller;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.service.pokemon.IPokemonCreation;
import com.hampus.projektuppgiftapi.service.pokemon.IPokemonModification;
import com.hampus.projektuppgiftapi.service.pokemon.IPokemonRetrieval;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/database/v2")
@Tag(name = "Pokémon Database", description = "Endpoints for communicating with Database")
public class DatabaseController {

    private final IPokemonModification POKEMON_MODIFICATION;
    private final IPokemonRetrieval POKEMON_RETRIEVAL;
    private final IPokemonCreation POKEMON_CREATION;

    @Autowired
    public DatabaseController(IPokemonModification databaseService, IPokemonRetrieval pokemonRetrieval, IPokemonCreation pokemonCreation) {
        this.POKEMON_MODIFICATION = databaseService;
        this.POKEMON_RETRIEVAL = pokemonRetrieval;
        this.POKEMON_CREATION = pokemonCreation;
    }

    @GetMapping("/pokemon/{identifier}")
    @Operation(summary = "Get a Pokémon by identifier", description = "Fetch all info about one Pokémon by Name or ID")
    public Mono<ResponseEntity<Pokemon>> getPokemonFromDatabase(@PathVariable String identifier) {
        return POKEMON_RETRIEVAL.getPokemonByIdentifier(identifier).map(ResponseEntity::ok);
    }

    @Operation(summary = "Get all Pokémon", description = "Fetch a List of all Pokémon with corresponding info")
    @GetMapping("/pokemon/all")
    public Mono<ResponseEntity<List<Pokemon>>> getAllPokemonFromDatabase(){
        return POKEMON_RETRIEVAL.getAllPokemon().collectList().map(ResponseEntity::ok);
    }

    @PostMapping("/pokemon/add")
    @Operation(summary = "Add a Pokémon", description = "Post a new Pokémon to the Database")
    public Mono<ResponseEntity<Pokemon>> addOnePokemonToDatabase(@RequestBody PokemonDTO pokemonDTO){
        return POKEMON_CREATION.saveOnePokemonToDB(pokemonDTO).map(ResponseEntity::ok);
    }

    @DeleteMapping("/pokemon/{name}")
    @Operation(summary = "Delete a Pokémon", description = "Delete a Pokémon by name from Database")
    public Mono<ResponseEntity<Boolean>> deleteOnePokemonFromDatabase(@PathVariable String name) {
        return POKEMON_MODIFICATION.deleteByName(name).thenReturn(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/pokemon/all")
    @Operation(summary = "Delete all Pokémon", description = "Deletes")
    public Mono<ResponseEntity<Boolean>> deleteAllPokemonFromDatabase(){
        return POKEMON_MODIFICATION.deleteAll().thenReturn(ResponseEntity.noContent().build());
    }
}
