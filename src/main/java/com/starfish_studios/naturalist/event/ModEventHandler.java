package com.starfish_studios.naturalist.event;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Firefly;
import com.starfish_studios.naturalist.entity.Snake;
import com.starfish_studios.naturalist.entity.Bird;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistItems;
import com.starfish_studios.naturalist.registry.NaturalistPotions;
import com.starfish_studios.naturalist.util.NaturalistBrewingRecipe;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventHandler {
    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(Potions.AWKWARD, NaturalistItems.ANTLER.get(), NaturalistPotions.FOREST_DASHER.get()));
            BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.REDSTONE, NaturalistPotions.LONG_FOREST_DASHER.get()));
            BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(NaturalistPotions.FOREST_DASHER.get(), Items.GLOWSTONE_DUST, NaturalistPotions.STRONG_FOREST_DASHER.get()));
            BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(Potions.AWKWARD, NaturalistItems.GLOW_GOOP.get(), NaturalistPotions.GLOWING.get()));
            BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(NaturalistPotions.GLOWING.get(), Items.REDSTONE, NaturalistPotions.LONG_GLOWING.get()));

            SpawnPlacements.register(NaturalistEntityTypes.SNAIL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Mob::checkMobSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.BEAR.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.BUTTERFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.FIREFLY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Firefly::checkFireflySpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.CORAL_SNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.RATTLESNAKE.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Snake::checkSnakeSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.DEER.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Animal::checkAnimalSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.BLUEJAY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.CANARY.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.CARDINAL.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
            SpawnPlacements.register(NaturalistEntityTypes.ROBIN.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.MOTION_BLOCKING, Bird::checkBirdSpawnRules);
        });
    }
}
