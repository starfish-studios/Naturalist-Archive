package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import java.util.function.Supplier;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.potion.Potion;

public class NaturalistPotions {
    public static final Supplier<Potion> FOREST_DASHER = CommonPlatformHelper.registerPotion("forest_dasher", () -> new Potion(new StatusEffectInstance(StatusEffects.SPEED, 400, 1), new StatusEffectInstance(StatusEffects.WEAKNESS, 400, 0)));
    public static final Supplier<Potion> LONG_FOREST_DASHER = CommonPlatformHelper.registerPotion("long_forest_dasher", () -> new Potion("forest_dasher", new StatusEffectInstance(StatusEffects.SPEED, 800, 1), new StatusEffectInstance(StatusEffects.WEAKNESS, 800, 0)));
    public static final Supplier<Potion> STRONG_FOREST_DASHER = CommonPlatformHelper.registerPotion("strong_forest_dasher", () -> new Potion("forest_dasher", new StatusEffectInstance(StatusEffects.SPEED, 400, 2), new StatusEffectInstance(StatusEffects.WEAKNESS, 400, 1)));
    public static final Supplier<Potion> GLOWING = CommonPlatformHelper.registerPotion("glowing", () -> new Potion(new StatusEffectInstance(StatusEffects.GLOWING, 400)));
    public static final Supplier<Potion> LONG_GLOWING = CommonPlatformHelper.registerPotion("long_glowing", () -> new Potion("glowing", new StatusEffectInstance(StatusEffects.GLOWING, 800)));

    public static void init() {}
}
