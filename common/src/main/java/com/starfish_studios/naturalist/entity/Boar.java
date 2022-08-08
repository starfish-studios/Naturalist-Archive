package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LightningEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.ZoglinEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.Difficulty;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.UUID;
import java.util.function.Predicate;

public class Boar extends AnimalEntity implements Angerable, IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.fromTag(NaturalistTags.ItemTags.BOAR_FOOD_ITEMS);
    private static final UniformIntProvider PERSISTENT_ANGER_TIME = TimeHelper.betweenSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    public Boar(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 14.0).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 1.0D);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld level, PassiveEntity otherParent) {
        return NaturalistEntityTypes.BOAR.get().create(level);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(2, new TemptGoal(this, 1.2, FOOD_ITEMS, false));
        this.goalSelector.add(3, new BoarAvoidPlayerGoal(this, PlayerEntity.class, 16.0f, 1.5D, 1.5D, entity -> !entity.isHolding(FOOD_ITEMS)));
        this.goalSelector.add(4, new BoarMeleeAttackGoal(this, 1.2, false));
        this.goalSelector.add(5, new BabyPanicGoal(this, 1.4));
        this.goalSelector.add(6, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(3, new UniversalAngerGoal<>(this, false));
    }

    public boolean isAggro() {
        return this.getHealth() > this.getMaxHealth() / 2;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            if (!this.isAggro()) {
                this.stopAnger();
            }
            this.tickAngerLogic((ServerWorld)this.world, true);
        }
    }

    @Override
    public void mobTick() {
        if (this.getMoveControl().isMoving()) {
            this.setSprinting(this.getMoveControl().getSpeed() >= 1.2D);
        } else {
            this.setSprinting(false);
        }
        super.mobTick();
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ENTITY_PIG_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ENTITY_PIG_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ENTITY_PIG_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ENTITY_PIG_STEP, 0.15f, 1.0f);
    }

    @Override
    public float getSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 0.5f;
    }

    @Override
    public void onStruckByLightning(ServerWorld level, LightningEntity lightning) {
        if (level.getDifficulty() != Difficulty.PEACEFUL) {
            ZoglinEntity zoglin = EntityType.ZOGLIN.create(level);
            zoglin.refreshPositionAndAngles(this.getX(), this.getY(), this.getZ(), this.getYaw(), this.getPitch());
            zoglin.setAiDisabled(this.isAiDisabled());
            zoglin.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                zoglin.setCustomName(this.getCustomName());
                zoglin.setCustomNameVisible(this.isCustomNameVisible());
            }
            zoglin.setPersistent();
            level.spawnEntity(zoglin);
            this.discard();
        } else {
            super.onStruckByLightning(level, lightning);
        }
    }

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(PERSISTENT_ANGER_TIME.get(this.random));
    }

    @Override
    public void setAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Override
    public int getAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setAngryAt(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    @Nullable
    public UUID getAngryAt() {
        return this.persistentAngerTarget;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getVelocity().horizontalLengthSquared() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("boar.run", true));
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("boar.walk", true));
                event.getController().setAnimationSpeed(1.5D);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("boar.idle", true));
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

    static class BoarMeleeAttackGoal extends MeleeAttackGoal {
        private final Boar boar;

        public BoarMeleeAttackGoal(Boar mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
            this.boar = mob;
        }

        @Override
        public boolean canStart() {
            return this.boar.isAggro() && !this.boar.isBaby() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return this.boar.isAggro() && !this.boar.isBaby() && super.shouldContinue();
        }
    }

    static class BoarAvoidPlayerGoal extends FleeEntityGoal<PlayerEntity> {
        private final Boar boar;

        public BoarAvoidPlayerGoal(Boar mob, Class<PlayerEntity> entityClassToAvoid, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> predicateOnAvoidEntity) {
            super(mob, entityClassToAvoid, maxDistance, walkSpeedModifier, sprintSpeedModifier, predicateOnAvoidEntity);
            this.boar = mob;
        }

        @Override
        public boolean canStart() {
            return !this.boar.isAggro() && !this.boar.isBaby() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !this.boar.isAggro() && !this.boar.isBaby() && super.shouldContinue();
        }

        @Override
        public void start() {
            this.boar.stopAnger();
            super.start();
        }
    }
}
