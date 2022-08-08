package com.starfish_studios.naturalist.entity.ai.goal;

import org.jetbrains.annotations.Nullable;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.mob.MobEntity;

public class FollowAdultGoal extends Goal {
    protected final MobEntity mob;
    protected final Predicate<MobEntity> followPredicate;
    @Nullable
    protected MobEntity followingMob;
    protected final double speedModifier;
    protected final EntityNavigation navigation;
    protected int timeToRecalcPath;
    protected final float stopDistance;
    protected float oldWaterCost;
    protected final float areaSize;

    public FollowAdultGoal(MobEntity mob, double speedModifier, float stopDistance, float areaSize) {
        this.mob = mob;
        this.followPredicate = followingMob -> followingMob != null && !followingMob.isBaby();
        this.speedModifier = speedModifier;
        this.navigation = mob.getNavigation();
        this.stopDistance = stopDistance;
        this.areaSize = areaSize;
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
    }

    @Override
    public boolean canStart() {
        List<? extends MobEntity> list = this.mob.world.getEntitiesByClass(this.mob.getClass(), this.mob.getBoundingBox().expand(this.areaSize), this.followPredicate);
        if (!list.isEmpty()) {
            for (MobEntity mob : list) {
                if (mob.isBaby()) continue;
                if (mob.isInvisible()) continue;
                this.followingMob = mob;
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean shouldContinue() {
        return this.followingMob != null && !this.navigation.isIdle() && this.mob.squaredDistanceTo(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
    }

    @Override
    public void start() {
        this.timeToRecalcPath = 0;
        this.oldWaterCost = this.mob.getPathfindingPenalty(PathNodeType.WATER);
        this.mob.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
    }

    @Override
    public void stop() {
        this.followingMob = null;
        this.navigation.stop();
        this.mob.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterCost);
    }

    @Override
    public void tick() {
        double z;
        double y;
        if (this.followingMob == null || this.mob.isLeashed()) {
            return;
        }
        this.mob.getLookControl().lookAt(this.followingMob, 10.0f, this.mob.getMaxLookPitchChange());
        if (--this.timeToRecalcPath > 0) {
            return;
        }
        this.timeToRecalcPath = this.getTickCount(10);
        double x = this.mob.getX() - this.followingMob.getX();
        double distanceToMob = x * x + (y = this.mob.getY() - this.followingMob.getY()) * y + (z = this.mob.getZ() - this.followingMob.getZ()) * z;
        if (distanceToMob <= (double)(this.stopDistance * this.stopDistance)) {
            this.navigation.stop();
            LookControl lookControl = this.followingMob.getLookControl();
            if (distanceToMob <= (double)this.stopDistance || lookControl.getLookX() == this.mob.getX() && lookControl.getLookY() == this.mob.getY() && lookControl.getLookZ() == this.mob.getZ()) {
                double h = this.followingMob.getX() - this.mob.getX();
                double i = this.followingMob.getZ() - this.mob.getZ();
                this.navigation.startMovingTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speedModifier);
            }
            return;
        }
        this.navigation.startMovingTo(this.followingMob, this.speedModifier);
    }
}
