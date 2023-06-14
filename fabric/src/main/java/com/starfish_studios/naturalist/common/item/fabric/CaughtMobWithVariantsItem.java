package com.starfish_studios.naturalist.common.item.fabric;

import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.material.*;

//TODO: give this a better, shorter name
public class CaughtMobWithVariantsItem extends CaughtMobItem {
    private final int variantCount;

    public CaughtMobWithVariantsItem(EntityType<?> entitySupplier, Fluid fluid, SoundEvent emptyingSound, int variantCount, Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
        this.variantCount = variantCount;
    }

    @Override
    public void fillItemCategory(CreativeModeTab category, NonNullList<ItemStack> items) {
        for (int i = 0; i < variantCount; i++) {
            ItemStack variantStack = new ItemStack(this);
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putInt("Variant", i);
            variantStack.setTag(compoundTag);
            items.add(variantStack);
        }
    }
}
