package com.starfish_studios.naturalist.common.block.crate;

import net.minecraft.core.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import org.jetbrains.annotations.*;

public class AnimalCrateBlock extends Block implements EntityBlock {
    public AnimalCrateBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new AnimalCrateBlockEntity(pos, state);
    }
}
