package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;

import java.util.function.Supplier;

public class NaturalistPotions {
    public static final Supplier<Potion> FOREST_DASHER = CommonPlatformHelper.registerPotion("forest_dasher", () -> new Potion(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1), new MobEffectInstance(MobEffects.WEAKNESS, 400, 0)));
    public static final Supplier<Potion> LONG_FOREST_DASHER = CommonPlatformHelper.registerPotion("long_forest_dasher", () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 1), new MobEffectInstance(MobEffects.WEAKNESS, 800, 0)));
    public static final Supplier<Potion> STRONG_FOREST_DASHER = CommonPlatformHelper.registerPotion("strong_forest_dasher", () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 2), new MobEffectInstance(MobEffects.WEAKNESS, 400, 1)));

    public static void init() {}
}
