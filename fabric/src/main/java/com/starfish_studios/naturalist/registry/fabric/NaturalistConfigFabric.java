package com.starfish_studios.naturalist.registry.fabric;

import com.starfish_studios.naturalist.Naturalist;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = Naturalist.MOD_ID)
public class NaturalistConfigFabric implements ConfigData {

    // MISC ANIMALS

    public int fireflySpawnWeight = 500;
    public int bluejaySpawnWeight = 8;
    public int canarySpawnWeight = 8;
    public int cardinalSpawnWeight = 8;
    public int robinSpawnWeight = 8;
    public int butterflySpawnWeight = 6;
    public int snailSpawnWeight = 5;
    public int snakeSpawnWeight = 4;


    // FOREST ANIMALS

    public int coralSnakeSpawnWeight = 4;
    public int rattlesnakeSpawnWeight = 4;
    public int bearSpawnWeight = 8;
    public int deerSpawnWeight = 8;


    // DRYLAND ANIMALS

    public int rhinoSpawnWeight = 15;
    public int lionSpawnWeight = 8;
    public int elephantSpawnWeight = 10;
    public int zebraSpawnWeight = 10;
    public int giraffeSpawnWeight = 10;
    public int hippoSpawnWeight = 15;
    public int vultureSpawnWeight = 15;
    public int boarSpawnWeight = 15;


    // SWAMPLAND ANIMALS

    public int dragonflySpawnWeight = 8;
    public int catfishSpawnWeight = 5;
    public int alligatorSpawnWeight = 15;
    public int bassSpawnWeight = 8;
    public int lizardSpawnWeight = 15;
    public int tortoiseSpawnWeight = 6;
    public int duckSpawnWeight = 15;


    // ADD VANILLA ANIMALS

    public int forestRabbitSpawnWeight = 6;
    public int forestFoxSpawnWeight = 6;


    // REMOVE VANILLA ANIMALS

    public boolean removeSavannaFarmAnimals = true;
    public boolean removeSwampFarmAnimals = true;
}
