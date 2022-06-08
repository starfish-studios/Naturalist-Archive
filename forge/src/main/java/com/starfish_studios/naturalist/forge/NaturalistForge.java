package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import dev.architectury.platform.forge.EventBuses;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(Naturalist.MOD_ID)
public class NaturalistForge {
    public NaturalistForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(Naturalist.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        Naturalist.init();
    }
}
