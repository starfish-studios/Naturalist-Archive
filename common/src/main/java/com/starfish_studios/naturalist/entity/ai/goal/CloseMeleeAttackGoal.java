package com.starfish_studios.naturalist.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.mob.PathAwareEntity;

public class CloseMeleeAttackGoal extends MeleeAttackGoal {
    public CloseMeleeAttackGoal(PathAwareEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
    }

    @Override
    protected double getSquaredMaxAttackDistance(LivingEntity pAttackTarget) {
        return 4.0F + pAttackTarget.getWidth();
    }
}
