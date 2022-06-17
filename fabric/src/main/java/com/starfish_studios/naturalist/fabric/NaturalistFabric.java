package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistMobCategories;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import com.starfish_studios.naturalist.registry.fabric.NaturalistConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.world.biome.Biome;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(NaturalistConfigFabric.class, GsonConfigSerializer::new);
        Naturalist.init();
        addSpawns();
        registerEntityAttributes();
        Naturalist.registerBrewingRecipes();
        Naturalist.registerCompostables();
        Naturalist.registerSpawnPlacements();
    }

    void registerEntityAttributes() {
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.SNAIL.get(), Snail.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BEAR.get(), Bear.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BUTTERFLY.get(), Butterfly.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.FIREFLY.get(), Firefly.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.SNAKE.get(), Snake.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CORAL_SNAKE.get(), Snake.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.RATTLESNAKE.get(), Snake.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.DEER.get(), Deer.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BLUEJAY.get(), Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CANARY.get(), Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CARDINAL.get(), Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ROBIN.get(), Bird.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CATERPILLAR.get(), Caterpillar.createAttributes());
    }

    void addSpawns() {
        NaturalistConfigFabric config = AutoConfig.getConfigHolder(NaturalistConfigFabric.class).getConfig();
        addMobSpawn(NaturalistTags.Biomes.HAS_BEAR, SpawnGroup.CREATURE, NaturalistEntityTypes.BEAR.get(), config.bearSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_DEER, SpawnGroup.CREATURE, NaturalistEntityTypes.DEER.get(), config.deerSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAIL, SpawnGroup.CREATURE, NaturalistEntityTypes.SNAIL.get(), config.snailSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_FIREFLY, NaturalistMobCategories.getFireflyCategory(), NaturalistEntityTypes.FIREFLY.get(), config.fireflySpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_BUTTERFLY, SpawnGroup.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), config.butterflySpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAKE, SpawnGroup.CREATURE, NaturalistEntityTypes.SNAKE.get(), config.snakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_RATTLESNAKE, SpawnGroup.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), config.rattlesnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_CORAL_SNAKE, SpawnGroup.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), config.coralSnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_BLUEJAY, SpawnGroup.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), config.bluejaySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CANARY, SpawnGroup.CREATURE, NaturalistEntityTypes.CANARY.get(), config.canarySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CARDINAL, SpawnGroup.CREATURE, NaturalistEntityTypes.CARDINAL.get(), config.cardinalSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_ROBIN, SpawnGroup.CREATURE, NaturalistEntityTypes.ROBIN.get(), config.robinSpawnWeight, 1, 4);
        addMobSpawn(BiomeTags.IS_FOREST, SpawnGroup.CREATURE, EntityType.RABBIT,  config.forestRabbitSpawnWeight, 2, 3);
        addMobSpawn(BiomeTags.IS_FOREST, SpawnGroup.CREATURE, EntityType.FOX, config.forestFoxSpawnWeight, 2, 4);
    }

    void addMobSpawn(TagKey<Biome> tag, SpawnGroup mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(biomeSelector -> biomeSelector.hasTag(tag), mobCategory, entityType, weight, minGroupSize, maxGroupSize);
    }
}
