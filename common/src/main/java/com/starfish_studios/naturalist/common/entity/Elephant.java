package com.starfish_studios.naturalist.common.entity;

import com.starfish_studios.naturalist.common.entity.core.ElephantContainer;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.common.entity.core.ai.goal.DistancedFollowParentGoal;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.core.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.navigation.GroundPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bee;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import javax.annotation.Nullable;
import java.util.EnumSet;

public class Elephant extends AbstractChestedHorse implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    // private static final EntityDataAccessor<Integer> DIRTY_TICKS = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> DRINKING = SynchedEntityData.defineId(Elephant.class, EntityDataSerializers.BOOLEAN);

    protected ElephantContainer inventory;
    @Nullable
    protected BlockPos waterPos;

    public Elephant(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0f;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 80.0D).add(Attributes.MOVEMENT_SPEED, 0.3D).add(Attributes.ATTACK_DAMAGE, 10.0D).add(Attributes.ATTACK_KNOCKBACK, 1.2).add(Attributes.KNOCKBACK_RESISTANCE, 0.75D).add(Attributes.FOLLOW_RANGE, 20.0D);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        AgeableMobGroupData ageableMobGroupData;
        if (spawnData == null) {
            spawnData = new AgeableMobGroupData(true);
        }
        if ((ageableMobGroupData = (AgeableMobGroupData)spawnData).getGroupSize() > 1) {
            this.setAge(-24000);
        }
        ageableMobGroupData.increaseGroupSizeByOne();
        RandomSource random = level.getRandom();
        this.getAttribute(Attributes.FOLLOW_RANGE).addPermanentModifier(new AttributeModifier("Random spawn bonus", random.triangle(0.0, 0.11485000000000001), AttributeModifier.Operation.MULTIPLY_BASE));
        return spawnData;
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return NaturalistEntityTypes.ELEPHANT.get().create(serverLevel);
    }

    @Override
    protected PathNavigation createNavigation(Level level) {
        return new GroundPathNavigation(this, level);
    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.2D);
        } else {
            this.setSprinting(false);
        }
        super.customServerAiStep();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Bee.class, 8.0f, 1.3, 1.3));
        this.goalSelector.addGoal(2, new ElephantMeleeAttackGoal(this, 1.3D, false));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(4, new DistancedFollowParentGoal(this, 1.25D, 24.0D, 6.0D, 12.0D));
        this.goalSelector.addGoal(5, new ElephantDrinkWaterGoal(this));
        this.goalSelector.addGoal(6, new ElephantMoveToWaterGoal(this, 1.0D, 8, 4));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
    }

    @Override
    public int getMaxHeadYRot() {
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
    public boolean doHurtTarget(Entity target) {
        boolean shouldHurt = target.hurt(DamageSource.mobAttack(this), (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE));
        if (shouldHurt && target instanceof LivingEntity livingEntity) {
            Vec3 knockbackDirection = new Vec3(this.blockPosition().getX() - target.getX(), 0.0, this.blockPosition().getZ() - target.getZ()).normalize();
            float shieldBlockModifier = livingEntity.isDamageSourceBlocked(DamageSource.mobAttack(this)) ? 0.5f : 1.0f;
            livingEntity.knockback(shieldBlockModifier * 3.0D, knockbackDirection.x(), knockbackDirection.z());
            double knockbackResistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE));
            livingEntity.setDeltaMovement(livingEntity.getDeltaMovement().add(0.0, 0.5f * knockbackResistance, 0.0));
        }
        this.playSound(SoundEvents.RAVAGER_ATTACK, 1.0f, 1.0f);
        return shouldHurt;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        // this.entityData.define(DIRTY_TICKS, 0);
        this.entityData.define(DRINKING, false);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        // pCompound.putInt("DirtyTicks", this.getDirtyTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        // this.setDirtyTicks(pCompound.getInt("DirtyTicks"));
        this.updateContainerEquipment();
    }

    // public void setDirtyTicks(int ticks) {
    //     this.entityData.set(DIRTY_TICKS, ticks);
    //  }

    // public int getDirtyTicks() {
    //     return this.entityData.get(DIRTY_TICKS);
    // }

    // public boolean isDirty() {
    //     return this.getDirtyTicks() > 0;
    // }

    public void setDrinking(boolean drinking) {
        this.entityData.set(DRINKING, drinking);
    }

    public boolean isDrinking() {
        return this.entityData.get(DRINKING);
    }



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

    public double getPassengersRidingOffset() {
        return 3.2;
    }

    protected void playChestEquipsSound() {
        this.playSound(SoundEvents.LLAMA_CHEST, 1.0F, (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);
    }

    public int getInventoryColumns() {
        return 5;
    }

    protected int getInventorySize() {
        return this.hasChest() ? 27 : super.getInventorySize();
    }

    public void openCustomInventoryScreen(Player player) {
        if (!this.level.isClientSide && (!this.isVehicle() || this.hasPassenger(player)) && this.isTamed()) {
            player.openHorseInventory(this, this.inventory);
        }

    }

    public boolean isSaddleable() {
        return true;
    }

    public void containerChanged(Container container) {
        super.containerChanged(container);
    }

    public boolean canEatGrass() {
        return false;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        /* if (this.level instanceof ServerLevel serverLevel) {
            if (this.isDirty()) {
                this.setDirtyTicks(this.isInWater() ? 0 : Math.max(0, this.getDirtyTicks() - 1));
            } else {
                long dayTime = serverLevel.getDayTime();
                if (dayTime > 4300 && dayTime < 11000 && this.isOnGround() && this.getRandom().nextFloat() < 0.001f && !this.isDrinking()) {
                    this.swing(InteractionHand.MAIN_HAND);
                    this.setDirtyTicks(1000);
                    serverLevel.sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.DIRT.defaultBlockState()), this.getX(), this.getY(), this.getZ(),
                            200, 0.5, 3.0, 0.5, 10);
                }
            }
        } */
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().loop("run"));
                event.getController().setAnimationSpeed(1.2F);
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop("walk"));
                event.getController().setAnimationSpeed(0.7F);
            }
        } else if (this.isDrinking()) {
            event.getController().setAnimation(new AnimationBuilder().loop("elephant.water"));
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("idle"));
            event.getController().setAnimationSpeed(0.5F);
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState swingPredicate(AnimationEvent<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().playOnce("elephant.swing"));
            this.swinging = false;
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
        public ElephantMeleeAttackGoal(PathfinderMob pathfinderMob, double speedMultiplier, boolean followingTargetEvenIfNotSeen) {
            super(pathfinderMob, speedMultiplier, followingTargetEvenIfNotSeen);
        }

        @Override
        protected double getAttackReachSqr(LivingEntity attackTarget) {
            return Mth.square(this.mob.getBbWidth());
        }
    }

    static class ElephantMoveToWaterGoal extends MoveToBlockGoal {
        private final Elephant elephant;

        public ElephantMoveToWaterGoal(Elephant pathfinderMob, double speedModifier, int searchRange, int verticalSearchRange) {
            super(pathfinderMob, speedModifier, searchRange, verticalSearchRange);
            this.elephant = pathfinderMob;
        }

        @Override
        public boolean canUse() {
            return !this.elephant.isBaby() && !this.elephant.isDrinking() && this.elephant.waterPos == null && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.isReachedTarget() && super.canContinueToUse();
        }

        @Override
        protected boolean isValidTarget(LevelReader level, BlockPos pos) {
            if (level.getBlockState(pos).isFaceSturdy(level, pos, Direction.DOWN)) {
                for (Direction direction : Direction.Plane.HORIZONTAL) {
                    if (level.getFluidState(pos.relative(direction)).is(Fluids.WATER)) {
                        this.elephant.waterPos = pos.relative(direction);
                        return true;
                    }
                }
            }
            return false;
        }

        @Override
        public double acceptedDistance() {
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
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.JUMP, Goal.Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (this.elephant.waterPos == null || this.elephant.distanceToSqr(Vec3.atCenterOf(this.elephant.waterPos)) > 15) {
                this.elephant.setDrinking(false);
                return false;
            }
            return this.elephant.isDrinking();
        }

        @Override
        public boolean canContinueToUse() {
            return this.drinkTicks > 0 && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.drinkTicks = 150;
            if (this.elephant.waterPos != null) {
                this.elephant.getLookControl().setLookAt(Vec3.atCenterOf(this.elephant.waterPos));
            }
        }

        @Override
        public void tick() {
            this.drinkTicks--;
            if (this.elephant.waterPos != null) {
                this.elephant.getLookControl().setLookAt(Vec3.atCenterOf(this.elephant.waterPos));
            }
        }

        @Override
        public void stop() {
            this.elephant.waterPos = null;
            this.elephant.setDrinking(false);
        }
    }
}
