package com.starfish_studios.naturalist.common.block.crate;

import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;

public class AnimalCrateBlockEntity extends BlockEntity {
    public AnimalCrateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(NaturalistBlockEntities.ANIMAL_CRATE.get(), blockPos, blockState);
    }
}
