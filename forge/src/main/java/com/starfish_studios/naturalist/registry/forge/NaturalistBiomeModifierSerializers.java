package com.starfish_studios.naturalist.registry.forge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.world.forge.*;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistBiomeModifierSerializers {
    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS = DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Naturalist.MOD_ID);

    public static final RegistryObject<Codec<MobSpawnsBiomeModifier>> MOB_SPAWNS = BIOME_MODIFIER_SERIALIZERS.register("mob_spawns", () -> RecordCodecBuilder.create(builder -> builder
            .group(Biome.LIST_CODEC.fieldOf("biomes").forGetter(MobSpawnsBiomeModifier::biomes),
                    MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawn").forGetter(MobSpawnsBiomeModifier::spawn),
                    MobCategory.CODEC.fieldOf("category").forGetter(MobSpawnsBiomeModifier::category))
            .apply(builder, MobSpawnsBiomeModifier::new)));
}
