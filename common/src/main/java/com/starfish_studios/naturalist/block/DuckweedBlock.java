package com.starfish_studios.naturalist.block;

import net.minecraft.core.BlockPos;
import net.minecraft.data.tags.FluidTagsProvider;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;

public class DuckweedBlock extends WaterlilyBlock {
    public DuckweedBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        FluidState fluidState = level.getFluidState(pos);
        FluidState fluidState2 = level.getFluidState(pos.above());
        return (fluidState.is(FluidTags.WATER) || fluidState.getType() == Fluids.WATER || state.getMaterial() == Material.ICE) && fluidState2.getType() == Fluids.EMPTY;
    }
}
