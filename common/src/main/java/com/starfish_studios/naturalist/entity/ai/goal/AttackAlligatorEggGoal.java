package com.starfish_studios.naturalist.entity.ai.goal;

import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.RemoveBlockGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;

public class AttackAlligatorEggGoal extends RemoveBlockGoal {
    public AttackAlligatorEggGoal(Block block, PathfinderMob pathfinderMob, double d, int i) {
        super(NaturalistRegistry.ALLIGATOR_EGG.get(), pathfinderMob, d, i);
    }
    public void playDestroyProgressSound(LevelAccessor level, BlockPos pos) {
        level.playSound((Player)null, pos, SoundEvents.ZOMBIE_DESTROY_EGG, SoundSource.HOSTILE, 0.5F, 0.9F + level.getRandom().nextFloat() * 0.2F);
    }

    public void playBreakSound(Level level, BlockPos pos) {
        level.playSound((Player)null, pos, SoundEvents.TURTLE_EGG_BREAK, SoundSource.BLOCKS, 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
    }

    public double acceptedDistance() {
        return 1.14;
    }
}
