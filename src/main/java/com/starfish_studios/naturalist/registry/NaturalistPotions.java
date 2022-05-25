package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Naturalist.MOD_ID);

    public static final RegistryObject<Potion> FOREST_DASHER = POTIONS.register("forest_dasher", () -> new Potion(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 1), new MobEffectInstance(MobEffects.WEAKNESS, 400, 0)));
    public static final RegistryObject<Potion> LONG_FOREST_DASHER = POTIONS.register("long_forest_dasher", () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 800, 1), new MobEffectInstance(MobEffects.WEAKNESS, 800, 0)));
    public static final RegistryObject<Potion> STRONG_FOREST_DASHER = POTIONS.register("strong_forest_dasher", () -> new Potion("forest_dasher", new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 400, 2), new MobEffectInstance(MobEffects.WEAKNESS, 400, 1)));
    public static final RegistryObject<Potion> GLOWING = POTIONS.register("glowing", () -> new Potion(new MobEffectInstance(MobEffects.GLOWING, 400)));
    public static final RegistryObject<Potion> LONG_GLOWING = POTIONS.register("long_glowing", () -> new Potion("glowing", new MobEffectInstance(MobEffects.GLOWING, 800)));
}
