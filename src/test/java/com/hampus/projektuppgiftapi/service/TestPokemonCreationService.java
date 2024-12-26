package com.hampus.projektuppgiftapi.service;

import com.hampus.projektuppgiftapi.exceptions.pokemon.PokemonNotFoundException;
import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import com.hampus.projektuppgiftapi.service.pokemon.PokemonRetrievalService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

@ExtendWith(MockitoExtension.class)
public class TestPokemonCreationService {

    @Mock
    private IPokemonRepository POKEMON_REPO;

    @InjectMocks
    private PokemonRetrievalService POKEMON_RETRIEVAL;

    @Test
    @DisplayName("Test: Incorrect fetch of pokemon by ID or Username")
    public void testFetchPokemonByIdOrUsername() {
        Mockito.when(POKEMON_REPO.findByPokemonId(Mockito.any(Integer.class))).thenReturn(Mono.empty());
        Mono<Pokemon> result = POKEMON_RETRIEVAL.getPokemonById(1);
        StepVerifier.create(result).expectError(PokemonNotFoundException.class).verify();

        Mockito.when(POKEMON_REPO.findByName(Mockito.any(String.class))).thenReturn(Mono.empty());
        result = POKEMON_RETRIEVAL.getPokemonByName("Pokemon");
        StepVerifier.create(result).expectError(PokemonNotFoundException.class).verify();
    }
}
