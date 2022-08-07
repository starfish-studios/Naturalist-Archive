package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.platform.forge.CommonPlatformHelperImpl;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
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
        event.put(NaturalistEntityTypes.ZEBRA.get(), AbstractHorse.createBaseHorseAttributes().build());
        event.put(NaturalistEntityTypes.GIRAFFE.get(), Giraffe.createAttributes().build());
        event.put(NaturalistEntityTypes.HIPPO.get(), Hippo.createAttributes().build());
        event.put(NaturalistEntityTypes.VULTURE.get(), Vulture.createAttributes().build());
        event.put(NaturalistEntityTypes.BOAR.get(), Boar.createAttributes().build());
    }
}
