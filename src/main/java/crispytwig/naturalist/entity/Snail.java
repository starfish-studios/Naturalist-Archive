package crispytwig.naturalist.entity;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;

public class Snail extends Animal implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);

    public Snail(EntityType<? extends Animal> type, Level level) {
        super(type, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 2.0D).add(Attributes.MOVEMENT_SPEED, 0.06F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new HideGoal(this));
        this.goalSelector.addGoal(1, new SnailStrollGoal(this, 1.0D, 0.0F));
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return null;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        for (Player player : level.getEntitiesOfClass(Player.class, this.getBoundingBox())) {
            if (!player.isOnGround()) {
                this.hurt(DamageSource.playerAttack(player), 5.0F);
                level.playSound(null, this.blockPosition(), SoundEvents.TURTLE_EGG_CRACK, SoundSource.NEUTRAL, 1.0F, 1.2F);
            }
        }
    }

    private boolean canHide() {
        List<Player> players = this.level.getNearbyPlayers(TargetingConditions.forNonCombat().range(5.0D), this, this.getBoundingBox().inflate(5.0D, 3.0D, 5.0D));
        return !players.isEmpty();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.canHide()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snail.retreat", true));
            return PlayState.CONTINUE;
        } else if (!(event.getLimbSwingAmount() > -0.01F && event.getLimbSwingAmount() < 0.01F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snail.move", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class HideGoal extends Goal {
        private final Snail snail;

        public HideGoal(Snail mob) {
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.JUMP));
            this.snail = mob;
        }

        @Override
        public boolean canUse() {
            return snail.canHide();
        }

        @Override
        public void start() {
            snail.setJumping(false);
            snail.getNavigation().stop();
            snail.getMoveControl().setWantedPosition(snail.getX(), snail.getY(), snail.getZ(), 0.0D);
        }
    }

    static class SnailStrollGoal extends WaterAvoidingRandomStrollGoal {
        public SnailStrollGoal(PathfinderMob pMob, double pSpeedModifier, float pProbability) {
            super(pMob, pSpeedModifier, pProbability);
            this.forceTrigger = true;
            this.interval = 1;
        }
    }
}
