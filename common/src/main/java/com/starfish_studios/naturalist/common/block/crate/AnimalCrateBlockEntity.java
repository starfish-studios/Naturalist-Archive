package com.starfish_studios.naturalist.common.block.crate;

import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class AnimalCrateBlockEntity extends BlockEntity {

    public static final String ANIMAL_CRATE_DATA = "stored_animal";

    public AnimalCrateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NaturalistBlockEntities.ANIMAL_CRATE.get(), blockPos, blockState);
    }

    @Override
    protected void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
    }
}
