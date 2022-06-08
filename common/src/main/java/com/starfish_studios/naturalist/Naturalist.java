package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.registry.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class Naturalist {
    public static final String MOD_ID = "naturalist";
    public static final CreativeModeTab TAB = NaturalistRegistryHelper.registerCreativeModeTab(new ResourceLocation(MOD_ID, "tab"), () -> new ItemStack(Items.FERN));

    public static void init() {
        NaturalistBlocks.init();
        NaturalistItems.init();
        NaturalistSoundEvents.init();
        NaturalistEntityTypes.init();
        NaturalistPotions.init();
    }
}
