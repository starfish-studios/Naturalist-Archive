package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.registry.forge.NaturalistRegistryHelperImpl;
import net.minecraftforge.common.MinecraftForge;
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

        bus.addListener(this::setup);

        MinecraftForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            Naturalist.registerBrewingRecipes();
            Naturalist.registerSpawnPlacements();
        });
    }
}
