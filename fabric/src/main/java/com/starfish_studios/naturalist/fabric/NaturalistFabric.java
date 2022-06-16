package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistMobCategories;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import com.starfish_studios.naturalist.registry.fabric.NaturalistConfig;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.tags.BiomeTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Naturalist.init();
        AutoConfig.register(NaturalistConfig.class, GsonConfigSerializer::new);
        addSpawns();
        registerEntityAttributes();
        Naturalist.registerBrewingRecipes();
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
        NaturalistConfig config = AutoConfig.getConfigHolder(NaturalistConfig.class).getConfig();
        addMobSpawn(NaturalistTags.Biomes.HAS_BEAR, MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), config.bearSpawnWeight, 1, 2);
        addMobSpawn(NaturalistTags.Biomes.HAS_DEER, MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), config.deerSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAIL, MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), config.snailSpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_FIREFLY, NaturalistMobCategories.getFireflyCategory(), NaturalistEntityTypes.FIREFLY.get(), config.fireflySpawnWeight, 2, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_BUTTERFLY, MobCategory.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), config.butterflySpawnWeight, 1, 3);
        addMobSpawn(NaturalistTags.Biomes.HAS_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), config.snakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_RATTLESNAKE, MobCategory.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), config.rattlesnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_CORAL_SNAKE, MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), config.coralSnakeSpawnWeight, 1, 1);
        addMobSpawn(NaturalistTags.Biomes.HAS_BLUEJAY, MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), config.bluejaySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CANARY, MobCategory.CREATURE, NaturalistEntityTypes.CANARY.get(), config.canarySpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_CARDINAL, MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL.get(), config.cardinalSpawnWeight, 1, 4);
        addMobSpawn(NaturalistTags.Biomes.HAS_ROBIN, MobCategory.CREATURE, NaturalistEntityTypes.ROBIN.get(), config.robinSpawnWeight, 1, 4);
        addMobSpawn(BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.RABBIT,  config.forestRabbitSpawnWeight, 2, 3);
        addMobSpawn(BiomeTags.IS_FOREST, MobCategory.CREATURE, EntityType.FOX, config.forestFoxSpawnWeight, 2, 4);
    }

    void addMobSpawn(TagKey<Biome> tag, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        BiomeModifications.addSpawn(biomeSelector -> biomeSelector.hasTag(tag) && biomeSelector.hasTag(BiomeTags.IS_OVERWORLD), mobCategory, entityType, weight, minGroupSize, maxGroupSize);
    }
}
