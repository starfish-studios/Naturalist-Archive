package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.AttackPlayerNearBabiesGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.entity.ai.goal.DistancedFollowParentGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.MobNavigation;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.BeeEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluids;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Elephant extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> DIRTY_TICKS = DataTracker.registerData(Elephant.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> DRINKING = DataTracker.registerData(Elephant.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Nullable
    protected BlockPos waterPos;

    public Elephant(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.stepHeight = 1.0f;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 50.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.75D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D);
    }

    @Override
    public EntityData initialize(ServerWorldAccess level, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData spawnData, @Nullable NbtCompound dataTag) {
        PassiveData ageableMobGroupData;
        if (spawnData == null) {
            spawnData = new PassiveData(true);
        }
        if ((ageableMobGroupData = (PassiveData)spawnData).getSpawnedCount() > 1) {
            this.setBreedingAge(-24000);
        }
        ageableMobGroupData.countSpawned();
        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        return spawnData;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverLevel, PassiveEntity ageableMob) {
        return NaturalistEntityTypes.ELEPHANT.get().create(serverLevel);
    }

    @Override
    protected EntityNavigation createNavigation(World level) {
        return new MobNavigation(this, level);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new FleeEntityGoal<>(this, BeeEntity.class, 8.0f, 1.5, 1.5));
        this.goalSelector.add(2, new ElephantMeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(3, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.add(4, new DistancedFollowParentGoal(this, 1.25D, 24.0D, 6.0D, 12.0D));
        this.goalSelector.add(5, new ElephantDrinkWaterGoal(this));
        this.goalSelector.add(6, new ElephantMoveToWaterGoal(this, 1.0D, 8, 4));
        this.goalSelector.add(7, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.targetSelector.add(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.add(2, new AttackPlayerNearBabiesGoal(this, 0.5F));
    }

    @Override
    public int getMaxHeadRotation() {
        return 35;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return NaturalistSoundEvents.ELEPHANT_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.ELEPHANT_AMBIENT.get();
    }

    @Override
    public boolean tryAttack(Entity target) {
        boolean shouldHurt = target.damage(DamageSource.mob(this), (float) this.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
        if (shouldHurt && target instanceof LivingEntity livingEntity) {
            Vec3d knockbackDirection = new Vec3d(this.getBlockPos().getX() - target.getX(), 0.0, this.getBlockPos().getZ() - target.getZ()).normalize();
            float shieldBlockModifier = livingEntity.blockedByShield(DamageSource.mob(this)) ? 0.5f : 1.0f;
            livingEntity.takeKnockback(shieldBlockModifier * 3.0D, knockbackDirection.getX(), knockbackDirection.getZ());
            double knockbackResistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
            livingEntity.setVelocity(livingEntity.getVelocity().add(0.0, 0.5f * knockbackResistance, 0.0));
        }
        this.playSound(SoundEvents.ENTITY_RAVAGER_ATTACK, 1.0f, 1.0f);
        return shouldHurt;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(DIRTY_TICKS, 0);
        this.dataTracker.startTracking(DRINKING, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound pCompound) {
        super.writeCustomDataToNbt(pCompound);
        pCompound.putInt("DirtyTicks", this.getDirtyTicks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.setDirtyTicks(pCompound.getInt("DirtyTicks"));
    }

    public void setDirtyTicks(int ticks) {
        this.dataTracker.set(DIRTY_TICKS, ticks);
    }

    public int getDirtyTicks() {
        return this.dataTracker.get(DIRTY_TICKS);
    }

    public boolean isDirty() {
        return this.getDirtyTicks() > 0;
    }

    public void setDrinking(boolean drinking) {
        this.dataTracker.set(DRINKING, drinking);
    }

    public boolean isDrinking() {
        return this.dataTracker.get(DRINKING);
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.world instanceof ServerWorld serverLevel) {
            if (this.isDirty()) {
                this.setDirtyTicks(this.isTouchingWater() ? 0 : Math.max(0, this.getDirtyTicks() - 1));
            } else {
                long dayTime = serverLevel.getTimeOfDay();
                if (dayTime > 4300 && dayTime < 11000 && this.isOnGround() && this.getRandom().nextFloat() < 0.001f && !this.isDrinking()) {
                    this.swingHand(Hand.MAIN_HAND);
                    this.setDirtyTicks(1000);
                    serverLevel.spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.DIRT.getDefaultState()), this.getX(), this.getY(), this.getZ(),
                            200, 0.5, 3.0, 0.5, 10);
                }
            }
        }
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getVelocity().horizontalLengthSquared() > 1.0E-6) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("elephant.walk", true));
        } else if (this.isDrinking()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("elephant.water", true));
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("elephant.idle", true));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState swingPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("elephant.swing", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "swingController", 0, this::swingPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class ElephantMeleeAttackGoal extends MeleeAttackGoal {
        public ElephantMeleeAttackGoal(PathAwareEntity pathfinderMob, double speedMultiplier, boolean followingTargetEvenIfNotSeen) {
            super(pathfinderMob, speedMultiplier, followingTargetEvenIfNotSeen);
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity attackTarget) {
            return MathHelper.square(this.mob.getWidth());
        }
    }

    static class ElephantMoveToWaterGoal extends MoveToTargetPosGoal {
        private final Elephant elephant;

        public ElephantMoveToWaterGoal(Elephant pathfinderMob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(pathfinderMob, speedModifier, searchRange, verticalSearchRange);
            this.elephant = pathfinderMob;
        }

        @Override
        public boolean canStart() {
            return !this.elephant.isBaby() && !this.elephant.isDrinking() && this.elephant.waterPos == null && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !this.hasReached() && super.shouldContinue();
        }

        @Override
        protected boolean isTargetPos(WorldView level, BlockPos pos) {
            if (level.getBlockState(pos).isOf(Blocks.GRASS_BLOCK)) {
                for (Direction direction : Direction.Type.HORIZONTAL) {
                    if (level.getFluidState(pos.offset(direction)).isOf(Fluids.WATER)) {
                        this.elephant.waterPos = pos.offset(direction);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public double getDesiredDistanceToTarget() {
            return 2.5D;
        }

        @Override
        public void stop() {
            this.elephant.setDrinking(true);
            super.stop();
        }
    }

    static class ElephantDrinkWaterGoal extends Goal {
        private final Elephant elephant;
        private int drinkTicks;

        public ElephantDrinkWaterGoal(Elephant elephant) {
            this.elephant = elephant;
            this.setControls(EnumSet.of(Control.MOVE, Control.JUMP, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            if (this.elephant.waterPos == null || this.elephant.squaredDistanceTo(Vec3d.ofCenter(this.elephant.waterPos)) > 15) {
                this.elephant.setDrinking(false);
                return false;
            }
            return this.elephant.isDrinking();
        }

        @Override
        public boolean shouldContinue() {
            return this.drinkTicks > 0 && super.shouldContinue();
        }

        @Override
        public void start() {
            this.drinkTicks = 150;
            if (this.elephant.waterPos != null) {
                this.elephant.getLookControl().lookAt(Vec3d.ofCenter(this.elephant.waterPos));
            }
        }

        @Override
        public void tick() {
            this.drinkTicks--;
            if (this.elephant.waterPos != null) {
                this.elephant.getLookControl().lookAt(Vec3d.ofCenter(this.elephant.waterPos));
            }
        }

        @Override
        public void stop() {
            this.elephant.waterPos = null;
            this.elephant.setDrinking(false);
        }
    }
}
