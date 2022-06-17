package com.starfish_studios.naturalist.entity.ai.goal;

import com.starfish_studios.naturalist.entity.SleepingAnimal;
import java.util.EnumSet;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.mob.PathAwareEntity;

public class SleepGoal<E extends PathAwareEntity & SleepingAnimal> extends Goal {
    private final E mob;

    public SleepGoal(E mob) {
        this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.JUMP));
        this.mob = mob;
    }

    @Override
    public boolean canStart() {
        if (mob.sidewaysSpeed == 0.0F && mob.upwardSpeed == 0.0F && mob.forwardSpeed == 0.0F) {
            return mob.canSleep() || mob.isSleeping();
        } else {
            return false;
        }
    }

    @Override
    public boolean shouldContinue() {
        return mob.canSleep();
    }

    @Override
    public void start() {
        mob.setJumping(false);
        mob.setSleeping(true);
        mob.getNavigation().stop();
        mob.getMoveControl().moveTo(mob.getX(), mob.getY(), mob.getZ(), 0.0D);
    }

    @Override
    public void stop() {
        mob.setSleeping(false);
    }
}
