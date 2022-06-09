package com.starfish_studios.naturalist.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class NaturalistFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        Naturalist.init();
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
}
