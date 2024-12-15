package com.hampus.projektuppgiftapi.model.pokemon;

import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "Pokemon")
public class Pokemon {

    @Id
    @NotBlank
    private String id;
    @NotBlank
    private int pokemonId;
    @NotBlank
    private String name;
    @NotBlank
    private String firstType;

    private String secondType;
    private int evolutionStage;
    @NotBlank
    private int height;
    @NotBlank
    private int weight;
    @NotBlank
    private String imgURL;

    public Pokemon() {}

    public int getPokemonId() {
        return pokemonId;
    }

    public Pokemon setPokemonId(int pokemonId){
        this.pokemonId = pokemonId;
        return this;
    }

    public String getName() {
        return name;
    }

    public Pokemon setName(String name) {
        this.name = name;
        return this;
    }

    public String getFirstType() {
        return firstType;
    }

    public Pokemon setFirstType(String firstType) {
        this.firstType = firstType;
        return this;
    }

    public String getSecondType() {
        return secondType;
    }

    public Pokemon setSecondType(String secondType) {
        this.secondType = secondType;
        return this;
    }

    public int getEvolutionStage() {
        return evolutionStage;
    }

    public Pokemon setEvolutionStage(int evolutionNumber) {
        this.evolutionStage = evolutionNumber;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public Pokemon setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public Pokemon setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public String getImgURL() {
        return imgURL;
    }

    public Pokemon setImgURL(String imgURL) {
        this.imgURL = imgURL;
        return this;
    }

    @Override
    public String toString() {
        return String.format("%s, types: %s and %s, with id: %d", name, firstType, secondType, pokemonId);
    }
}
