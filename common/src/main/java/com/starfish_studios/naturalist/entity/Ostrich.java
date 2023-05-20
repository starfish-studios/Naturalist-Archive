package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.AlertOthersPanicGoal;
import com.starfish_studios.naturalist.entity.ai.goal.RunAroundLikeCrazyGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.List;

public class Ostrich extends Animal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int panicTicks = 0;

    public Ostrich(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 2.0F;
    }

    // GOALS/ATTRIBUTES/BREEDING

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return NaturalistEntityTypes.OSTRICH.get().create(level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AlertOthersPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.6));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.25D, Ingredient.of(Items.APPLE), true));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.5D, 2.0D, livingEntity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isDiscrete()));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Monster.class, 4.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(4, new AvoidEntityGoal<>(this, Animal.class, 10.0F, 1.5D, 2.0D, livingEntity -> livingEntity.getType().is(NaturalistTags.EntityTypes.OSTRICH_PREDATORS)));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }



    public void makeMad() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_ANGRY, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        // return this.isBaby() ? NaturalistSoundEvents.DEER_HURT_BABY.get() : NaturalistSoundEvents.DEER_HURT.get();
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        // return this.isBaby() ? NaturalistSoundEvents.DEER_AMBIENT_BABY.get() : NaturalistSoundEvents.DEER_AMBIENT.get();
        return null;
    }
    @Override
    public float getVoicePitch() {
        return this.isBaby() ? super.getVoicePitch() * 0.65F : super.getVoicePitch();
    }
    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.APPLE);
    }

    // EATING

    @Override
    public void aiStep() {
        super.aiStep();
    }

    protected void doPlayerRide(Player player) {
        if (!this.level.isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (passenger instanceof Mob mob) {
            this.yBodyRot = mob.yBodyRot;
        }
        passenger.setPos(this.getX(), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.getZ());
        if (passenger instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 1.15;
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (!this.isBaby()) {
            if (this.isVehicle()) {
                return super.mobInteract(player, hand);
            }
        }

        if (!itemStack.isEmpty()) {
            InteractionResult interactionResult = itemStack.interactLivingEntity(player, this, hand);
            if (interactionResult.consumesAction()) {
                return interactionResult;
            }
                this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_ANGRY, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
        }

        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        } else {
            this.doPlayerRide(player);
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
    }

    @Override
    public void handleEntityEvent(byte pId) {
            super.handleEntityEvent(pId);
    }

    @Override
    public void ate() {
        if (this.isBaby()) {
            this.ageUp(60);
        }
    }

    // MOVEMENT

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.5D);
        } else {
            this.setSprinting(false);
        }
        super.customServerAiStep();
    }

    // PANICKING

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean lastHurt = super.hurt(pSource, pAmount);
        if (lastHurt) {
            int ticks = 100 + this.random.nextInt(100);
            this.panicTicks = ticks;
            List<? extends Ostrich> ostriches = this.level.getEntitiesOfClass(Ostrich.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            for (Ostrich ostrich : ostriches) {
                ostrich.panicTicks = ticks;
            }
        }
        return lastHurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (panicTicks >= 0) {
                panicTicks--;
            }
            if (panicTicks == 0 && this.getLastHurtByMob() != null) {
                this.setLastHurtByMob(null);
            }
        }
    }

    // ANIMATION

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().loop("run"));
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop("walk"));
                event.getController().setAnimationSpeed(1.0D);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("idle"));
            event.getController().setAnimationSpeed(0.5D);
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5);
        data.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
