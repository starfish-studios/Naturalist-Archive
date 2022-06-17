package com.starfish_studios.naturalist.entity.ai.goal;

import javax.annotation.Nullable;
import net.minecraft.entity.ai.AboveGroundTargeting;
import net.minecraft.entity.ai.NoPenaltySolidTargeting;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import java.util.EnumSet;

public class FlyingWanderGoal extends Goal {
    private final PathAwareEntity mob;

    public FlyingWanderGoal(PathAwareEntity mob) {
        this.setControls(EnumSet.of(Control.MOVE));
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        return mob.getNavigation().isIdle() && mob.getRandom().nextInt(10) == 0;
    }

    @Override
    public boolean shouldContinue() {
        return mob.getNavigation().isFollowingPath();
    }

    @Override
    public void start() {
        Vec3d vec3 = this.findPos();
        if (vec3 != null) {
            mob.getNavigation().startMovingAlong(mob.getNavigation().findPathTo(new BlockPos(vec3), 1), 1.0D);
        }

    }

    @Nullable
    private Vec3d findPos() {
        Vec3d viewVector = mob.getRotationVec(0.0F);
        Vec3d hoverPos = AboveGroundTargeting.find(mob, 8, 7, viewVector.x, viewVector.z, ((float) Math.PI / 2F), 3, 1);
        return hoverPos != null ? hoverPos : NoPenaltySolidTargeting.find(mob, 8, 4, -2, viewVector.x, viewVector.z, (float) Math.PI / 2F);
    }
}
