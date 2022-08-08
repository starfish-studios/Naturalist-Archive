package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.AlertOthersPanicGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.EatGrassGoal;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.List;

public class Deer extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private int panicTicks = 0;
    private int eatAnimationTick;
    private EatGrassGoal eatBlockGoal;

    public Deer(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.stepHeight = 2.0F;
    }

    // GOALS/ATTRIBUTES/BREEDING

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld level, PassiveEntity mob) {
        return NaturalistEntityTypes.DEER.get().create(level);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 8.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AlertOthersPanicGoal(this, 2.0D));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(3, new TemptGoal(this, 1.25D, Ingredient.ofItems(Items.APPLE), true));
        this.goalSelector.add(4, new FleeEntityGoal<>(this, PlayerEntity.class, 8.0F, 1.5D, 2.0D, livingEntity -> EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isSneaky()));
        this.goalSelector.add(4, new FleeEntityGoal<>(this, HostileEntity.class, 4.0F, 1.5D, 2.0D));
        this.goalSelector.add(4, new FleeEntityGoal<>(this, AnimalEntity.class, 10.0F, 1.5D, 2.0D, livingEntity -> livingEntity.getType().isIn(NaturalistTags.EntityTypes.DEER_PREDATORS)));
        this.goalSelector.add(5, new FollowParentGoal(this, 1.25D));
        this.eatBlockGoal = new EatGrassGoal(this);
        this.goalSelector.add(6, this.eatBlockGoal);
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return this.isBaby() ? NaturalistSoundEvents.DEER_HURT_BABY.get() : NaturalistSoundEvents.DEER_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.DEER_AMBIENT_BABY.get() : NaturalistSoundEvents.DEER_AMBIENT.get();
    }

    @Override
    public float getSoundPitch() {
        return this.isBaby() ? super.getSoundPitch() * 0.65F : super.getSoundPitch();
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return pStack.isOf(Items.APPLE);
    }

    // EATING

    @Override
    public void tickMovement() {
        if (this.world.isClient) {
            this.eatAnimationTick = Math.max(0, this.eatAnimationTick - 1);
        }
        super.tickMovement();
    }

    @Override
    public void handleStatus(byte pId) {
        if (pId == 10) {
            this.eatAnimationTick = 40;
        } else {
            super.handleStatus(pId);
        }
    }

    public boolean isEating() {
        return this.eatAnimationTick > 0;
    }

    @Override
    public void onEatingGrass() {
        if (this.isBaby()) {
            this.growUp(60);
        }
    }

    // MOVEMENT

    @Override
    public void mobTick() {
        this.eatAnimationTick = this.eatBlockGoal.getTimer();
        if (this.getMoveControl().isMoving()) {
            this.setSprinting(this.getMoveControl().getSpeed() >= 1.5D);
        } else {
            this.setSprinting(false);
        }
        super.mobTick();
    }

    // PANICKING

    @Override
    public boolean damage(DamageSource pSource, float pAmount) {
        boolean lastHurt = super.damage(pSource, pAmount);
        if (lastHurt) {
            int ticks = 100 + this.random.nextInt(100);
            this.panicTicks = ticks;
            List<? extends Deer> deers = this.world.getNonSpectatingEntities(Deer.class, this.getBoundingBox().expand(8.0D, 4.0D, 8.0D));
            for (Deer deer : deers) {
                deer.panicTicks = ticks;
            }
        }
        return lastHurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            if (panicTicks >= 0) {
                panicTicks--;
            }
            if (panicTicks == 0 && this.getAttacker() != null) {
                this.setAttacker(null);
            }
        }
    }

    // ANIMATION

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("deer.eat", true));
            event.getController().setAnimationSpeed(1.0D);
        } else if (this.getVelocity().horizontalLengthSquared() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("deer.run", true));
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("deer.walk", true));
                event.getController().setAnimationSpeed(1.25D);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("deer.idle", true));
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
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
}
