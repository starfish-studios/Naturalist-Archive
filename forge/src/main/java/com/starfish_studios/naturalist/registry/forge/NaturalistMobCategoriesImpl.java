package com.starfish_studios.naturalist.registry.forge;

import net.minecraft.entity.SpawnGroup;

public class NaturalistMobCategoriesImpl {
    public static SpawnGroup getFireflyCategory() {
        return SpawnGroup.create("fireflies", "fireflies", 10, true, false, 128);
    }
}
