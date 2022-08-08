package com.starfish_studios.naturalist.entity.ai.goal;

import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.predicate.entity.EntityPredicates;

public class AttackPlayerNearBabiesGoal extends ActiveTargetGoal<PlayerEntity> {
    private final float followDistanceMultiplier;

    public AttackPlayerNearBabiesGoal(MobEntity pMob, float followDistanceMultiplier) {
        super(pMob, PlayerEntity.class, 10, true, true, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
        this.followDistanceMultiplier = followDistanceMultiplier;
    }

    @Override
    public boolean canStart() {
        if (this.mob.isBaby()) {
            return false;
        }
        if (super.canStart()) {
            for (MobEntity mob : this.mob.world.getNonSpectatingEntities(mob.getClass(), this.mob.getBoundingBox().expand(8.0D, 4.0D, 8.0D))) {
                if (mob.isBaby()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected double getFollowRange() {
        return super.getFollowRange() * this.followDistanceMultiplier;
    }
}