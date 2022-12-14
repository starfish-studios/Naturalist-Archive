package com.starfish_studios.naturalist.entity.ai.goal;

import com.starfish_studios.naturalist.entity.HidingAnimal;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class HideGoal<T extends Mob & HidingAnimal> extends Goal {
    private final T mob;

    public HideGoal(T mob) {
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        return mob.canHide();
    }

    @Override
    public void start() {
        mob.setJumping(false);
        mob.getNavigation().stop();
        mob.getMoveControl().setWantedPosition(mob.getX(), mob.getY(), mob.getZ(), 0.0D);
    }
}