package com.starfish_studios.naturalist.registry.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.world.forge.*;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistBiomeModifierSerializers {
    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Naturalist.MOD_ID);

    public static RegistryObject<Codec<BearBiomeModifier>> BEAR_CODEC = BIOME_MODIFIER_SERIALIZERS.register("bear", () -> RecordCodecBuilder.create(builder -> builder.group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(BearBiomeModifier::biomes)).apply(builder, BearBiomeModifier::new)));
}
