//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.starfish_studios.naturalist.block;

import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CattailBlock extends DoublePlantBlock implements SimpleWaterloggedBlock {
    private static final BooleanProperty WATERLOGGED;
    protected static final float AABB_OFFSET = 6.0F;
    protected static final VoxelShape SHAPE;

    public CattailBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(HALF, DoubleBlockHalf.LOWER).setValue(WATERLOGGED, false));
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPE;
    }

    protected boolean mayPlaceOn(BlockState state, BlockGetter level, BlockPos pos) {
        return state.is(NaturalistTags.BlockTags.CATTAIL_PLACEABLE) || level.getFluidState(pos.above()).isSourceOfType(Fluids.WATER) && super.mayPlaceOn(state, level, pos);
    }

    @Nullable
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockState = super.getStateForPlacement(context);
        return blockState != null ? copyWaterloggedFrom(context.getLevel(), context.getClickedPos()) : null;
    }

    private BlockState copyWaterloggedFrom(Level level, BlockPos clickedPos) {
        return this.defaultBlockState().setValue(WATERLOGGED, level.getFluidState(clickedPos).getType() == Fluids.WATER);
    }

    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity placer, ItemStack stack) {
        if (!level.isClientSide()) {
            BlockPos blockPos = pos.above();
            BlockState blockState = DoublePlantBlock.copyWaterloggedFrom(level, blockPos, this.defaultBlockState().setValue(HALF, DoubleBlockHalf.UPPER));
            level.setBlock(blockPos, blockState, 3);
        }

    }

    public FluidState getFluidState(BlockState state) {
        return (Boolean)state.getValue(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.getFluidState(state);
    }

    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        if (state.getValue(HALF) == DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, level, pos);
        } else {
            BlockPos blockPos = pos.below();
            BlockState blockState = level.getBlockState(blockPos);
            return this.mayPlaceOn(blockState, level, blockPos);
        }
    }

    public BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos currentPos, BlockPos neighborPos) {
        if (state.getValue(WATERLOGGED)) {
            level.scheduleTick(currentPos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        return super.updateShape(state, direction, neighborState, level, currentPos, neighborPos);
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(HALF, WATERLOGGED);
    }

    public float getMaxVerticalOffset() {
        return 0.2F;
    }


    static {
        WATERLOGGED = BlockStateProperties.WATERLOGGED;
        SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 13.0, 14.0);
    }
}
