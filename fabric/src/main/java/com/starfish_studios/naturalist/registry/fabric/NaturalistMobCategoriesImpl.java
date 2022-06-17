package com.starfish_studios.naturalist.registry.fabric;

import net.minecraft.entity.SpawnGroup;

public class NaturalistMobCategoriesImpl {
    static {
        SpawnGroup.values();
    }
    public static SpawnGroup FIREFLIES;

    public static SpawnGroup getFireflyCategory() {
        return FIREFLIES;
    }
}
