package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistTags;
import com.starfish_studios.naturalist.registry.forge.NaturalistBiomeModifiers;
import com.starfish_studios.naturalist.registry.forge.NaturalistConfigForge;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
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
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), NaturalistConfigForge.SNAIL_SPAWN_WEIGHT.get(), 2, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), NaturalistConfigForge.SNAKE_SPAWN_WEIGHT.get(), 1, 1);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfigForge.CORAL_SNAKE_SPAWN_WEIGHT.get(), 1, 1);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_RATTLESNAKE, MobCategory.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), NaturalistConfigForge.RATTLESNAKE_SPAWN_WEIGHT.get(), 1, 1);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), NaturalistConfigForge.BEAR_SPAWN_WEIGHT.get(), 1, 2);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), NaturalistConfigForge.DEER_SPAWN_WEIGHT.get(), 1, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_FIREFLY, MobCategory.AMBIENT, NaturalistEntityTypes.FIREFLY.get(), NaturalistConfigForge.FIREFLY_SPAWN_WEIGHT.get(), 2, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfigForge.BLUEJAY_SPAWN_WEIGHT.get(), 1, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.CANARY.get(), NaturalistConfigForge.CANARY_SPAWN_WEIGHT.get(), 1, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL.get(), NaturalistConfigForge.CARDINAL_SPAWN_WEIGHT.get(), 1, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.ROBIN.get(), NaturalistConfigForge.ROBIN_SPAWN_WEIGHT.get(), 1, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BUTTERFLY, MobCategory.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfigForge.BUTTERFLY_SPAWN_WEIGHT.get(), 1, 3);
            addMobSpawn(builder, biome, BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.FOX, NaturalistConfigForge.FOREST_FOX_SPAWN_WEIGHT.get(), 2, 4);
            addMobSpawn(builder, biome, BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.RABBIT, NaturalistConfigForge.FOREST_RABBIT_SPAWN_WEIGHT.get(), 2, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_RHINO, MobCategory.CREATURE, NaturalistEntityTypes.RHINO.get(), NaturalistConfigForge.RHINO_SPAWN_WEIGHT.get(), 1, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_LION, MobCategory.CREATURE, NaturalistEntityTypes.LION.get(), NaturalistConfigForge.LION_SPAWN_WEIGHT.get(), 3, 5);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ELEPHANT, MobCategory.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), NaturalistConfigForge.ELEPHANT_SPAWN_WEIGHT.get(), 2, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ZEBRA, MobCategory.CREATURE, NaturalistEntityTypes.ZEBRA.get(), NaturalistConfigForge.ZEBRA_SPAWN_WEIGHT.get(), 2, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_GIRAFFE, MobCategory.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), NaturalistConfigForge.GIRAFFE_SPAWN_WEIGHT.get(), 1, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_HIPPO, MobCategory.CREATURE, NaturalistEntityTypes.HIPPO.get(), NaturalistConfigForge.HIPPO_SPAWN_WEIGHT.get(), 3, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_VULTURE, MobCategory.CREATURE, NaturalistEntityTypes.VULTURE.get(), NaturalistConfigForge.VULTURE_SPAWN_WEIGHT.get(), 2, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_DRAGONFLY, MobCategory.AMBIENT, NaturalistEntityTypes.DRAGONFLY.get(), NaturalistConfigForge.DRAGONFLY_SPAWN_WEIGHT.get(), 2, 3);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_CATFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CATFISH.get(), NaturalistConfigForge.CATFISH_SPAWN_WEIGHT.get(), 1, 1);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_ALLIGATOR, MobCategory.CREATURE, NaturalistEntityTypes.ALLIGATOR.get(), NaturalistConfigForge.ALLIGATOR_SPAWN_WEIGHT.get(), 1, 2);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_BASS, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BASS.get(), NaturalistConfigForge.BASS_SPAWN_WEIGHT.get(), 2, 4);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_LIZARD, MobCategory.CREATURE, NaturalistEntityTypes.LIZARD.get(), NaturalistConfigForge.LIZARD_SPAWN_WEIGHT.get(), 1, 2);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_TORTOISE, MobCategory.CREATURE, NaturalistEntityTypes.TORTOISE.get(), NaturalistConfigForge.TORTOISE_SPAWN_WEIGHT.get(), 1, 2);
            addMobSpawn(builder, biome, NaturalistTags.Biomes.HAS_DUCK, MobCategory.CREATURE, NaturalistEntityTypes.DUCK.get(), NaturalistConfigForge.DUCK_SPAWN_WEIGHT.get(), 2, 4);
        }
    }

    void addMobSpawn(ModifiableBiomeInfo.BiomeInfo.Builder builder, Holder<Biome> biome, TagKey<Biome> tag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (biome.is(tag)) {
            builder.getMobSpawnSettings().addSpawn(mobCategory, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return NaturalistBiomeModifiers.ADD_ANIMALS_CODEC.get();
    }
}
