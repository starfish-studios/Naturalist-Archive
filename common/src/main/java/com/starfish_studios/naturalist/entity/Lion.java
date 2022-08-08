package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.entity.ai.goal.SleepGoal;
import com.starfish_studios.naturalist.entity.ai.navigation.BetterGroundPathNavigation;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
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
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Hand;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;
import java.util.function.Predicate;

public class Lion extends AnimalEntity implements IAnimatable, SleepingAnimal {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(Lion.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> HAS_MANE = DataTracker.registerData(Lion.class, TrackedDataHandlerRegistry.BOOLEAN);

    public Lion(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.stepHeight = 1.0F;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 48.0D);
    }

    @Override
    public EntityData initialize(ServerWorldAccess level, LocalDifficulty difficulty, SpawnReason reason, @Nullable EntityData spawnData, @Nullable NbtCompound dataTag) {
        PassiveData ageableMobGroupData;
        if (spawnData == null) {
            spawnData = new PassiveData(true);
            this.setHasMane(true);
        }
        if ((ageableMobGroupData = (PassiveData)spawnData).getSpawnedCount() > 2) {
            this.setBreedingAge(-24000);
        }
        ageableMobGroupData.countSpawned();
        this.getAttributeInstance(EntityAttributes.GENERIC_FOLLOW_RANGE).addPersistentModifier(new EntityAttributeModifier("Random spawn bonus", this.random.nextGaussian() * 0.05, EntityAttributeModifier.Operation.MULTIPLY_BASE));
        return spawnData;
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverLevel, PassiveEntity ageableMob) {
        return NaturalistEntityTypes.LION.get().create(serverLevel);
    }

    @Override
    protected EntityNavigation createNavigation(World level) {
        return new BetterGroundPathNavigation(this, level);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new LionPreyGoal(this));
        this.goalSelector.add(2, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.add(3, new SleepGoal<>(this));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LionFollowLeaderGoal(this, 1.1D, 8.0F, 24.0F));
        this.goalSelector.add(7, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(8, new LookAroundGoal(this));
        this.targetSelector.add(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PathAwareEntity.class, 10, true, true, entity -> entity.getType().isIn(NaturalistTags.EntityTypes.LION_HOSTILES) && !entity.isBaby() && !this.isSleeping() && !this.isBaby() && this.getWorld().isNight()));
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(HAS_MANE, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound pCompound) {
        super.writeCustomDataToNbt(pCompound);
        pCompound.putBoolean("Mane", this.hasMane());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.setHasMane(pCompound.getBoolean("Mane"));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.sidewaysSpeed = 0.0F;
            this.forwardSpeed = 0.0F;
        }
        this.calculateDimensions();
    }

    @Override
    protected void onGrowUp() {
        super.onGrowUp();
        this.setHasMane(this.getRandom().nextBoolean());
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.world.getTimeOfDay();
        if (this.getTarget() != null || this.world.isWater(this.getBlockPos())) {
            return false;
        } else {
            return dayTime > 6000 && dayTime < 13000;
        }
    }

    @Override
    public void mobTick() {
        if (this.getMoveControl().isMoving()) {
            double speedModifier = this.getMoveControl().getSpeed();
            if (speedModifier < 1.0D && this.isOnGround()) {
                this.setPose(EntityPose.CROUCHING);
                this.setSprinting(false);
            } else if (speedModifier >= 1.5D && this.isOnGround()) {
                this.setPose(EntityPose.STANDING);
                this.setSprinting(true);
            } else {
                this.setPose(EntityPose.STANDING);
                this.setSprinting(false);
            }
        } else {
            this.setPose(EntityPose.STANDING);
            this.setSprinting(false);
        }
    }

    @Override
    public boolean bypassesSteppingEffects() {
        return this.isInSneakingPose() || super.bypassesSteppingEffects();
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    @Override
    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    public void setHasMane(boolean hasMane) {
        this.dataTracker.set(HAS_MANE, hasMane);
    }

    public boolean hasMane() {
        return this.dataTracker.get(HAS_MANE);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return NaturalistSoundEvents.LION_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.LION_AMBIENT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation(this.hasMane() || this.isBaby() ? "lion.sleep2" : "lion.sleep", true));
            event.getController().setAnimationSpeed(1.0F);
        } else if (this.getVelocity().horizontalLengthSquared() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("lion.run", true));
                event.getController().setAnimationSpeed(2.5F);
            } else if (this.isInSneakingPose()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("lion.prey", true));
                event.getController().setAnimationSpeed(0.8F);
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("lion.walk", true));
                event.getController().setAnimationSpeed(1.0F);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lion.idle", true));
            event.getController().setAnimationSpeed(1.0F);
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("lion.swing", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class LionFollowLeaderGoal extends Goal {
        private final Lion mob;
        private final Predicate<MobEntity> followPredicate;
        @Nullable
        private Lion followingMob;
        private final double speedModifier;
        private final EntityNavigation navigation;
        private int timeToRecalcPath;
        private final float stopDistance;
        private float oldWaterCost;
        private final float areaSize;

        public LionFollowLeaderGoal(Lion mob, double speedModifier, float stopDistance, float areaSize) {
            this.mob = mob;
            this.followPredicate = followingMob -> followingMob != null && !followingMob.isBaby();
            this.speedModifier = speedModifier;
            this.navigation = mob.getNavigation();
            this.stopDistance = stopDistance;
            this.areaSize = areaSize;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            if (this.mob.isBaby() || this.mob.hasMane()) {
                return false;
            }
            List<Lion> nearbyLions = this.mob.world.getEntitiesByClass(Lion.class, this.mob.getBoundingBox().expand(this.areaSize), this.followPredicate);
            if (!nearbyLions.isEmpty()) {
                for (Lion lion : nearbyLions) {
                    if (!lion.hasMane()) continue;
                    if (lion.isInvisible()) continue;
                    this.followingMob = lion;
                    return true;
                }
                if (this.followingMob == null) {
                    for (Lion lion : nearbyLions) {
                        if (lion.isBaby()) continue;
                        if (lion.isInvisible()) continue;
                        this.followingMob = lion;
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public boolean shouldContinue() {
            return this.followingMob != null && !this.navigation.isIdle() && this.mob.squaredDistanceTo(this.followingMob) > (double)(this.stopDistance * this.stopDistance);
        }

        @Override
        public void start() {
            this.timeToRecalcPath = 0;
            this.oldWaterCost = this.mob.getPathfindingPenalty(PathNodeType.WATER);
            this.mob.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        }

        @Override
        public void stop() {
            this.followingMob = null;
            this.navigation.stop();
            this.mob.setPathfindingPenalty(PathNodeType.WATER, this.oldWaterCost);
        }

        @Override
        public void tick() {
            double f;
            double e;
            if (this.followingMob == null || this.mob.isLeashed()) {
                return;
            }
            this.mob.getLookControl().lookAt(this.followingMob, 10.0f, this.mob.getMaxLookPitchChange());
            if (--this.timeToRecalcPath > 0) {
                return;
            }
            this.timeToRecalcPath = this.getTickCount(10);
            double d = this.mob.getX() - this.followingMob.getX();
            double g = d * d + (e = this.mob.getY() - this.followingMob.getY()) * e + (f = this.mob.getZ() - this.followingMob.getZ()) * f;
            if (g <= (double)(this.stopDistance * this.stopDistance)) {
                this.navigation.stop();
                LookControl lookControl = this.followingMob.getLookControl();
                if (g <= (double)this.stopDistance || lookControl.getLookX() == this.mob.getX() && lookControl.getLookY() == this.mob.getY() && lookControl.getLookZ() == this.mob.getZ()) {
                    double h = this.followingMob.getX() - this.mob.getX();
                    double i = this.followingMob.getZ() - this.mob.getZ();
                    this.navigation.startMovingTo(this.mob.getX() - h, this.mob.getY(), this.mob.getZ() - i, this.speedModifier);
                }
                return;
            }
            this.navigation.startMovingTo(this.followingMob, this.speedModifier);
        }
    }

    static class LionPreyGoal extends Goal {
        protected final PathAwareEntity mob;
        private double speedModifier = 0.5D;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;

        public LionPreyGoal(PathAwareEntity pathfinderMob) {
            this.mob = pathfinderMob;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            if (this.mob.isBaby()) {
                return false;
            }
            long gameTime = this.mob.world.getTime();
            if (gameTime - this.lastCanUseCheck < 20L) {
                return false;
            }
            this.lastCanUseCheck = gameTime;
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            this.path = this.mob.getNavigation().findPathTo(livingEntity, 0);
            if (this.path != null) {
                return true;
            }
            return this.getAttackReachSqr(livingEntity) >= this.mob.squaredDistanceTo(livingEntity.getX(), livingEntity.getY(), livingEntity.getZ());
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (livingEntity == null) {
                return false;
            }
            if (!livingEntity.isAlive()) {
                return false;
            }
            return !this.mob.getNavigation().isIdle();
        }

        @Override
        public void start() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return;
            }
            this.speedModifier = this.mob.distanceTo(target) > 12 ? 0.5D : 1.7D;
            this.mob.getNavigation().startMovingAlong(this.path, this.speedModifier);
            this.mob.setAttacking(true);
            this.mob.playSound(NaturalistSoundEvents.LION_ROAR.get(), 1.0F, this.mob.getSoundPitch());
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget(null);
            }
            this.mob.setAttacking(false);
            this.mob.getNavigation().stop();
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.mob.getTarget();
            if (target == null) {
                return;
            }
            this.speedModifier = this.mob.distanceTo(target) > 12 ? 0.5D : 1.7D;
            this.mob.getLookControl().lookAt(target, 30.0f, 30.0f);
            double d = this.mob.squaredDistanceTo(target.getX(), target.getY(), target.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if (this.mob.getVisibilityCache().canSee(target) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || target.squaredDistanceTo(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.pathedTargetX = target.getX();
                this.pathedTargetY = target.getY();
                this.pathedTargetZ = target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.mob.getNavigation().startMovingTo(target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.getTickCount(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(target, d);
        }

        protected void checkAndPerformAttack(LivingEntity enemy, double distToEnemySqr) {
            double d = this.getAttackReachSqr(enemy);
            if (distToEnemySqr <= d && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(enemy);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.getTickCount(20);
        }

        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return this.mob.getWidth() * 2.0f * (this.mob.getWidth() * 2.0f) + attackTarget.getWidth();
        }
    }
}
