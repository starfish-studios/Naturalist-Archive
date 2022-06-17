package com.starfish_studios.naturalist.block;

import com.starfish_studios.naturalist.entity.Butterfly;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.state.property.Properties;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldView;
import javax.annotation.Nullable;
import java.util.Random;

public class ChrysalisBlock extends HorizontalFacingBlock {
    public static final IntProperty AGE = Properties.AGE_3;
    protected static final VoxelShape[] EAST_AABB = new VoxelShape[]{Block.createCuboidShape(11.0D, 7.0D, 6.0D, 15.0D, 12.0D, 10.0D), Block.createCuboidShape(9.0D, 5.0D, 5.0D, 15.0D, 12.0D, 11.0D), Block.createCuboidShape(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D), Block.createCuboidShape(7.0D, 3.0D, 4.0D, 15.0D, 12.0D, 12.0D)};
    protected static final VoxelShape[] WEST_AABB = new VoxelShape[]{Block.createCuboidShape(1.0D, 7.0D, 6.0D, 5.0D, 12.0D, 10.0D), Block.createCuboidShape(1.0D, 5.0D, 5.0D, 7.0D, 12.0D, 11.0D), Block.createCuboidShape(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D), Block.createCuboidShape(1.0D, 3.0D, 4.0D, 9.0D, 12.0D, 12.0D)};
    protected static final VoxelShape[] NORTH_AABB = new VoxelShape[]{Block.createCuboidShape(6.0D, 7.0D, 1.0D, 10.0D, 12.0D, 5.0D), Block.createCuboidShape(5.0D, 5.0D, 1.0D, 11.0D, 12.0D, 7.0D), Block.createCuboidShape(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D), Block.createCuboidShape(4.0D, 3.0D, 1.0D, 12.0D, 12.0D, 9.0D)};
    protected static final VoxelShape[] SOUTH_AABB = new VoxelShape[]{Block.createCuboidShape(6.0D, 7.0D, 11.0D, 10.0D, 12.0D, 15.0D), Block.createCuboidShape(5.0D, 5.0D, 9.0D, 11.0D, 12.0D, 15.0D), Block.createCuboidShape(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D), Block.createCuboidShape(4.0D, 3.0D, 7.0D, 12.0D, 12.0D, 15.0D)};

    public ChrysalisBlock(Settings properties) {
        super(properties);
        this.setDefaultState(this.stateManager.getDefaultState().with(FACING, Direction.NORTH).with(AGE, 0));
    }

    @Override
    public boolean hasRandomTicks(BlockState pState) {
        return true;
    }

    @Override
    public void randomTick(BlockState pState, ServerWorld pLevel, BlockPos pPos, RandomSource pRandom) {
        int age = pState.get(AGE);
        if (age < 3) {
            if (pLevel.random.nextInt(5) == 0) {
                pLevel.setBlockState(pPos, pState.with(AGE, age + 1), 2);
            }
        } else {
            pLevel.removeBlock(pPos, false);
            pLevel.playSound(null, pPos, SoundEvents.BLOCK_WOOD_BREAK, SoundCategory.BLOCKS, 0.7F, 0.9F + pRandom.nextFloat() * 0.2F);
            pLevel.syncWorldEvent(2001, pPos, Block.getRawIdFromState(pState));
            Butterfly butterfly = NaturalistEntityTypes.BUTTERFLY.get().create(pLevel);
            butterfly.refreshPositionAndAngles(pPos.getX() + 0.5D, pPos.getY() + 0.5D, pPos.getZ() + 0.5D, 0.0F, 0.0F);
            pLevel.spawnEntity(butterfly);
        }
    }

    @Override
    public boolean canPlaceAt(BlockState pState, WorldView pLevel, BlockPos pPos) {
        BlockState facingState = pLevel.getBlockState(pPos.offset(pState.get(FACING)));
        return facingState.isIn(BlockTags.LOGS);
    }

    @Override
    public VoxelShape getOutlineShape(BlockState pState, BlockView pLevel, BlockPos pPos, ShapeContext pContext) {
        int age = pState.get(AGE);
        switch (pState.get(FACING)) {
            case SOUTH:
                return SOUTH_AABB[age];
            case NORTH:
            default:
                return NORTH_AABB[age];
            case WEST:
                return WEST_AABB[age];
            case EAST:
                return EAST_AABB[age];
        }
    }

    @Nullable
    @Override
    public BlockState getPlacementState(ItemPlacementContext pContext) {
        BlockState state = this.getDefaultState();
        WorldView level = pContext.getWorld();
        BlockPos pos = pContext.getBlockPos();

        for(Direction direction : pContext.getPlacementDirections()) {
            if (direction.getAxis().isHorizontal()) {
                state = state.with(FACING, direction);
                if (state.canPlaceAt(level, pos)) {
                    return state;
                }
            }
        }

        return null;
    }

    @Override
    public BlockState getStateForNeighborUpdate(BlockState pState, Direction pFacing, BlockState pFacingState, WorldAccess pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        return pFacing == pState.get(FACING) && !pState.canPlaceAt(pLevel, pCurrentPos) ? Blocks.AIR.getDefaultState() : super.getStateForNeighborUpdate(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void appendProperties(StateManager.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING, AGE);
    }
}
