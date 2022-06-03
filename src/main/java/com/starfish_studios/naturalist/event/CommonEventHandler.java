package com.starfish_studios.naturalist.event;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.registry.NaturalistMobCategories;
import com.starfish_studios.naturalist.registry.NaturalistConfig;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        MobSpawnSettings.SpawnerData bear = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BEAR.get(), NaturalistConfig.BEAR_SPAWN_WEIGHT.get(), 1, 2);
        MobSpawnSettings.SpawnerData deer = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.DEER.get(), NaturalistConfig.DEER_SPAWN_WEIGHT.get(), 1, 3);
        MobSpawnSettings.SpawnerData snail = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), NaturalistConfig.SNAIL_SPAWN_WEIGHT.get(), 1, 3);
        MobSpawnSettings.SpawnerData firefly = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), NaturalistConfig.FIREFLY_SPAWN_WEIGHT.get(), 2, 3);
        MobSpawnSettings.SpawnerData butterfly = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfig.BUTTERFLY_SPAWN_WEIGHT.get(), 1, 3);
        MobSpawnSettings.SpawnerData snake = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAKE.get(), NaturalistConfig.SNAKE_SPAWN_WEIGHT.get(), 1, 1);
        MobSpawnSettings.SpawnerData rattlesnake = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RATTLESNAKE.get(), NaturalistConfig.RATTLESNAKE_SPAWN_WEIGHT.get(), 1, 1);
        MobSpawnSettings.SpawnerData coralsnake = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfig.CORAL_SNAKE_SPAWN_WEIGHT.get(), 1, 1);
        MobSpawnSettings.SpawnerData bluejay = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        MobSpawnSettings.SpawnerData canary = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CANARY.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        MobSpawnSettings.SpawnerData cardinal = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CARDINAL.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        MobSpawnSettings.SpawnerData robin = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.ROBIN.get(), NaturalistConfig.BIRD_SPAWN_WEIGHT.get(), 1, 4);
        MobSpawnSettings.SpawnerData rabbit = new MobSpawnSettings.SpawnerData(EntityType.RABBIT,  NaturalistConfig.FOREST_RABBIT_SPAWN_WEIGHT.get(), 2, 3);
        MobSpawnSettings.SpawnerData fox = new MobSpawnSettings.SpawnerData(EntityType.FOX, NaturalistConfig.FOREST_FOX_SPAWN_WEIGHT.get(), 2, 4);

        switch (event.getCategory()) {
            case FOREST -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, bear)
                        .addSpawn(MobCategory.CREATURE, deer)
                        .addSpawn(MobCategory.CREATURE, snake)
                        .addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(NaturalistMobCategories.FIREFLIES, firefly)
                        .addSpawn(MobCategory.CREATURE, butterfly)
                        .addSpawn(MobCategory.CREATURE, rabbit)
                        .addSpawn(MobCategory.CREATURE, fox)
                        .addSpawn(MobCategory.CREATURE, cardinal)
                        .addSpawn(MobCategory.CREATURE, robin);
            }
            case TAIGA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, bear)
                        .addSpawn(MobCategory.CREATURE, bluejay);
            }
            case PLAINS -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snake)
                        .addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(NaturalistMobCategories.FIREFLIES, firefly)
                        .addSpawn(MobCategory.CREATURE, butterfly)
                        .addSpawn(MobCategory.CREATURE, robin);
            }
            case SWAMP -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snake)
                        .addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(NaturalistMobCategories.FIREFLIES, firefly)
                        .addSpawn(MobCategory.CREATURE, cardinal);
            }
            case MESA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, rattlesnake);
            }
            case SAVANNA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, rattlesnake)
                        .addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(MobCategory.CREATURE, cardinal);
            }
            case DESERT -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, rattlesnake)
                        .addSpawn(MobCategory.CREATURE, cardinal);
            }
            case JUNGLE -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, coralsnake);
            }
            case BEACH -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, coralsnake);
            }
            case RIVER -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, coralsnake)
                        .addSpawn(MobCategory.CREATURE, snail);
            }
            case EXTREME_HILLS -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(MobCategory.CREATURE, bluejay)
                        .addSpawn(MobCategory.CREATURE, canary);
            }
            case MUSHROOM -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(NaturalistMobCategories.FIREFLIES, firefly);
            }
            case UNDERGROUND -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
            }
            case MOUNTAIN -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail)
                        .addSpawn(MobCategory.CREATURE, canary)
                        .addSpawn(MobCategory.CREATURE, robin);
            }
            case ICY -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, bluejay);
            }
        }
    }
}
