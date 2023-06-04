package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.LookControl;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class FollowAdultGoal extends Goal {
    protected final Mob mob;
    protected final Predicate<Mob> followPredicate;
    @Nullable
    protected Mob followingMob;
    protected final double speedModifier;
    protected final PathNavigation navigation;
    protected int timeToRecalcPath;
    protected final float stopDistance;
    protected float oldWaterCost;
    protected final float areaSize;

    public FollowAdultGoal(Mob mob, double speedModifier, float stopDistance, float areaSize) {
        this.mob = mob;
        this.followPredicate = followingMob -> followingMob != null && !followingMob.isBaby();
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        List<? extends Mob> list = this.mob.level.getEntitiesOfClass(this.mob.getClass(), this.mob.getBoundingBox().inflate(this.areaSize), this.followPredicate);
        if (!list.isEmpty()) {
            for (Mob mob : list) {
                if (mob.isBaby()) continue;
                if (mob.isInvisible()) continue;
                this.followingMob = mob;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.followingMob != null && !this.navigation.isDone() && this.mob.distanceToSqr(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingMalus(BlockPathTypes.WATER);
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.followingMob = null;
        this.navigation.stop();
        this.mob.setPathfindingMalus(BlockPathTypes.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        double z;
        double y;
        if (this.followingMob == null || this.mob.isLeashed()) {
            return;
        }
        this.mob.getLookControl().setLookAt(this.followingMob, 10.0f, this.mob.getMaxHeadXRot());
        if (--this.timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = this.adjustedTickDelay(10);
        double x = this.mob.getX() - this.followingMob.getX();
        double distanceToMob = x * x + (y = this.mob.getY() - this.followingMob.getY()) * y + (z = this.mob.getZ() - this.followingMob.getZ()) * z;
        if (distanceToMob <= (double)(this.stopDistance * this.stopDistance)) {
            this.navigation.stop();
            LookControl lookControl = this.followingMob.getLookControl();
            if (distanceToMob <= (double)this.stopDistance || lookControl.getWantedX() == this.mob.getX() && lookControl.getWantedY() == this.mob.getY() && lookControl.getWantedZ() == this.mob.getZ()) {
                double h = this.followingMob.getX() - this.mob.getX();
                double i = this.followingMob.getZ() - this.mob.getZ();
                this.navigation.moveTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speedModifier);
            }
            return;
        }
        this.navigation.moveTo(this.followingMob, this.speedModifier);
    }
}
