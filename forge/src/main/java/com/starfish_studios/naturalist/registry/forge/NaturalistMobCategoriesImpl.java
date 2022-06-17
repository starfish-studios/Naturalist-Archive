package com.starfish_studios.naturalist.registry.forge;

import net.minecraft.world.entity.MobCategory;

public class NaturalistMobCategoriesImpl {
    public static MobCategory getFireflyCategory() {
        return MobCategory.create("fireflies", "fireflies", 10, true, false, 128);
    }
}
