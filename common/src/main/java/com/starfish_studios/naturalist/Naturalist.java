package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.entity.Bird;
import com.starfish_studios.naturalist.entity.Firefly;
import com.starfish_studios.naturalist.entity.Snake;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import com.starfish_studios.naturalist.registry.*;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.potion.Potions;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

public class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final ItemGroup TAB = CommonPlatformHelper.registerCreativeModeTab(new Identifier(MOD_ID, "tab"), () -> new ItemStack(Items.FERN));
    public static final Logger LOGGER = LogManager.getLogger();

    public static void init() {
        GeckoLib.initialize();
        NaturalistBlocks.init();
        NaturalistItems.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
    }
    
    public static void registerBrewingRecipes() {
        CommonPlatformHelper.registerBrewingRecipe(Potions.AWKWARD, NaturalistItems.ANTLER.get(), NaturalistPotions.FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.REDSTONE, NaturalistPotions.LONG_FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.GLOWSTONE_DUST, NaturalistPotions.STRONG_FOREST_DASHER.get());
        CommonPlatformHelper.registerBrewingRecipe(Potions.AWKWARD, NaturalistItems.GLOW_GOOP.get(), NaturalistPotions.GLOWING.get());
        CommonPlatformHelper.registerBrewingRecipe(NaturalistPotions.GLOWING.get(), Items.REDSTONE, NaturalistPotions.LONG_GLOWING.get());
    }

    public static void registerSpawnPlacements() {
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAIL.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, MobEntity::canMobSpawn);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BEAR.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING_NO_LEAVES, AnimalEntity::isValidNaturalSpawn);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BUTTERFLY.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, AnimalEntity::isValidNaturalSpawn);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.FIREFLY.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAKE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CORAL_SNAKE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.RATTLESNAKE.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.DEER.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, AnimalEntity::isValidNaturalSpawn);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.BLUEJAY.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CANARY.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.CARDINAL.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        CommonPlatformHelper.registerSpawnPlacement(NaturalistEntityTypes.ROBIN.get(), SpawnRestriction.Location.ON_GROUND, Heightmap.Type.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
    }

    public static void registerCompostables() {
        CommonPlatformHelper.registerCompostable(0.65F, NaturalistItems.SNAIL_SHELL.get());
    }
}
