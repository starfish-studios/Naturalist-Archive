package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class BabyHurtByTargetGoal extends HurtByTargetGoal {
    public BabyHurtByTargetGoal(PathfinderMob pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
    }

    @Override
    public void start() {
        super.start();
        if (this.mob.isBaby()) {
            this.alertOthers();
            this.stop();
        }
    }

    @Override
    protected void alertOther(Mob pMob, LivingEntity pTarget) {
        if (!pMob.isBaby()) {
            super.alertOther(pMob, pTarget);
        }
    }
}