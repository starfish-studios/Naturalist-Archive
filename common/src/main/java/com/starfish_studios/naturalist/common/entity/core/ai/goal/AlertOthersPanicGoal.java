package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class AlertOthersPanicGoal extends Goal {
    protected final PathfinderMob mob;
    protected final double speedModifier;
    protected double posX;
    protected double posY;
    protected double posZ;
    protected boolean isRunning;

    public AlertOthersPanicGoal(PathfinderMob mob, double speedModifier) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.setFlags(EnumSet.of(Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if (this.mob.getLastHurtByMob() == null || !this.mob.getLastHurtByMob().isAlive()) {
            return false;
        } else {
            if (this.mob.isOnFire()) {
                BlockPos blockpos = this.lookForWater(this.mob.level, this.mob, 5);
                if (blockpos != null) {
                    this.posX = blockpos.getX();
                    this.posY = blockpos.getY();
                    this.posZ = blockpos.getZ();
                    return true;
                }
            }
            if (this.mob.getLastHurtByMob() != null) {
                List<? extends PathfinderMob> mobs = this.mob.level.getEntitiesOfClass(this.mob.getClass(), AABB.unitCubeFromLowerCorner(this.mob.position()).inflate(8.0D, 8.0D, 8.0D));
                for (PathfinderMob mob : mobs) {
                    mob.setLastHurtByMob(this.mob.getLastHurtByMob());
                }
                return this.findRandomPosAway(this.mob.getLastHurtByMob());
            }
            return this.findRandomPosition();
        }
    }

    protected boolean findRandomPosAway(LivingEntity entity) {
        Vec3 vec3 = LandRandomPos.getPosAway(this.mob, 16, 7, entity.position());
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
        Vec3 vec3 = LandRandomPos.getPos(this.mob, 5, 4);
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
        this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        this.isRunning = true;
    }

    @Override
    public void stop() {
        this.isRunning = false;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone();
    }

    @Nullable
    protected BlockPos lookForWater(BlockGetter pLevel, Entity pEntity, int pRange) {
        BlockPos blockpos = pEntity.blockPosition();
        return !pLevel.getBlockState(blockpos).getCollisionShape(pLevel, blockpos).isEmpty() ? null : BlockPos.findClosestMatch(pEntity.blockPosition(), pRange, 1, (fluidState) -> pLevel.getFluidState(fluidState).is(FluidTags.WATER)).orElse(null);
    }
}
