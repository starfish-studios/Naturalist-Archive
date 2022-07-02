package com.starfish_studios.naturalist.entity.ai.goal;

import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.player.Player;

public class AttackPlayerNearBabiesGoal extends NearestAttackableTargetGoal<Player> {
    private final float followDistanceMultiplier;

    public AttackPlayerNearBabiesGoal(Mob pMob, float followDistanceMultiplier) {
        super(pMob, Player.class, 10, true, true, EntitySelector.NO_CREATIVE_OR_SPECTATOR::test);
        this.followDistanceMultiplier = followDistanceMultiplier;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isBaby()) {
            return false;
        }
        if (super.canUse()) {
            for (Mob mob : this.mob.level.getEntitiesOfClass(mob.getClass(), this.mob.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                if (mob.isBaby()) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected double getFollowDistance() {
        return super.getFollowDistance() * this.followDistanceMultiplier;
    }
}