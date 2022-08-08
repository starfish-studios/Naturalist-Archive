package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.platform.forge.CommonPlatformHelperImpl;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistMobCategories;
import com.starfish_studios.naturalist.registry.forge.NaturalistConfigForge;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.biome.SpawnSettings;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.List;

@Mod(Naturalist.MOD_ID)
@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID)
public class NaturalistForge {

    public NaturalistForge() {
        Naturalist.init();

        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, NaturalistConfigForge.COMMON_CONFIG);

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        CommonPlatformHelperImpl.BLOCKS.register(bus);
        CommonPlatformHelperImpl.ITEMS.register(bus);
        CommonPlatformHelperImpl.SOUND_EVENTS.register(bus);
        CommonPlatformHelperImpl.ENTITY_TYPES.register(bus);
        CommonPlatformHelperImpl.POTIONS.register(bus);

        bus.addListener(this::setup);
        bus.addListener(this::createAttributes);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Naturalist.registerBrewingRecipes();
            Naturalist.registerCompostables();
            Naturalist.registerSpawnPlacements();
        });
    }

    private void createAttributes(EntityAttributeCreationEvent event) {
        event.put(NaturalistEntityTypes.SNAIL.get(), Snail.createAttributes().build());
        event.put(NaturalistEntityTypes.BEAR.get(), Bear.createAttributes().build());
        event.put(NaturalistEntityTypes.BUTTERFLY.get(), Butterfly.createAttributes().build());
        event.put(NaturalistEntityTypes.FIREFLY.get(), Firefly.createAttributes().build());
        event.put(NaturalistEntityTypes.SNAKE.get(), Snake.createAttributes().build());
        event.put(NaturalistEntityTypes.CORAL_SNAKE.get(), Snake.createAttributes().build());
        event.put(NaturalistEntityTypes.RATTLESNAKE.get(), Snake.createAttributes().build());
        event.put(NaturalistEntityTypes.DEER.get(), Deer.createAttributes().build());
        event.put(NaturalistEntityTypes.BLUEJAY.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.CANARY.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.CARDINAL.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.ROBIN.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.CATERPILLAR.get(), Caterpillar.createAttributes().build());
        event.put(NaturalistEntityTypes.RHINO.get(), Rhino.createAttributes().build());
        event.put(NaturalistEntityTypes.LION.get(), Lion.createAttributes().build());
        event.put(NaturalistEntityTypes.ELEPHANT.get(), Elephant.createAttributes().build());
        event.put(NaturalistEntityTypes.ZEBRA.get(), HorseBaseEntity.createBaseHorseAttributes().build());
        event.put(NaturalistEntityTypes.GIRAFFE.get(), Giraffe.createAttributes().build());
        event.put(NaturalistEntityTypes.HIPPO.get(), Hippo.createAttributes().build());
        event.put(NaturalistEntityTypes.VULTURE.get(), Vulture.createAttributes().build());
        event.put(NaturalistEntityTypes.BOAR.get(), Boar.createAttributes().build());
    }

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.TAIGA), SpawnGroup.CREATURE, NaturalistEntityTypes.BEAR.get(), NaturalistConfigForge.BEAR_SPAWN_WEIGHT.get(), 1, 2);
        addMobSpawn(event, List.of(Biome.Category.FOREST), SpawnGroup.CREATURE, NaturalistEntityTypes.DEER.get(), NaturalistConfigForge.DEER_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.SWAMP, Biome.Category.SAVANNA, Biome.Category.RIVER, Biome.Category.EXTREME_HILLS, Biome.Category.MUSHROOM, Biome.Category.UNDERGROUND, Biome.Category.MOUNTAIN), SpawnGroup.CREATURE, NaturalistEntityTypes.SNAIL.get(), NaturalistConfigForge.SNAIL_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.SWAMP, Biome.Category.MUSHROOM), NaturalistMobCategories.getFireflyCategory(), NaturalistEntityTypes.FIREFLY.get(), NaturalistConfigForge.FIREFLY_SPAWN_WEIGHT.get(), 2, 3);
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.PLAINS), SpawnGroup.CREATURE, NaturalistEntityTypes.BUTTERFLY.get(), NaturalistConfigForge.BUTTERFLY_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.SWAMP), SpawnGroup.CREATURE, NaturalistEntityTypes.SNAKE.get(), NaturalistConfigForge.SNAKE_SPAWN_WEIGHT.get(), 1, 1);
        addMobSpawn(event, List.of(Biome.Category.MESA, Biome.Category.SAVANNA, Biome.Category.DESERT), SpawnGroup.CREATURE, NaturalistEntityTypes.RATTLESNAKE.get(), NaturalistConfigForge.RATTLESNAKE_SPAWN_WEIGHT.get(), 1, 1);
        addMobSpawn(event, List.of(Biome.Category.JUNGLE, Biome.Category.BEACH, Biome.Category.RIVER), SpawnGroup.CREATURE, NaturalistEntityTypes.CORAL_SNAKE.get(), NaturalistConfigForge.CORAL_SNAKE_SPAWN_WEIGHT.get(), 1, 1);
        addMobSpawn(event, List.of(Biome.Category.TAIGA, Biome.Category.EXTREME_HILLS, Biome.Category.ICY), SpawnGroup.CREATURE, NaturalistEntityTypes.BLUEJAY.get(), NaturalistConfigForge.BLUEJAY_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.Category.EXTREME_HILLS, Biome.Category.MOUNTAIN), SpawnGroup.CREATURE, NaturalistEntityTypes.CANARY.get(), NaturalistConfigForge.CANARY_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.SWAMP, Biome.Category.SAVANNA, Biome.Category.DESERT), SpawnGroup.CREATURE, NaturalistEntityTypes.CARDINAL.get(), NaturalistConfigForge.CARDINAL_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.Category.FOREST, Biome.Category.PLAINS, Biome.Category.MOUNTAIN), SpawnGroup.CREATURE, NaturalistEntityTypes.ROBIN.get(), NaturalistConfigForge.ROBIN_SPAWN_WEIGHT.get(), 1, 4);
        addMobSpawn(event, List.of(Biome.Category.FOREST), SpawnGroup.CREATURE, EntityType.RABBIT, NaturalistConfigForge.FOREST_RABBIT_SPAWN_WEIGHT.get(), 2, 3);
        addMobSpawn(event, List.of(Biome.Category.FOREST), SpawnGroup.CREATURE, EntityType.FOX, NaturalistConfigForge.FOREST_FOX_SPAWN_WEIGHT.get(), 2, 4);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.RHINO.get(), NaturalistConfigForge.RHINO_SPAWN_WEIGHT.get(), 1, 3);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.LION.get(), NaturalistConfigForge.LION_SPAWN_WEIGHT.get(), 3, 5);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.ELEPHANT.get(), NaturalistConfigForge.ELEPHANT_SPAWN_WEIGHT.get(), 2, 3);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.ZEBRA.get(), NaturalistConfigForge.ZEBRA_SPAWN_WEIGHT.get(), 2, 3);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.GIRAFFE.get(), NaturalistConfigForge.GIRAFFE_SPAWN_WEIGHT.get(), 2, 4);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA, Biome.Category.JUNGLE), SpawnGroup.CREATURE, NaturalistEntityTypes.HIPPO.get(), NaturalistConfigForge.HIPPO_SPAWN_WEIGHT.get(), 3, 4);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA, Biome.Category.DESERT), SpawnGroup.CREATURE, NaturalistEntityTypes.VULTURE.get(), NaturalistConfigForge.VULTURE_SPAWN_WEIGHT.get(), 2, 4);
        addMobSpawn(event, List.of(Biome.Category.SAVANNA), SpawnGroup.CREATURE, NaturalistEntityTypes.BOAR.get(), NaturalistConfigForge.BOAR_SPAWN_WEIGHT.get(), 4, 4);
        if (NaturalistConfigForge.REMOVE_SAVANNA_FARM_ANIMALS.get()) {
            removeMobSpawn(event, List.of(Biome.Category.SAVANNA), List.of(EntityType.SHEEP, EntityType.PIG, EntityType.CHICKEN, EntityType.COW));
        }
    }

    private static void addMobSpawn(BiomeLoadingEvent event, List<Biome.Category> categories, SpawnGroup spawnGroup, EntityType<?> entityType, int weight, int minGroupSize, int maxGroupSize) {
        if (categories.contains(event.getCategory())) {
            event.getSpawns().spawn(spawnGroup, new SpawnSettings.SpawnEntry(entityType, weight, minGroupSize, maxGroupSize));
        }
    }

    private static void removeMobSpawn(BiomeLoadingEvent event, List<Biome.Category> categories, List<EntityType<?>> entityTypes) {
        if (categories.contains(event.getCategory())) {
            event.getSpawns().getSpawner(SpawnGroup.CREATURE).removeIf(entry -> entityTypes.contains(entry.type));
        }
    }
}
