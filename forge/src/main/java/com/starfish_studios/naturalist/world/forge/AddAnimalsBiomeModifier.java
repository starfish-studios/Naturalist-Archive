package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import com.starfish_studios.naturalist.registry.forge.NaturalistBiomeModifiers;
import com.starfish_studios.naturalist.registry.forge.NaturalistConfigForge;
import com.starfish_studios.naturalist.registry.forge.NaturalistMobCategoriesImpl;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public class AddAnimalsBiomeModifier implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase.equals(Phase.ADD)) {
            if (biome.is(NaturalistTags.Biomes.HAS_SNAIL)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), NaturalistConfigForge.SNAIL_SPAWN_WEIGHT.get(), 2, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_SNAKE)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAKE.get(), NaturalistConfigForge.SNAKE_SPAWN_WEIGHT.get(), 1, 1));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_CORAL_SNAKE)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfigForge.CORAL_SNAKE_SPAWN_WEIGHT.get(), 1, 1));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_RATTLESNAKE)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RATTLESNAKE.get(), NaturalistConfigForge.RATTLESNAKE_SPAWN_WEIGHT.get(), 1, 1));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_BEAR)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BEAR.get(), NaturalistConfigForge.BEAR_SPAWN_WEIGHT.get(), 1, 2));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_DEER)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.DEER.get(), NaturalistConfigForge.DEER_SPAWN_WEIGHT.get(), 1, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_FIREFLY)) {
                builder.getMobSpawnSettings().addSpawn(NaturalistMobCategoriesImpl.getFireflyCategory(), new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), NaturalistConfigForge.FIREFLY_SPAWN_WEIGHT.get(), 2, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_BLUEJAY)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfigForge.BLUEJAY_SPAWN_WEIGHT.get(), 1, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_CANARY)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CANARY.get(), NaturalistConfigForge.CANARY_SPAWN_WEIGHT.get(), 1, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_CARDINAL)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CARDINAL.get(), NaturalistConfigForge.CARDINAL_SPAWN_WEIGHT.get(), 1, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_ROBIN)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.ROBIN.get(), NaturalistConfigForge.ROBIN_SPAWN_WEIGHT.get(), 1, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_BUTTERFLY)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfigForge.BUTTERFLY_SPAWN_WEIGHT.get(), 1, 3));
            }
            if (biome.is(BiomeTags.IS_FOREST)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.FOX, NaturalistConfigForge.FOREST_FOX_SPAWN_WEIGHT.get(), 2, 4));
            }
            if (biome.is(BiomeTags.IS_FOREST)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(EntityType.RABBIT, NaturalistConfigForge.FOREST_RABBIT_SPAWN_WEIGHT.get(), 2, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_RHINO)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RHINO.get(), NaturalistConfigForge.RHINO_SPAWN_WEIGHT.get(), 1, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_LION)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.LION.get(), NaturalistConfigForge.LION_SPAWN_WEIGHT.get(), 3, 5));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_ELEPHANT)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.ELEPHANT.get(), NaturalistConfigForge.ELEPHANT_SPAWN_WEIGHT.get(), 2, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_ZEBRA)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.ZEBRA.get(), NaturalistConfigForge.ZEBRA_SPAWN_WEIGHT.get(), 2, 3));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_GIRAFFE)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.GIRAFFE.get(), NaturalistConfigForge.GIRAFFE_SPAWN_WEIGHT.get(), 2, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_HIPPO)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.HIPPO.get(), NaturalistConfigForge.HIPPO_SPAWN_WEIGHT.get(), 3, 4));
            }
            if (biome.is(NaturalistTags.Biomes.HAS_VULTURE)) {
                builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.VULTURE.get(), NaturalistConfigForge.VULTURE_SPAWN_WEIGHT.get(), 2, 4));
            }
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
