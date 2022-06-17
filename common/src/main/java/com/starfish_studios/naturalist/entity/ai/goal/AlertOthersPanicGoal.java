package com.starfish_studios.naturalist.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.FuzzyTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import java.util.EnumSet;
import java.util.List;

public class AlertOthersPanicGoal extends Goal {
    protected final PathAwareEntity mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public AlertOthersPanicGoal(PathAwareEntity mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setControls(EnumSet.of(Control.MOVE));
    }

    @Override
    public boolean canStart() {
        if (this.mob.getAttacker() == null || !this.mob.getAttacker().isAlive()) {
            return false;
        } else {
            if (this.mob.isOnFire()) {
                BlockPos blockpos = this.lookForWater(this.mob.world, this.mob, 5);
                if (blockpos != null) {
                    this.posX = blockpos.getX();
                    this.posY = blockpos.getY();
                    this.posZ = blockpos.getZ();
                    return true;
                }
            }
            if (this.mob.getAttacker() != null) {
                List<? extends PathAwareEntity> mobs = this.mob.world.getNonSpectatingEntities(this.mob.getClass(), Box.from(this.mob.getPos()).expand(8.0D, 8.0D, 8.0D));
                for (PathAwareEntity mob : mobs) {
                    mob.setAttacker(this.mob.getAttacker());
                }
                return this.findRandomPosAway(this.mob.getAttacker());
            }
            return this.findRandomPosition();
        }
    }

    protected boolean findRandomPosAway(LivingEntity entity) {
        Vec3d vec3 = FuzzyTargeting.findFrom(this.mob, 16, 7, entity.getPos());
        if (vec3 == null) {
            return false;
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }
    }

    protected boolean findRandomPosition() {
        Vec3d vec3 = FuzzyTargeting.find(this.mob, 5, 4);
        if (vec3 == null) {
            return false;
        } else {
            this.posX = vec3.x;
            this.posY = vec3.y;
            this.posZ = vec3.z;
            return true;
        }
    }

    public boolean isRunning() {
        return this.isRunning;
    }

    @Override
    public void start() {
        this.mob.getNavigation().startMovingTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean shouldContinue() {
        return !this.mob.getNavigation().isIdle();
    }

    @Nullable
    protected BlockPos lookForWater(BlockView pLevel, Entity pEntity, int pRange) {
        BlockPos blockpos = pEntity.getBlockPos();
        return !pLevel.getBlockState(blockpos).getCollisionShape(pLevel, blockpos).isEmpty() ? null : BlockPos.findClosest(pEntity.getBlockPos(), pRange, 1, (fluidState) -> pLevel.getFluidState(fluidState).isIn(FluidTags.WATER)).orElse(null);
    }
}
