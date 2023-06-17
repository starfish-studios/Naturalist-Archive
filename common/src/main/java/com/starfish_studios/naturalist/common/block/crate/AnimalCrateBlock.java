package com.starfish_studios.naturalist.common.block.crate;

import net.minecraft.core.*;
import net.minecraft.world.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.phys.*;
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

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack) {
        super.setPlacedBy(level, pos, state, placer, stack);
        if (level.getBlockEntity(pos) instanceof AnimalCrateBlockEntity animalCrateBlockEntity) {
            animalCrateBlockEntity.onPlace(placer, stack);
        }
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult ray) {
        if (level.getBlockEntity(pos) instanceof AnimalCrateBlockEntity animalCrateBlockEntity) {
            return animalCrateBlockEntity.onUse(player, hand);
        }
        return super.use(state, level, pos, player, hand, ray);
    }
}
