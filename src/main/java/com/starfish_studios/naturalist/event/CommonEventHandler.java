package com.starfish_studios.naturalist.event;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.registry.NaturalistMobCategories;
import com.starfish_studios.naturalist.registry.NaturalistConfig;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.List;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.TAIGA), MobCategory.CREATURE, NaturalistEntityTypes.BEAR.get(), NaturalistConfig.BEAR_SPAWN_WEIGHT.get(), 1, 2);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST), MobCategory.CREATURE, NaturalistEntityTypes.DEER.get(), NaturalistConfig.DEER_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.SWAMP, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.RIVER, Biome.BiomeCategory.EXTREME_HILLS, Biome.BiomeCategory.MUSHROOM, Biome.BiomeCategory.UNDERGROUND, Biome.BiomeCategory.MOUNTAIN), MobCategory.CREATURE, NaturalistEntityTypes.SNAIL.get(), NaturalistConfig.SNAIL_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.SWAMP, Biome.BiomeCategory.MUSHROOM), NaturalistMobCategories.FIREFLIES, NaturalistEntityTypes.FIREFLY.get(), NaturalistConfig.FIREFLY_SPAWN_WEIGHT.get(), 2, 3);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.PLAINS), MobCategory.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfig.BUTTERFLY_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.SWAMP), MobCategory.CREATURE, NaturalistEntityTypes.SNAKE.get(), NaturalistConfig.SNAKE_SPAWN_WEIGHT.get(), 1, 1);
        addMobSpawn(event, List.of(Biome.BiomeCategory.MESA, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.DESERT), MobCategory.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), NaturalistConfig.RATTLESNAKE_SPAWN_WEIGHT.get(), 1, 1);
        addMobSpawn(event, List.of(Biome.BiomeCategory.JUNGLE, Biome.BiomeCategory.BEACH, Biome.BiomeCategory.RIVER), MobCategory.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfig.CORAL_SNAKE_SPAWN_WEIGHT.get(), 1, 1);
        addMobSpawn(event, List.of(Biome.BiomeCategory.TAIGA, Biome.BiomeCategory.EXTREME_HILLS, Biome.BiomeCategory.ICY), MobCategory.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.BiomeCategory.EXTREME_HILLS, Biome.BiomeCategory.MOUNTAIN), MobCategory.CREATURE, NaturalistEntityTypes.CANARY.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.SWAMP, Biome.BiomeCategory.SAVANNA, Biome.BiomeCategory.DESERT), MobCategory.CREATURE, NaturalistEntityTypes.CARDINAL.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST, Biome.BiomeCategory.PLAINS, Biome.BiomeCategory.MOUNTAIN), MobCategory.CREATURE, NaturalistEntityTypes.ROBIN.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST), MobCategory.CREATURE, EntityType.RABBIT, NaturalistConfig.FOREST_RABBIT_SPAWN_WEIGHT.get(), 2, 3);
        addMobSpawn(event, List.of(Biome.BiomeCategory.FOREST), MobCategory.CREATURE, EntityType.FOX, NaturalistConfig.FOREST_FOX_SPAWN_WEIGHT.get(), 2, 4);
    }

    private static void addMobSpawn(BiomeLoadingEvent event, List<Biome.BiomeCategory> categories, MobCategory mobCategory, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (categories.contains(event.getCategory())) {
            event.getSpawns().addSpawn(mobCategory, new MobSpawnSettings.SpawnerData(entityType, weight, minGroupSize, maxGroupSize));
        }
    }
}
