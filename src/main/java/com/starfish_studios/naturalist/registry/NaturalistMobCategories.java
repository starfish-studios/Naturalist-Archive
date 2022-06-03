package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.MobCategory;

public class NaturalistMobCategories {
    public static final MobCategory FIREFLIES = MobCategory.create("fireflies", new ResourceLocation(Naturalist.MOD_ID, "fireflies").toString(), 5, true, false, 128);
}
