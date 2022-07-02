package com.starfish_studios.naturalist.entity.ai.navigation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class BetterGroundPathNavigation extends GroundPathNavigation {
    public BetterGroundPathNavigation(Mob mob, Level level) {
        super(mob, level);
    }

    @Override
    protected void followThePath() {
        boolean shouldAdvance;
        Vec3 tempMobPos = this.getTempMobPos();
        this.maxDistanceToWaypoint = this.mob.getBbWidth() * 0.75F;
        BlockPos nextPos = this.path.getNextNodePos();
        double x = Math.abs(this.mob.getX() - ((double)nextPos.getX() + 0.5));
        double y = Math.abs(this.mob.getY() - (double)nextPos.getY());
        double z = Math.abs(this.mob.getZ() - ((double)nextPos.getZ() + 0.5));
        shouldAdvance = x < (double) this.maxDistanceToWaypoint && z < (double) this.maxDistanceToWaypoint && y < 1.0;
        if (shouldAdvance || this.mob.canCutCorner(this.path.getNextNode().type) && this.shouldTargetNextNodeInDirection(tempMobPos)) {
            this.path.advance();
        }
        this.doStuckDetection(tempMobPos);
    }

    private boolean shouldTargetNextNodeInDirection(Vec3 currentPos) {
        if (this.path.getNextNodeIndex() + 1 >= this.path.getNodeCount()) {
            return false;
        }
        Vec3 nextPos = Vec3.atBottomCenterOf(this.path.getNextNodePos());
        if (!currentPos.closerThan(nextPos, 2.0)) {
            return false;
        }
        Vec3 nextPos2 = Vec3.atBottomCenterOf(this.path.getNodePos(this.path.getNextNodeIndex() + 1));
        Vec3 difference = nextPos2.subtract(nextPos);
        return difference.dot(currentPos.subtract(nextPos)) > 0.0;
    }
}
