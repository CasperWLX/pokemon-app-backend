package com.hampus.projektuppgiftapi.controller;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.service.PokemonService;
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

    private final PokemonService DATABASE_SERVICE;

    @Autowired
    public DatabaseController(PokemonService databaseService) {
        this.DATABASE_SERVICE = databaseService;
    }

    @GetMapping("/pokemon/{identifier}")
    @Operation(summary = "Get a Pokémon by identifier", description = "Fetch all info about one Pokémon by Name or ID")
    public Mono<ResponseEntity<Pokemon>> getPokemonFromDatabase(@PathVariable String identifier) {
        return DATABASE_SERVICE.getPokemonByIdentifier(identifier).map(ResponseEntity::ok);
    }

    @Operation(summary = "Get all Pokémon", description = "Fetch a List of all Pokémon with corresponding info")
    @GetMapping("/pokemon/all")
    public Mono<ResponseEntity<List<Pokemon>>> getAllPokemonFromDatabase(){
        return DATABASE_SERVICE.getAllPokemon().collectList().map(ResponseEntity::ok);
    }

    @PostMapping("/pokemon/add")
    @Operation(summary = "Add a Pokémon", description = "Post a new Pokémon to the Database")
    public Mono<ResponseEntity<Pokemon>> addOnePokemonToDatabase(@RequestBody PokemonDTO pokemonDTO){
        return DATABASE_SERVICE.saveOnePokemonToDB(pokemonDTO).map(ResponseEntity::ok);
    }

    @DeleteMapping("/pokemon/{name}")
    @Operation(summary = "Delete a Pokémon", description = "Delete a Pokémon by name from Database")
    public Mono<ResponseEntity<Boolean>> deleteOnePokemonFromDatabase(@PathVariable String name) {
        return DATABASE_SERVICE.deletePokemonByName(name).thenReturn(ResponseEntity.noContent().build());
    }

    @DeleteMapping("/pokemon/all")
    @Operation(summary = "Delete all Pokémon", description = "Deletes")
    public Mono<ResponseEntity<Boolean>> deleteAllPokemonFromDatabase(){
        return DATABASE_SERVICE.deleteAllPokemon().thenReturn(ResponseEntity.noContent().build());
    }
}
