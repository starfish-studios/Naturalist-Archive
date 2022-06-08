package com.starfish_studios.naturalist.registry.forge;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;

public class NaturalistMobCategoriesImpl {
    public static MobCategory getFireflyCategory() {
        return MobCategory.create("fireflies", new ResourceLocation(Naturalist.MOD_ID, "fireflies").toString(), 5, true, false, 128);
    }
}
