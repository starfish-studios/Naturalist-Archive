package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import com.starfish_studios.naturalist.common.entity.core.SleepingAnimal;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class SleepGoal<E extends PathfinderMob & SleepingAnimal> extends Goal {
    private final E mob;

    public SleepGoal(E mob) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (mob.xxa == 0.0F && mob.yya == 0.0F && mob.zza == 0.0F) {
            return mob.canSleep() || mob.isSleeping();
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return mob.canSleep();
    }

    @Override
    public void start() {
        mob.setJumping(false);
        mob.setSleeping(true);
        mob.getNavigation().stop();
        mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), 0.0D);
    }

    @Override
    public void stop() {
        mob.setSleeping(false);
    }
}
