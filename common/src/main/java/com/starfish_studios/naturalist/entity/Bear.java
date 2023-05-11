package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.Containers;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.AbstractSchoolingFish;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.BeehiveBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.entity.CampfireBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
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
import java.util.UUID;
import java.util.function.Predicate;

public class Bear extends Animal implements NeutralMob, IAnimatable, SleepingAnimal, Shearable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.BEAR_TEMPT_ITEMS);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SNIFFING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SITTING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> SHEARED = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> EAT_COUNTER = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);
    @Nullable
    private UUID persistentAngerTarget;

    public Bear(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0F;
        this.setCanPickUpLoot(true);
    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.25D);
        } else {
            this.setSprinting(false);
        }
        super.customServerAiStep();
    }

    // BREEDING

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return NaturalistEntityTypes.BEAR.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return FOOD_ITEMS.test(pStack);
    }

    @Override
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor pLevel, DifficultyInstance pDifficulty, MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pSpawnData == null) {
            pSpawnData = new AgeableMobGroupData(1.0F);
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    // ATTRIBUTES/GOALS/LOGIC

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BearFloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new BearMeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(3, new BearTemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(3, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(4, new BearSleepGoal(this));
        this.goalSelector.addGoal(5, new DistancedFollowParentGoal(this, 1.25D, 48.0D, 8.0D, 12.0D));
        this.goalSelector.addGoal(5, new SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8, 2));
        this.goalSelector.addGoal(6, new BearHarvestFoodGoal(this, 1.2F, 12, 3));
        this.goalSelector.addGoal(7, new BearPickupFoodAndSitGoal(this));
        this.goalSelector.addGoal(8, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(9, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(10, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new BearAttackPlayerNearBabiesGoal(this, Player.class, 20, false, true, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, PathfinderMob.class, 10, true, false, (entity) -> entity.getType().is(NaturalistTags.EntityTypes.BEAR_HOSTILES) && !this.isSleeping() && !this.isBaby()));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.xxa = 0.0F;
            this.zza = 0.0F;
        }
        this.handleEating();
        if (!this.getMainHandItem().isEmpty()) {
            if (this.isAngry()) {
                this.stopBeingAngry();
            }
            this.setSniffing(false);
        }
        this.level.getProfiler().push("looting");
        if (!this.level.isClientSide && this.canPickUpLoot() && this.isAlive() && !this.dead && this.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
            for(ItemEntity itementity : this.level.getEntitiesOfClass(ItemEntity.class, this.getBoundingBox().inflate(1.0D, 0.0D, 1.0D))) {
                if (!itementity.isRemoved() && !itementity.getItem().isEmpty() && this.wantsToPickUp(itementity.getItem())) {
                    this.pickUpItem(itementity);
                }
            }
        }
        this.level.getProfiler().pop();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.equals(DamageSource.SWEET_BERRY_BUSH) || super.isInvulnerableTo(pSource);
    }

    @Override
    protected float getStandingEyeHeight(Pose pPose, EntityDimensions pSize) {
        return pSize.height * 0.75F;
    }

    // ENTITY DATA

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(SNIFFING, false);
        this.entityData.define(SITTING, false);
        this.entityData.define(SHEARED, false);
        this.entityData.define(EAT_COUNTER, 0);
        this.entityData.define(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readPersistentAngerSaveData(this.level, pCompound);
        if (pCompound.contains("Sheared")) {
            this.setSheared(pCompound.getBoolean("Sheared"));
        }
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addPersistentAngerSaveData(pCompound);
        pCompound.putBoolean("Sheared", this.isSheared());
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.level.getDayTime();
        return (dayTime < 12000 || dayTime > 18000) && dayTime < 23000 && dayTime > 6000 && !this.isAngry() && !this.level.isWaterAt(this.blockPosition());
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

    public boolean isSniffing() {
        return this.entityData.get(SNIFFING);
    }

    public void setSniffing(boolean sniffing) {
        this.entityData.set(SNIFFING, sniffing);
    }

    public boolean isSitting() {
        return this.entityData.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.entityData.set(SITTING, sitting);
    }

    public boolean isSheared() {
        return this.entityData.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.entityData.set(SHEARED, sheared);
    }

    public boolean isEating() {
        return this.entityData.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean eat) {
        this.entityData.set(EAT_COUNTER, eat ? 1 : 0);
    }

    private int getEatCounter() {
        return this.entityData.get(EAT_COUNTER);
    }

    private void setEatCounter(int amount) {
        this.entityData.set(EAT_COUNTER, amount);
    }

    // ANGER

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int pTime) {
        this.entityData.set(REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.entityData.get(REMAINING_ANGER_TIME);
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Nullable
    @Override
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    // EATING

    private void handleEating() {
        if (!this.isEating() && this.isSitting() && !this.isSleeping() && !this.getMainHandItem().isEmpty() && this.random.nextInt(80) == 1) {
            this.eat(true);
        } else if (this.getMainHandItem().isEmpty() || !this.isSitting()) {
            this.eat(false);
        }
        if (this.isEating()) {
            this.addEatingParticles();
            if (!this.level.isClientSide && this.getEatCounter() > 40) {
                if (this.isFood(this.getItemBySlot(EquipmentSlot.MAINHAND))) {
                    if (!this.level.isClientSide) {
                        this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.gameEvent(GameEvent.EAT);
                        this.setSheared(false);
                    }
                    this.setSitting(false);
                }
                this.eat(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    private void addEatingParticles() {
        if (this.getEatCounter() % 5 == 0 || this.getEatCounter() == 0) {
            this.playSound(NaturalistSoundEvents.BEAR_EAT.get(), 0.5F + 0.5F * (float)this.random.nextInt(2), (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.0F);

            for(int i = 0; i < 6; ++i) {
                Vec3 speedVec = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double)this.random.nextFloat() - 0.5D) * 0.1D);
                speedVec  = speedVec .xRot(-this.getXRot() * ((float)Math.PI / 180F));
                speedVec  = speedVec .yRot(-this.getYRot() * ((float)Math.PI / 180F));
                double y = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3 posVec = new Vec3(((double)this.random.nextFloat() - 0.5D) * 0.8D, y, 1.0D + ((double)this.random.nextFloat() - 0.5D) * 0.4D);
                posVec = posVec.yRot(-this.yBodyRot * ((float)Math.PI / 180F));
                posVec = posVec.add(this.getX(), this.getEyeY() + 1.0D, this.getZ());
                this.level.addParticle(new ItemParticleOption(ParticleTypes.ITEM, this.getItemBySlot(EquipmentSlot.MAINHAND)), posVec.x, posVec.y, posVec.z, speedVec .x, speedVec .y + 0.05D, speedVec .z);
            }
        }
    }

    @Override
    public boolean canTakeItem(ItemStack pItemstack) {
        EquipmentSlot slot = Mob.getEquipmentSlotForItem(pItemstack);
        if (!this.getItemBySlot(slot).isEmpty() || this.isBaby()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canTakeItem(pItemstack);
        }
    }

    @Override
    protected void pickUpItem(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getItem();
        if (this.getMainHandItem().isEmpty() && FOOD_ITEMS.test(stack) && !this.isBaby()) {
            this.onItemPickup(pItemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, stack);
            this.handDropChances[EquipmentSlot.MAINHAND.getIndex()] = 2.0F;
            this.take(pItemEntity, stack.getCount());
            pItemEntity.discard();
        }
    }

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        if (!this.getMainHandItem().isEmpty() && !this.level.isClientSide) {
            ItemEntity itemEntity = new ItemEntity(this.level, this.getX() + this.getLookAngle().x, this.getY() + 1.0D, this.getZ() + this.getLookAngle().z, this.getMainHandItem());
            itemEntity.setPickUpDelay(80);
            itemEntity.setThrower(this.getUUID());
            this.playSound(NaturalistSoundEvents.BEAR_SPIT.get(), 1.0F, 1.0F);
            this.level.addFreshEntity(itemEntity);
            this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.hurt(pSource, pAmount);
    }

    // SHEARING


    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.is(Items.SHEARS) && this.readyForShearing()) {
            if (!this.isSleeping()) {
                this.setLastHurtByMob(player);
            }
            this.shear(SoundSource.PLAYERS);
            this.gameEvent(GameEvent.SHEAR, player);
            if (!this.level.isClientSide) {
                itemStack.hurtAndBreak(1, player, p -> p.broadcastBreakEvent(hand));
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        return super.mobInteract(player, hand);
    }

    @Override
    public void shear(SoundSource source) {
        this.level.playSound(null, this, SoundEvents.SHEEP_SHEAR, source, 1.0f, 1.0f);
        this.setSheared(true);
        int amount = 1 + this.random.nextInt(2);
        for (int j = 0; j < amount; ++j) {
            ItemEntity itemEntity = this.spawnAtLocation(NaturalistRegistry.BEAR_FUR.get(), 1);
            if (itemEntity == null) continue;
            itemEntity.setDeltaMovement(itemEntity.getDeltaMovement().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        }
    }

    @Override
    public boolean readyForShearing() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    // MOVEMENT

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    void tryToSit() {
        if (!this.isTouchingWater()) {
            this.setZza(0.0F);
            this.getNavigation().stop();
            this.setSitting(true);
        }
    }

    boolean isTouchingWater() {
        return this.level.isWaterAt(this.blockPosition());
    }

    // SOUNDS

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return this.isBaby() ? NaturalistSoundEvents.BEAR_HURT_BABY.get() : NaturalistSoundEvents.BEAR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BEAR_DEATH.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isSleeping() ? NaturalistSoundEvents.BEAR_SLEEP.get() : this.isBaby() ? NaturalistSoundEvents.BEAR_AMBIENT_BABY.get() : NaturalistSoundEvents.BEAR_AMBIENT.get();
    }

    @Override
    public float getVoicePitch() {
        return this.isSleeping() ? super.getVoicePitch() * 0.3F : this.isBaby() ? super.getVoicePitch() * 0.4F : super.getVoicePitch();
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    // ANIMATION

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bear.sleep"));
            return PlayState.CONTINUE;
        } else if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bear.sit"));
            return PlayState.CONTINUE;
        } else if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().loop("run"));
                event.getController().setAnimationSpeed(1.8D);
                return PlayState.CONTINUE;
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop("walk"));
                event.getController().setAnimationSpeed(1.4D);
                return PlayState.CONTINUE;
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("idle"));
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState sniffPredicate(AnimationEvent<E> event) {
        if (this.isSniffing()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bear.sniff"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState swingPredicate(AnimationEvent<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().playOnce("bear.swing"));
            this.swinging = false;
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState eatPredicate(AnimationEvent<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(new AnimationBuilder().loop("bear.eat"));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5);
        data.addAnimationController(new AnimationController<>(this, "controller", 5, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "sniffController", 2, this::sniffPredicate));
        data.addAnimationController(new AnimationController<>(this, "swingController", 2, this::swingPredicate));
        data.addAnimationController(new AnimationController<>(this, "eatController", 5, this::eatPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    // GOALS

    static class BearAttackPlayerNearBabiesGoal extends NearestAttackableTargetGoal<Player> {
        private final Bear bear;

        public BearAttackPlayerNearBabiesGoal(Bear pMob, Class<Player> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @org.jetbrains.annotations.Nullable Predicate<LivingEntity> pTargetPredicate) {
            super(pMob, pTargetType, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
            this.bear = pMob;
        }

        @Override
        public boolean canUse() {
            if (!bear.isBaby() && !bear.isSleeping()) {
                if (super.canUse()) {
                    for (Bear bear : bear.level.getEntitiesOfClass(Bear.class, bear.getBoundingBox().inflate(8.0D, 4.0D, 8.0D))) {
                        if (bear.isBaby()) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        @Override
        protected double getFollowDistance() {
            return super.getFollowDistance() * 0.5D;
        }
    }

    class BearSleepGoal extends SleepGoal<Bear> {
        public BearSleepGoal(Bear animal) {
            super(animal);
        }

        @Override
        public void start() {
            Bear.this.setSniffing(false);
            super.start();
        }
    }

    static class BearHarvestFoodGoal extends MoveToBlockGoal {
        protected int ticksWaited;
        private final Bear bear;

        public BearHarvestFoodGoal(Bear pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.bear = pMob;
        }

        @Override
        public double acceptedDistance() {
            return 3.0D;
        }

        public boolean shouldRecalculatePath() {
            return this.tryTicks % 100 == 0;
        }

        @Override
        protected boolean isValidTarget(LevelReader pLevel, BlockPos pPos) {
            BlockState state = pLevel.getBlockState(pPos);
            if (state.getBlock() instanceof BeehiveBlock) {
                return state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5;
            } else if (state.is(Blocks.SWEET_BERRY_BUSH)) {
                return state.getValue(SweetBerryBushBlock.AGE) >= 2;
            } else if (state.is(Blocks.CAMPFIRE) && pLevel.getBlockEntity(pPos) instanceof CampfireBlockEntity campfire) {
                return campfireIsTempting(campfire);
            }
            return false;
        }

        @Override
        public void tick() {
            if (this.isReachedTarget()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            } else if (!this.isReachedTarget() && bear.getRandom().nextFloat() < 0.05F) {
                bear.playSound(NaturalistSoundEvents.BEAR_SNIFF.get(), 1.0F, 1.0F);
                bear.setSniffing(true);
            }
            bear.getLookControl().setLookAt(blockPos.getX() + 0.5D, blockPos.getY(), blockPos.getZ() + 0.5D, 10.0F, bear.getMaxHeadXRot());
            super.tick();
        }

        protected void onReachedTarget() {
            if (bear.level.getGameRules().getBoolean(GameRules.RULE_MOBGRIEFING)) {
                BlockState state = bear.level.getBlockState(blockPos);
                bear.setSniffing(false);
                if (state.getBlock() instanceof BeehiveBlock && state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    this.harvestHoney(state);
                } else if (state.is(Blocks.SWEET_BERRY_BUSH) && state.getValue(SweetBerryBushBlock.AGE) >= 2) {
                    this.pickSweetBerries(state);
                } else if (state.is(Blocks.CAMPFIRE) && bear.level.getBlockEntity(blockPos) instanceof CampfireBlockEntity campfire && campfireIsTempting(campfire)) {
                    this.stealCampfireFood(state, campfire);
                }
            }
        }

        private void stealCampfireFood(BlockState state, CampfireBlockEntity campfire) {
            for (int i = 0; i < campfire.getItems().size(); i++) {
                if (FOOD_ITEMS.test(campfire.getItems().get(i))) {
                    Containers.dropItemStack(bear.level, blockPos.getX(), blockPos.getY(), blockPos.getZ(), campfire.getItems().get(i));
                    campfire.getItems().set(i, ItemStack.EMPTY);
                    bear.level.sendBlockUpdated(blockPos, state, state, 3);
                    campfire.setChanged();
                    break;
                }
            }
        }

        private boolean campfireIsTempting(CampfireBlockEntity campfire) {
            for (int i = 0; i < campfire.getItems().size(); i++) {
                if (FOOD_ITEMS.test(campfire.getItems().get(i))) {
                    return true;
                }
            }
            return false;
        }

        private void harvestHoney(BlockState state) {
            state.setValue(BeehiveBlock.HONEY_LEVEL, 0);
            BeehiveBlock.dropHoneycomb(bear.level, blockPos);
            bear.playSound(SoundEvents.BEEHIVE_SHEAR, 1.0F, 1.0F);
            bear.level.setBlock(blockPos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), 2);
            bear.swing(InteractionHand.MAIN_HAND);
        }

        private void pickSweetBerries(BlockState state) {
            int age = state.getValue(SweetBerryBushBlock.AGE);
            state.setValue(SweetBerryBushBlock.AGE, 1);
            int berryAmount = 1 + bear.level.random.nextInt(2) + (age == 3 ? 1 : 0);
            Block.popResource(bear.level, this.blockPos, new ItemStack(Items.SWEET_BERRIES, berryAmount));
            bear.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
            bear.level.setBlock(this.blockPos, state.setValue(SweetBerryBushBlock.AGE, 1), 2);
            bear.swing(InteractionHand.MAIN_HAND);
        }

        @Override
        public boolean canUse() {
            return !bear.isBaby() && bear.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return bear.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.ticksWaited = 0;
            super.start();
        }

        @Override
        protected BlockPos getMoveToTarget() {
            BlockPos.MutableBlockPos mutable = new BlockPos.MutableBlockPos().set(blockPos);
            while (bear.level.getBlockState(mutable.below()).isAir()) {
                mutable.move(Direction.DOWN);
            }
            return mutable;
        }
    }

    static class BearFloatGoal extends FloatGoal {
        private final Bear bear;

        public BearFloatGoal(Bear pMob) {
            super(pMob);
            this.bear = pMob;
        }

        @Override
        public boolean canUse() {
            if (!bear.isBaby()) {
                return (bear.level.isWaterAt(bear.blockPosition().below()) || bear.level.isWaterAt(bear.blockPosition().above())) && super.canUse();
            } else {
                return super.canUse();
            }
        }
    }

    static class BearTemptGoal extends TemptGoal {
        private final Bear bear;

        public BearTemptGoal(Bear pMob, double pSpeedModifier, Ingredient pItems, boolean pCanScare) {
            super(pMob, pSpeedModifier, pItems, pCanScare);
            this.bear = pMob;
        }

        @Override
        public boolean canUse() {
            return bear.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public void start() {
            super.start();
            bear.setSniffing(true);
        }

        @Override
        public void tick() {
            super.tick();
            if (bear.getRandom().nextFloat() < 0.05F) {
                bear.playSound(NaturalistSoundEvents.BEAR_SNIFF.get(), 1.0F, 1.0F);
            }
        }

        @Override
        public void stop() {
            super.stop();
            bear.setSniffing(false);
        }
    }

    static class BearPickupFoodAndSitGoal extends Goal {
        private int cooldown;
        private final Bear bear;
        
        public BearPickupFoodAndSitGoal(Bear bear) {
            this.setFlags(EnumSet.of(Flag.MOVE));
            this.bear = bear;
        }

        @Override
        public boolean canUse() {
            if (this.cooldown <= bear.tickCount && !bear.isBaby() && !bear.isTouchingWater() && !bear.isSleeping() && !bear.isSitting()) {
                return !bear.getMainHandItem().isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return !bear.isTouchingWater();
        }

        @Override
        public void tick() {
            if (!bear.isSitting() && !bear.getMainHandItem().isEmpty()) {
                bear.tryToSit();
            }
        }

        @Override
        public void start() {
            if (!bear.getMainHandItem().isEmpty()) {
                bear.tryToSit();
            }

            this.cooldown = 0;
        }

        @Override
        public void stop() {
            ItemStack stack = bear.getItemBySlot(EquipmentSlot.MAINHAND);
            if (!stack.isEmpty()) {
                bear.spawnAtLocation(stack);
                bear.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                int cooldownSeconds = bear.random.nextInt(150) + 10;
                this.cooldown = bear.tickCount + cooldownSeconds * 20;
            }
            bear.setSitting(false);
        }
    }

    static class BearMeleeAttackGoal extends MeleeAttackGoal {

        public BearMeleeAttackGoal(PathfinderMob pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canUse() {
            return mob.getMainHandItem().isEmpty() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return mob.getMainHandItem().isEmpty() && super.canContinueToUse();
        }

        @Override
        protected double getAttackReachSqr(LivingEntity pAttackTarget) {
            return pAttackTarget instanceof AbstractSchoolingFish ? super.getAttackReachSqr(pAttackTarget) : 4.0F + pAttackTarget.getBbWidth();
        }
    }
}
