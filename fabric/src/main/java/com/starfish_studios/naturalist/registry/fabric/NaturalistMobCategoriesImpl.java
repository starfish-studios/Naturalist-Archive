package com.starfish_studios.naturalist.registry.fabric;

import net.minecraft.world.entity.MobCategory;

public class NaturalistMobCategoriesImpl {
    static {
        MobCategory.values();
    }
    public static MobCategory FIREFLIES;

    public static MobCategory getFireflyCategory() {
        return FIREFLIES;
    }
}
