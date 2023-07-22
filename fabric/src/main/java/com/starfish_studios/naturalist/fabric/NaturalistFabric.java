package com.starfish_studios.naturalist.fabric;

import com.google.common.base.Preconditions;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import com.starfish_studios.naturalist.registry.fabric.NaturalistConfigFabric;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModificationContext;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.ModificationPhase;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalBiomeTags;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        AutoConfig.register(NaturalistConfigFabric.class, GsonConfigSerializer::new);
        Naturalist.init();
        addSpawns();
        addFeatures();
        registerEntityAttributes();
        Naturalist.registerBrewingRecipes();
        Naturalist.registerCompostables();
        Naturalist.registerSpawnPlacements();
        Naturalist.registerDispenserBehaviors();
    }


    public void addFeatures() {
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SWAMP),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                getPlacedFeatureKey("patch_cattail")
        );
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SWAMP),
                GenerationStep.Decoration.RAW_GENERATION,
                getPlacedFeatureKey("swamp_mud")
        );
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SWAMP),
                GenerationStep.Decoration.VEGETAL_DECORATION,
                getPlacedFeatureKey("patch_duckweed")
        );
        BiomeModifications.addFeature(
                (biomeSelector) -> biomeSelector.getBiomeKey().equals(Biomes.SAVANNA),
                GenerationStep.Decoration.LAKES,
                getPlacedFeatureKey("savanna_lake")
        );
    }


    private ResourceKey<PlacedFeature> getPlacedFeatureKey(String key) {
        return ResourceKey.create(Registry.PLACED_FEATURE_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, key));
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
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ZEBRA.get(), AbstractHorse.createBaseHorseAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.GIRAFFE.get(), Giraffe.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.HIPPO.get(), Hippo.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.VULTURE.get(), Vulture.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BOAR.get(), Boar.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.DRAGONFLY.get(), Dragonfly.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.CATFISH.get(), Catfish.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.ALLIGATOR.get(), Alligator.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.BASS.get(), AbstractFish.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.LIZARD.get(), Lizard.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.LIZARD_TAIL.get(), LizardTail.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.TORTOISE.get(), Tortoise.createAttributes());
        FabricDefaultAttributeRegistry.register(NaturalistEntityTypes.DUCK.get(), Duck.createAttributes());
    }

    void addSpawns() {
        NaturalistConfigFabric config = AutoConfig.getConfigHolder(NaturalistConfigFabric.class).getConfig();
        addMobSpawn(NaturalistTags.Biomes.HAS_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), config.bearSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), config.deerSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), config.snailSpawnWeight, 1, 3);

        addMobSpawn(NaturalistTags.Biomes.HAS_FIREFLY, MobCategory.AMBIENT, NaturalistEntityTypes.FIREFLY.get(), config.fireflySpawnWeight, 2, 3);


        addMobSpawn(NaturalistTags.Biomes.HAS_BUTTERFLY, MobCategory.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), config.butterflySpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), config.snakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_RATTLESNAKE, MobCategory.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), config.rattlesnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), config.coralSnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), config.bluejaySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.CANARY.get(), config.canarySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL.get(), config.cardinalSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.ROBIN.get(), config.robinSpawnWeight, 1, 4);
        addMobSpawn(BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.RABBIT, config.forestRabbitSpawnWeight, 2, 3);
        addMobSpawn(BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.FOX, config.forestFoxSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_RHINO, MobCategory.CREATURE, NaturalistEntityTypes.RHINO.get(), config.rhinoSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_LION, MobCategory.CREATURE, NaturalistEntityTypes.LION.get(), config.lionSpawnWeight, 3, 5);
        addMobSpawn(NaturalistTags.Biomes.HAS_ELEPHANT, MobCategory.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), config.elephantSpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_ZEBRA, MobCategory.CREATURE, NaturalistEntityTypes.ZEBRA.get(), config.zebraSpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_GIRAFFE, MobCategory.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), config.giraffeSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_HIPPO, MobCategory.CREATURE, NaturalistEntityTypes.HIPPO.get(), config.hippoSpawnWeight, 3, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_VULTURE, MobCategory.CREATURE, NaturalistEntityTypes.VULTURE.get(), config.vultureSpawnWeight, 2, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_BOAR, MobCategory.CREATURE, NaturalistEntityTypes.BOAR.get(), config.boarSpawnWeight, 4, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_DRAGONFLY, MobCategory.AMBIENT, NaturalistEntityTypes.DRAGONFLY.get(), config.dragonflySpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_CATFISH, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.CATFISH.get(), config.catfishSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_ALLIGATOR, MobCategory.CREATURE, NaturalistEntityTypes.ALLIGATOR.get(), config.alligatorSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_BASS, MobCategory.WATER_AMBIENT, NaturalistEntityTypes.BASS.get(), config.bassSpawnWeight, 4, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_LIZARD, MobCategory.CREATURE, NaturalistEntityTypes.LIZARD.get(), config.lizardSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_TORTOISE, MobCategory.CREATURE, NaturalistEntityTypes.TORTOISE.get(), config.tortoiseSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_DUCK, MobCategory.CREATURE, NaturalistEntityTypes.DUCK.get(), config.duckSpawnWeight, 1, 3);
        if (config.removeSavannaFarmAnimals) {
            removeSpawn(BiomeTags.IS_SAVANNA, List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
        if (config.removeSwampFarmAnimals) {
            removeSpawn(ConventionalBiomeTags.SWAMP, List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
    }

    void addMobSpawn(TagKey<Biome> tag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(biomeSelector -> biomeSelector.hasTag(tag), mobCategory, entityType, weight, minGroupSize, maxGroupSize);
    }

    void removeSpawn(TagKey<Biome> tag, List<EntityType<?>> entityTypes) {
        entityTypes.forEach(entityType -> {
            ResourceLocation id = Registry.ENTITY_TYPE.getKey(entityType);
            Preconditions.checkState(Registry.ENTITY_TYPE.containsKey(id), "Unregistered entity type: %s", entityType);
            BiomeModifications.create(id).add(ModificationPhase.REMOVALS, biomeSelector -> biomeSelector.hasTag(tag), context -> context.getSpawnSettings().removeSpawnsOfEntityType(entityType));
        });
        
    }
}
