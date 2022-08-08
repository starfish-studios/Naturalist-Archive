package com.starfish_studios.naturalist.entity.ai.navigation;

import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;

public class BetterGroundPathNavigation extends MobNavigation {
    public BetterGroundPathNavigation(MobEntity mob, World level) {
        super(mob, level);
    }

    @Override
    protected void continueFollowingPath() {
        boolean shouldAdvance;
        Vec3d tempMobPos = this.getPos();
        this.nodeReachProximity = this.entity.getWidth() * 0.75F;
        BlockPos nextPos = this.currentPath.getCurrentNodePos();
        double x = Math.abs(this.entity.getX() - ((double)nextPos.getX() + 0.5));
        double y = Math.abs(this.entity.getY() - (double)nextPos.getY());
        double z = Math.abs(this.entity.getZ() - ((double)nextPos.getZ() + 0.5));
        shouldAdvance = x < (double) this.nodeReachProximity && z < (double) this.nodeReachProximity && y < 1.0;
        if (shouldAdvance || this.entity.canJumpToNextPathNode(this.currentPath.getCurrentNode().type) && this.shouldJumpToNextNode(tempMobPos)) {
            this.currentPath.next();
        }
        this.checkTimeouts(tempMobPos);
    }

    private boolean shouldJumpToNextNode(Vec3d currentPos) {
        if (this.currentPath.getCurrentNodeIndex() + 1 >= this.currentPath.getLength()) {
            return false;
        }
        Vec3d nextPos = Vec3d.ofBottomCenter(this.currentPath.getCurrentNodePos());
        if (!currentPos.isInRange(nextPos, 2.0)) {
            return false;
        }
        Vec3d nextPos2 = Vec3d.ofBottomCenter(this.currentPath.getNodePos(this.currentPath.getCurrentNodeIndex() + 1));
        Vec3d difference = nextPos2.subtract(nextPos);
        return difference.dotProduct(currentPos.subtract(nextPos)) > 0.0;
    }
}
