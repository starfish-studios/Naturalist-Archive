package com.starfish_studios.naturalist.block;

import com.starfish_studios.naturalist.entity.Alligator;
import com.starfish_studios.naturalist.entity.Tortoise;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.TurtleEggBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class TortoiseEggBlock extends TurtleEggBlock {
    public TortoiseEggBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (this.shouldUpdateHatchLevel(level)) {
            int i = state.getValue(HATCH);
            if (i < 2) {
                level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_CRACK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                level.setBlock(pos, (BlockState)state.setValue(HATCH, i + 1), 2);
            } else {
                level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_HATCH.get(), SoundSource.BLOCKS, 0.7f, 0.9f + random.nextFloat() * 0.2f);
                level.removeBlock(pos, false);
                for (int j = 0; j < state.getValue(EGGS); ++j) {
                    level.levelEvent(2001, pos, Block.getId(state));
                    Tortoise tortoise = NaturalistEntityTypes.TORTOISE.get().create(level);
                    tortoise.setAge(-24000);
                    tortoise.moveTo((double)pos.getX() + 0.3 + (double)j * 0.2, pos.getY(), (double)pos.getZ() + 0.3, 0.0f, 0.0f);
                    level.addFreshEntity(tortoise);
                }
            }
        }
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (!level.isClientSide) {
            level.levelEvent(2005, pos, 0);
        }
    }

    private boolean shouldUpdateHatchLevel(Level level) {
        return level.random.nextInt(50) == 0;
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.isSteppingCarefully()) {
            this.destroyEgg(level, state, pos, entity, 100);
        }
        super.stepOn(level, pos, state, entity);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (!(entity instanceof Zombie)) {
            this.destroyEgg(level, state, pos, entity, 3);
        }
        super.fallOn(level, state, pos, entity, fallDistance);
    }

    private void destroyEgg(Level level, BlockState state, BlockPos pos, Entity entity, int chance) {
        if (!this.canDestroyEgg(level, entity)) {
            return;
        }
        if (!level.isClientSide && level.random.nextInt(chance) == 0 && state.is(Blocks.TURTLE_EGG)) {
            this.decreaseEggs(level, pos, state);
        }
    }

    private boolean canDestroyEgg(Level level, Entity entity) {
        if (entity instanceof Alligator) {
            return false;
        }
        if (entity instanceof LivingEntity) {
            return entity instanceof Player || level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING);
        }
        return false;
    }

    private void decreaseEggs(Level level, BlockPos pos, BlockState state) {
        level.playSound(null, pos, NaturalistSoundEvents.TORTOISE_EGG_BREAK.get(), SoundSource.BLOCKS, 0.7f, 0.9f + level.random.nextFloat() * 0.2f);
        int i = state.getValue(EGGS);
        if (i <= 1) {
            level.destroyBlock(pos, false);
        } else {
            level.setBlock(pos, state.setValue(EGGS, i - 1), 2);
            level.gameEvent(GameEvent.BLOCK_DESTROY, pos, GameEvent.Context.of(state));
            level.levelEvent(2001, pos, Block.getId(state));
        }
    }
}
