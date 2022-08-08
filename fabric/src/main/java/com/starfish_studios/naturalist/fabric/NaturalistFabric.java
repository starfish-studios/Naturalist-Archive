package com.starfish_studios.naturalist.fabric;

import com.google.common.base.Preconditions;
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
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.tag.BiomeTags;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;

import java.util.List;

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
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.RHINO.get(), Rhino.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.LION.get(), Lion.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ELEPHANT.get(), Elephant.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ZEBRA.get(), HorseBaseEntity.createBaseHorseAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.GIRAFFE.get(), Giraffe.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.HIPPO.get(), Hippo.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.VULTURE.get(), Vulture.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BOAR.get(), Boar.createAttributes());
    }

    void addSpawns() {
        NaturalistConfigFabric config = AutoConfig.getConfigHolder(NaturalistConfigFabric.class).getConfig();
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.TAIGA), SpawnGroup.CREATURE, NaturalistEntityTypes.BEAR.get(), config.bearSpawnWeight, 1, 2);
        addMobSpawn(List.of(Biome.Category.FOREST), SpawnGroup.CREATURE, NaturalistEntityTypes.DEER.get(), config.deerSpawnWeight, 1, 3);
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.SWAMP, Biome.Category.SAVANNA, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS, Biome.Category.MUSHROOM, Biome.Category.UNDERGROUND, Biome.Category.MOUNTAIN), SpawnGroup.CREATURE, NaturalistEntityTypes.SNAIL.get(), config.snailSpawnWeight, 1, 3);
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.SWAMP, Biome.Category.MUSHROOM), NaturalistMobCategories.getFireflyCategory(), NaturalistEntityTypes.FIREFLY.get(), config.fireflySpawnWeight, 2, 3);
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.PLAINS), SpawnGroup.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), config.butterflySpawnWeight, 1, 3);
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.SWAMP), SpawnGroup.CREATURE, NaturalistEntityTypes.SNAKE.get(), config.snakeSpawnWeight, 1, 1);
        addMobSpawn(List.of(Biome.Category.MESA, Biome.Category.SAVANNA, Biome.Category.DESERT), SpawnGroup.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), config.rattlesnakeSpawnWeight, 1, 1);
        addMobSpawn(List.of(Biome.Category.JUNGLE, Biome.Category.BEACH, Biome.Category.RIVER), SpawnGroup.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), config.coralSnakeSpawnWeight, 1, 1);
        addMobSpawn(List.of(Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS, Biome.Category.ICY), SpawnGroup.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), config.bluejaySpawnWeight, 1, 4);
        addMobSpawn(List.of(Biome.Category.EXTREME_HILLS, Biome.Category.MOUNTAIN), SpawnGroup.CREATURE, NaturalistEntityTypes.CANARY.get(), config.canarySpawnWeight, 1, 4);
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.SWAMP, Biome.Category.SAVANNA, Biome.Category.DESERT), SpawnGroup.CREATURE, NaturalistEntityTypes.CARDINAL.get(), config.cardinalSpawnWeight, 1, 4);
        addMobSpawn(List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.MOUNTAIN), SpawnGroup.CREATURE, NaturalistEntityTypes.ROBIN.get(), config.robinSpawnWeight, 1, 4);
        addMobSpawn(List.of(Biome.Category.FOREST), SpawnGroup.CREATURE, EntityType.RABBIT,  config.forestRabbitSpawnWeight, 2, 3);
        addMobSpawn(List.of(Biome.Category.FOREST), SpawnGroup.CREATURE, EntityType.FOX, config.forestFoxSpawnWeight, 2, 4);
        addMobSpawn(List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.RHINO.get(), config.rhinoSpawnWeight, 1, 3);
        addMobSpawn(List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.LION.get(), config.lionSpawnWeight, 3, 5);
        addMobSpawn(List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), config.elephantSpawnWeight, 2, 3);
        addMobSpawn(List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.ZEBRA.get(), config.zebraSpawnWeight, 2, 3);
        addMobSpawn(List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), config.giraffeSpawnWeight, 2, 4);
        addMobSpawn(List.of(Biome.Category.SAVANNA, Biome.Category.JUNGLE), SpawnGroup.CREATURE, NaturalistEntityTypes.HIPPO.get(), config.hippoSpawnWeight, 3, 4);
        addMobSpawn(List.of(Biome.Category.SAVANNA, Biome.Category.DESERT), SpawnGroup.CREATURE, NaturalistEntityTypes.VULTURE.get(), config.vultureSpawnWeight, 2, 4);
        addMobSpawn(List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.BOAR.get(), config.boarSpawnWeight, 4, 4);
        if (config.removeSavannaFarmAnimals) {
            removeSpawn(List.of(Biome.Category.SAVANNA), List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
    }

    void addMobSpawn(List<Biome.Category> categories, SpawnGroup mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(biomeSelector -> categories.contains(Biome.getCategory(biomeSelector.getBiomeRegistryEntry())) && biomeSelector.canGenerateIn(DimensionOptions.OVERWORLD), mobCategory, entityType, weight, minGroupSize, maxGroupSize);
    }

    void removeSpawn(List<Biome.Category> categories, List<EntityType<?>> entityTypes) {
        entityTypes.forEach(entityType -> {
            Identifier id = Registry.ENTITY_TYPE.getId(entityType);
            Preconditions.checkState(id != Registry.ENTITY_TYPE.getDefaultId(), "Unregistered entity type: %s", entityType);
            BiomeModifications.create(id).add(ModificationPhase.REMOVALS, biomeSelector -> categories.contains(Biome.getCategory(biomeSelector.getBiomeRegistryEntry())) && biomeSelector.canGenerateIn(DimensionOptions.OVERWORLD), context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
        });
    }
}
