package com.hampus.projektuppgiftapi.util.pokemon;

import com.hampus.projektuppgiftapi.model.pokemon.Pokemon;
import com.hampus.projektuppgiftapi.model.pokemon.PokemonDTO;
import com.hampus.projektuppgiftapi.repo.IPokemonRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TestPokemonUtil {

    @Mock
    private IPokemonRepository pokemonRepository;

    @InjectMocks
    private PokemonUtil POKEMON_UTIL;

    private PokemonDTO pokemonDTO;

    @BeforeEach()
    void setUp() {
        pokemonDTO = new PokemonDTO();
        pokemonDTO.setName("Pikachu")
                .setId(1)
                .setHeight(1)
                .setWeight(3)
                .setEvolutionStage(1)
                .setSprites(new PokemonDTO.Sprites()
                        .setFront_default("Test"))
                .setTypesList(List.of(
                        new PokemonDTO.Types()
                                .setType(new PokemonDTO.Types.Type().setName("Electric")),
                        new PokemonDTO.Types()
                                .setType(new PokemonDTO.Types.Type())));
    }

    @Test
    @DisplayName("Test: If database is empty or populated ")
    public void testIsDatabasePopulated() {
        Mockito.when(pokemonRepository.count()).thenReturn(Mono.just(1L));
        Mono<Boolean> result = POKEMON_UTIL.isDatabaseEmpty();

        StepVerifier.create(result)
                .consumeNextWith(value -> assertFalse(value, "The result should be false"))
                .verifyComplete();

        Mockito.when(pokemonRepository.count()).thenReturn(Mono.just(0L));
        result = POKEMON_UTIL.isDatabaseEmpty();

        StepVerifier.create(result)
                .consumeNextWith(value -> assertTrue(value, "The result should be true"))
                .verifyComplete();
    }

    @Test
    @DisplayName("Test: Successfully convert PokemonDTO to Pokemon model")
    public void testConvertPokemonDTOToPokemonModelSuccessfully() {
        Pokemon convertedToPokemonModel = POKEMON_UTIL.convertToPokemonModel(pokemonDTO);

        // Assert
        assertNotNull(convertedToPokemonModel, "The result should not be null");
        assertEquals(pokemonDTO.getId(), convertedToPokemonModel.getPokemonId(), "IDs should match");
        assertEquals(pokemonDTO.getName(), convertedToPokemonModel.getName(), "Names should match");
        assertEquals(pokemonDTO.getHeight(), convertedToPokemonModel.getHeight(), "Heights should match");
        assertEquals(pokemonDTO.getWeight(), convertedToPokemonModel.getWeight(), "Weights should match");
        assertEquals(pokemonDTO.getEvolutionStage(), convertedToPokemonModel.getEvolutionStage(), "Evolution stages should match");
        assertEquals(pokemonDTO.getSprites().getFront_default(), convertedToPokemonModel.getImgURL(), "Sprite URLs should match");
        assertEquals(pokemonDTO.getTypes().getFirst().getType().getName(), convertedToPokemonModel.getFirstType(), "First type should match");
        assertEquals(pokemonDTO.getTypes().getLast().getType().getName(), convertedToPokemonModel.getSecondType(), "Second type should be None");
    }

    @Test
    @DisplayName("Test: Correct boolean is returned based on user input")
    public void testIsInteger() {

        boolean result = POKEMON_UTIL.isInteger("1");
        assertTrue(result, "The result should be true");

        result = POKEMON_UTIL.isInteger("Pikachu");
        assertFalse(result, "The result should be false");

        result = POKEMON_UTIL.isInteger("%2e53tsat");
        assertFalse(result, "The result should be false");
    }
}
