package com.starfish_studios.naturalist.common.entity.core.ai.goal;

import com.starfish_studios.naturalist.common.entity.core.EggLayingAnimal;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.GameRules;

public class EggLayingBreedGoal<T extends Animal & EggLayingAnimal> extends BreedGoal {
    private final T animal;

    public EggLayingBreedGoal(T animal, double speedModifier) {
        super(animal, speedModifier);
        this.animal = animal;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !this.animal.hasEgg();
    }

    @Override
    protected void breed() {
        ServerPlayer serverPlayer = this.animal.getLoveCause();
        if (serverPlayer == null && this.partner.getLoveCause() != null) {
            serverPlayer = this.partner.getLoveCause();
        }
        if (serverPlayer != null) {
            serverPlayer.awardStat(Stats.ANIMALS_BRED);
            CriteriaTriggers.BRED_ANIMALS.trigger(serverPlayer, this.animal, this.partner, null);
        }
        this.animal.setHasEgg(true);
        this.animal.resetLove();
        this.partner.resetLove();
        RandomSource randomSource = this.animal.getRandom();
        if (this.level.getGameRules().getBoolean(GameRules.RULE_DOMOBLOOT)) {
            this.level.addFreshEntity(new ExperienceOrb(this.level, this.animal.getX(), this.animal.getY(), this.animal.getZ(), randomSource.nextInt(7) + 1));
        }
    }
}
