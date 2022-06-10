package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Naturalist.MOD_ID)
public class NaturalistConfig implements ConfigData {

    public int snailSpawnWeight = 5;
    public int snakeSpawnWeight = 4;
    public int coralSnakeSpawnWeight = 4;
    public int rattlesnakeSpawnWeight = 4;
    public int bearSpawnWeight = 8;
    public int deerSpawnWeight = 8;
    public int fireflySpawnWeight = 8;
    public int birdSpawnWeight = 8;
    public int butterflySpawnWeight = 6;
    public int forestRabbitSpawnWeight = 6;
    public int forestFoxSpawnWeight = 6;

}
