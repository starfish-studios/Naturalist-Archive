package com.starfish_studios.naturalist.entity.ai.goal;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;

public class BabyHurtByTargetGoal extends RevengeGoal {
    public BabyHurtByTargetGoal(PathAwareEntity pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
    }

    @Override
    public void start() {
        super.start();
        if (this.mob.isBaby()) {
            this.callSameTypeForRevenge();
            this.stop();
        }
    }

    @Override
    protected void setMobEntityTarget(MobEntity pMob, LivingEntity pTarget) {
        if (!pMob.isBaby()) {
            super.setMobEntityTarget(pMob, pTarget);
        }
    }
}