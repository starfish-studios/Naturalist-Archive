package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.Naturalist;
import net.fabricmc.api.ModInitializer;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Naturalist.init();
        Naturalist.registerBrewingRecipes();
        Naturalist.registerSpawnPlacements();
    }
}
