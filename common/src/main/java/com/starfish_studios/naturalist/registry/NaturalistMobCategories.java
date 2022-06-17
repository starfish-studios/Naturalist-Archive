package com.starfish_studios.naturalist.registry;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.entity.SpawnGroup;

public class NaturalistMobCategories {
    @ExpectPlatform
    public static SpawnGroup getFireflyCategory() {
        throw new AssertionError();
    }
}
