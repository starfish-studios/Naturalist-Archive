package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = Naturalist.MOD_ID)
public class NaturalistConfig implements ConfigData {

    public int snailSpawnWeight = 4;
    public int snakeSpawnWeight = 4;
    public int coralSnakeSpawnWeight = 4;
    public int rattlesnakeSpawnWeight = 4;
    public int bearSpawnWeight = 6;
    public int deerSpawnWeight = 6;
    public int fireflySpawnWeight = 8;
    public int birdSpawnWeight = 10;
    public int butterflySpawnWeight = 4;
    public int forestRabbitSpawnWeight = 4;
    public int forestFoxSpawnWeight = 4;

}
