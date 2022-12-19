package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import com.starfish_studios.naturalist.registry.forge.NaturalistBiomeModifiers;
import com.starfish_studios.naturalist.registry.forge.NaturalistConfigForge;
import net.minecraft.core.Holder;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class AddAnimalsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase.equals(Phase.ADD)) {
            if (biome.is(NaturalistTags.Biomes.HAS_DRAGONFLY)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.DRAGONFLY.get(), NaturalistConfigForge.DRAGONFLY_SPAWN_WEIGHT.get(), 2, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_CATFISH)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CATFISH.get(), NaturalistConfigForge.CATFISH_SPAWN_WEIGHT.get(), 1, 1));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_ALLIGATOR)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.ALLIGATOR.get(), NaturalistConfigForge.ALLIGATOR_SPAWN_WEIGHT.get(), 1, 2));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_BASS)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.WATER_AMBIENT, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BASS.get(), NaturalistConfigForge.BASS_SPAWN_WEIGHT.get(), 4, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_LIZARD)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.LIZARD.get(), NaturalistConfigForge.LIZARD_SPAWN_WEIGHT.get(), 1, 2));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_TORTOISE)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.TORTOISE.get(), NaturalistConfigForge.TORTOISE_SPAWN_WEIGHT.get(), 1, 3));
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return NaturalistBiomeModifiers.ADD_ANIMALS_CODEC.get();
    }
}
