package com.starfish_studios.naturalist.entity.ai.goal;

import net.minecraft.entity.ai.goal.EscapeDangerGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class BabyPanicGoal extends EscapeDangerGoal {
    public BabyPanicGoal(PathAwareEntity pMob, double pSpeedModifier) {
        super(pMob, pSpeedModifier);
    }

    @Override
    protected boolean isInDanger() {
        return mob.getAttacker() != null && mob.isBaby() || mob.isOnFire();
    }
}
