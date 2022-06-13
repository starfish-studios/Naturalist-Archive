package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.entity.Bird;
import com.starfish_studios.naturalist.entity.Firefly;
import com.starfish_studios.naturalist.entity.Snake;
import com.starfish_studios.naturalist.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;
import software.bernie.geckolib3.GeckoLib;

public class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final CreativeModeTab TAB = NaturalistRegistryHelper.registerCreativeModeTab(new ResourceLocation(MOD_ID, "tab"), () -> new ItemStack(Items.FERN));

    public static void init() {
        GeckoLib.initialize();
        NaturalistBlocks.init();
        NaturalistItems.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
    }
    
    public static void registerBrewingRecipes() {
        NaturalistRegistryHelper.registerBrewingRecipe(Potions.AWKWARD, NaturalistItems.ANTLER.get(), NaturalistPotions.FOREST_DASHER.get());
        NaturalistRegistryHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.REDSTONE, NaturalistPotions.LONG_FOREST_DASHER.get());
        NaturalistRegistryHelper.registerBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.GLOWSTONE_DUST, NaturalistPotions.STRONG_FOREST_DASHER.get());
        NaturalistRegistryHelper.registerBrewingRecipe(Potions.AWKWARD, NaturalistItems.GLOW_GOOP.get(), NaturalistPotions.GLOWING.get());
        NaturalistRegistryHelper.registerBrewingRecipe(NaturalistPotions.GLOWING.get(), Items.REDSTONE, NaturalistPotions.LONG_GLOWING.get());
    }

    public static void registerSpawnPlacements() {
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAIL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.BUTTERFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.FIREFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.CORAL_SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.RATTLESNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.BLUEJAY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.CANARY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.CARDINAL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        NaturalistRegistryHelper.registerSpawnPlacement(NaturalistEntityTypes.ROBIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
    }
}
