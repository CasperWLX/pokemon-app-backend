package com.hampus.projektuppgiftapi.model.pokemon;

import java.util.List;

public class PokemonDTO {

    private int id;
    private String name;
    private int height;
    private int weight;
    private Sprites sprites;
    private List<Types> types;
    private int evolutionStage = 1;


    public int getId() {
        return id;
    }

    public PokemonDTO setId(int id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public PokemonDTO setName(String name) {
        this.name = name;
        return this;
    }

    public int getHeight() {
        return height;
    }

    public PokemonDTO setHeight(int height) {
        this.height = height;
        return this;
    }

    public int getWeight() {
        return weight;
    }

    public PokemonDTO setWeight(int weight) {
        this.weight = weight;
        return this;
    }

    public int getEvolutionStage() {
        return evolutionStage;
    }

    public PokemonDTO setEvolutionStage(int evolutionStage) {
        this.evolutionStage = evolutionStage;
        return this;
    }

    public Sprites getSprites() {
        return sprites;
    }

    public PokemonDTO setSprites(Sprites sprites) {
        this.sprites = sprites;
        return this;
    }

    public List<Types> getTypes() {
        return types;
    }

    public PokemonDTO setTypesList(List<Types> types) {
        this.types = types;
        return this;
    }

    public static class Sprites {
        private String front_default;

        public String getFront_default() {
            return front_default;
        }

        public Sprites setFront_default(String front_default) {
            this.front_default = front_default;
            return this;
        }
    }

    public static class Types {
        private Type type;

        public Type getType() {
            return type;
        }

        public Types setType(Type type) {
            this.type = type;
            return this;
        }

        public static class Type {
            private String name;

            public String getName() {
                return name;
            }

            public Type setName(String name) {
                this.name = name;
                return this;
            }
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
