package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.forge.NaturalistBiomeModifierSerializers;
import com.starfish_studios.naturalist.registry.forge.NaturalistRegistryHelperImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Naturalist.MOD_ID)
public class NaturalistForge {

    public NaturalistForge() {
        Naturalist.init();

        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        NaturalistRegistryHelperImpl.BLOCKS.register(bus);
        NaturalistRegistryHelperImpl.ITEMS.register(bus);
        NaturalistRegistryHelperImpl.SOUND_EVENTS.register(bus);
        NaturalistRegistryHelperImpl.ENTITY_TYPES.register(bus);
        NaturalistRegistryHelperImpl.POTIONS.register(bus);
        NaturalistBiomeModifierSerializers.BIOME_MODIFIER_SERIALIZERS.register(bus);

        bus.addListener(this::setup);
        bus.addListener(this::createAttributes);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Naturalist.registerBrewingRecipes();
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
    }
}
