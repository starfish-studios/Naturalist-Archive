package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.*;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistItems;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.block.*;
import net.minecraft.block.entity.CampfireBlockEntity;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.passive.SchoolingFishEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.*;
import net.minecraft.world.event.GameEvent;
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
import java.util.UUID;
import java.util.function.Predicate;

public class Bear extends AnimalEntity implements Angerable, IAnimatable, SleepingAnimal, Shearable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.fromTag(NaturalistTags.ItemTags.BEAR_TEMPT_ITEMS);
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(Bear.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SNIFFING = DataTracker.registerData(Bear.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SITTING = DataTracker.registerData(Bear.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Boolean> SHEARED = DataTracker.registerData(Bear.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> EAT_COUNTER = DataTracker.registerData(Bear.class, TrackedDataHandlerRegistry.INTEGER);
    private static final UniformIntProvider PERSISTENT_ANGER_TIME = TimeHelper.betweenSeconds(20, 39);
    private static final TrackedData<Integer> REMAINING_ANGER_TIME = DataTracker.registerData(Bear.class, TrackedDataHandlerRegistry.INTEGER);
    @Nullable
    private UUID persistentAngerTarget;

    public Bear(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.stepHeight = 1.0F;
        this.setCanPickUpLoot(true);
    }

    // BREEDING

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld level, PassiveEntity mob) {
        return NaturalistEntityTypes.BEAR.get().create(level);
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return FOOD_ITEMS.test(pStack);
    }

    @Override
    public EntityData initialize(ServerWorldAccess pLevel, LocalDifficulty pDifficulty, SpawnReason pReason, @Nullable EntityData pSpawnData, @Nullable NbtCompound pDataTag) {
        if (pSpawnData == null) {
            pSpawnData = new PassiveData(1.0F);
        }

        return super.initialize(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    // ATTRIBUTES/GOALS/LOGIC

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 30.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new BearFloatGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(2, new BearMeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.add(3, new BearTemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.add(3, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.add(4, new BearSleepGoal(this));
        this.goalSelector.add(5, new DistancedFollowParentGoal(this, 1.25D, 48.0D, 8.0D, 12.0D));
        this.goalSelector.add(5, new SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8, 2));
        this.goalSelector.add(6, new BearHarvestFoodGoal(this, 1.2F, 12, 3));
        this.goalSelector.add(7, new BearPickupFoodAndSitGoal(this));
        this.goalSelector.add(8, new WanderAroundGoal(this, 1.0D));
        this.goalSelector.add(9, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(10, new LookAroundGoal(this));
        this.targetSelector.add(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.add(2, new BearAttackPlayerNearBabiesGoal(this, PlayerEntity.class, 20, false, true, null));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(4, new ActiveTargetGoal<>(this, PathAwareEntity.class, 10, true, false, (entity) -> entity.getType().isIn(NaturalistTags.EntityTypes.BEAR_HOSTILES) && !this.isSleeping() && !this.isBaby()));
        this.targetSelector.add(5, new UniversalAngerGoal<>(this, false));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            this.tickAngerLogic((ServerWorld)this.world, true);
        }
        if (this.isSleeping() || this.isImmobile()) {
            this.jumping = false;
            this.sidewaysSpeed = 0.0F;
            this.forwardSpeed = 0.0F;
        }
        this.handleEating();
        if (!this.getMainHandStack().isEmpty()) {
            if (this.hasAngerTime()) {
                this.stopAnger();
            }
            this.setSniffing(false);
        }
        this.world.getProfiler().push("looting");
        if (!this.world.isClient && this.canPickUpLoot() && this.isAlive() && !this.dead && this.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
            for(ItemEntity itementity : this.world.getNonSpectatingEntities(ItemEntity.class, this.getBoundingBox().expand(1.0D, 0.0D, 1.0D))) {
                if (!itementity.isRemoved() && !itementity.getStack().isEmpty() && this.canGather(itementity.getStack())) {
                    this.loot(itementity);
                }
            }
        }
        this.world.getProfiler().pop();
    }

    @Override
    public boolean isInvulnerableTo(DamageSource pSource) {
        return pSource.equals(DamageSource.SWEET_BERRY_BUSH) || super.isInvulnerableTo(pSource);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pPose, EntityDimensions pSize) {
        return pSize.height * 0.75F;
    }

    // ENTITY DATA

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(SNIFFING, false);
        this.dataTracker.startTracking(SITTING, false);
        this.dataTracker.startTracking(SHEARED, false);
        this.dataTracker.startTracking(EAT_COUNTER, 0);
        this.dataTracker.startTracking(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.readAngerFromNbt(this.world, pCompound);
        if (pCompound.contains("Sheared")) {
            this.setSheared(pCompound.getBoolean("Sheared"));
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound pCompound) {
        super.writeCustomDataToNbt(pCompound);
        this.writeAngerToNbt(pCompound);
        pCompound.putBoolean("Sheared", this.isSheared());
    }

    @Override
    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
    }

    @Override
    public boolean canSleep() {
        long dayTime = this.world.getTimeOfDay();
        return (dayTime < 12000 || dayTime > 18000) && dayTime < 23000 && dayTime > 6000 && !this.hasAngerTime() && !this.world.isWater(this.getBlockPos());
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    public boolean isSniffing() {
        return this.dataTracker.get(SNIFFING);
    }

    public void setSniffing(boolean sniffing) {
        this.dataTracker.set(SNIFFING, sniffing);
    }

    public boolean isSitting() {
        return this.dataTracker.get(SITTING);
    }

    public void setSitting(boolean sitting) {
        this.dataTracker.set(SITTING, sitting);
    }

    public boolean isSheared() {
        return this.dataTracker.get(SHEARED);
    }

    public void setSheared(boolean sheared) {
        this.dataTracker.set(SHEARED, sheared);
    }

    public boolean isEating() {
        return this.dataTracker.get(EAT_COUNTER) > 0;
    }

    public void eat(boolean eat) {
        this.dataTracker.set(EAT_COUNTER, eat ? 1 : 0);
    }

    private int getEatCounter() {
        return this.dataTracker.get(EAT_COUNTER);
    }

    private void setEatCounter(int amount) {
        this.dataTracker.set(EAT_COUNTER, amount);
    }

    // ANGER

    @Override
    public void chooseRandomAngerTime() {
        this.setAngerTime(PERSISTENT_ANGER_TIME.get(this.random));
    }

    @Override
    public void setAngerTime(int pTime) {
        this.dataTracker.set(REMAINING_ANGER_TIME, pTime);
    }

    @Override
    public int getAngerTime() {
        return this.dataTracker.get(REMAINING_ANGER_TIME);
    }

    @Override
    public void setAngryAt(@Nullable UUID pTarget) {
        this.persistentAngerTarget = pTarget;
    }

    @Nullable
    @Override
    public UUID getAngryAt() {
        return this.persistentAngerTarget;
    }

    // EATING

    private void handleEating() {
        if (!this.isEating() && this.isSitting() && !this.isSleeping() && !this.getMainHandStack().isEmpty() && this.random.nextInt(80) == 1) {
            this.eat(true);
        } else if (this.getMainHandStack().isEmpty() || !this.isSitting()) {
            this.eat(false);
        }
        if (this.isEating()) {
            this.addEatingParticles();
            if (!this.world.isClient && this.getEatCounter() > 40) {
                if (this.isBreedingItem(this.getEquippedStack(EquipmentSlot.MAINHAND))) {
                    if (!this.world.isClient) {
                        this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.emitGameEvent(GameEvent.EAT);
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
                Vec3d speedVec = new Vec3d(((double)this.random.nextFloat() - 0.5D) * 0.1D, Math.random() * 0.1D + 0.1D, ((double)this.random.nextFloat() - 0.5D) * 0.1D);
                speedVec  = speedVec .rotateX(-this.getPitch() * ((float)Math.PI / 180F));
                speedVec  = speedVec .rotateY(-this.getYaw() * ((float)Math.PI / 180F));
                double y = (double)(-this.random.nextFloat()) * 0.6D - 0.3D;
                Vec3d posVec = new Vec3d(((double)this.random.nextFloat() - 0.5D) * 0.8D, y, 1.0D + ((double)this.random.nextFloat() - 0.5D) * 0.4D);
                posVec = posVec.rotateY(-this.bodyYaw * ((float)Math.PI / 180F));
                posVec = posVec.add(this.getX(), this.getEyeY() + 1.0D, this.getZ());
                this.world.addParticle(new ItemStackParticleEffect(ParticleTypes.ITEM, this.getEquippedStack(EquipmentSlot.MAINHAND)), posVec.x, posVec.y, posVec.z, speedVec .x, speedVec .y + 0.05D, speedVec .z);
            }
        }
    }

    @Override
    public boolean canEquip(ItemStack pItemstack) {
        EquipmentSlot slot = MobEntity.getPreferredEquipmentSlot(pItemstack);
        if (!this.getEquippedStack(slot).isEmpty() || this.isBaby()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canEquip(pItemstack);
        }
    }

    @Override
    protected void loot(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getStack();
        if (this.getMainHandStack().isEmpty() && FOOD_ITEMS.test(stack) && !this.isBaby()) {
            this.triggerItemPickedUpByEntityCriteria(pItemEntity);
            this.equipStack(EquipmentSlot.MAINHAND, stack);
            this.handDropChances[EquipmentSlot.MAINHAND.getEntitySlotId()] = 2.0F;
            this.sendPickup(pItemEntity, stack.getCount());
            pItemEntity.discard();
        }
    }

    @Override
    public boolean damage(DamageSource pSource, float pAmount) {
        if (!this.getMainHandStack().isEmpty() && !this.world.isClient) {
            ItemEntity itemEntity = new ItemEntity(this.world, this.getX() + this.getRotationVector().x, this.getY() + 1.0D, this.getZ() + this.getRotationVector().z, this.getMainHandStack());
            itemEntity.setPickupDelay(80);
            itemEntity.setThrower(this.getUuid());
            this.playSound(NaturalistSoundEvents.BEAR_SPIT.get(), 1.0F, 1.0F);
            this.world.spawnEntity(itemEntity);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.damage(pSource, pAmount);
    }

    // SHEARING

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (itemStack.isOf(Items.SHEARS) && this.isShearable()) {
            if (!this.isSleeping()) {
                this.setAttacker(player);
            }
            this.sheared(SoundCategory.PLAYERS);
            this.emitGameEvent(GameEvent.SHEAR, player);
            if (!this.world.isClient) {
                itemStack.damage(1, player, p -> p.sendToolBreakStatus(hand));
            }
            return ActionResult.success(this.world.isClient);
        }
        return ActionResult.PASS;
    }

    @Override
    public void sheared(SoundCategory category) {
        this.world.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_SHEEP_SHEAR, category, 1.0f, 1.0f);
        this.setSheared(true);
        int amount = 1 + this.random.nextInt(2);
        for (int j = 0; j < amount; ++j) {
            ItemEntity itemEntity = this.dropItem(NaturalistItems.BEAR_FUR.get(), 1);
            if (itemEntity == null) continue;
            itemEntity.setVelocity(itemEntity.getVelocity().add((this.random.nextFloat() - this.random.nextFloat()) * 0.1f, this.random.nextFloat() * 0.05f, (this.random.nextFloat() - this.random.nextFloat()) * 0.1f));
        }
    }

    @Override
    public boolean isShearable() {
        return this.isAlive() && !this.isSheared() && !this.isBaby();
    }

    // MOVEMENT

    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.98F;
    }

    void tryToSit() {
        if (!this.isInWater()) {
            this.setForwardSpeed(0.0F);
            this.getNavigation().stop();
            this.setSitting(true);
        }
    }

    boolean isInWater() {
        return this.world.isWater(this.getBlockPos());
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
    public float getSoundPitch() {
        return this.isSleeping() ? super.getSoundPitch() * 0.3F : this.isBaby() ? super.getSoundPitch() * 0.4F : super.getSoundPitch();
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pBlock) {
        this.playSound(SoundEvents.ENTITY_POLAR_BEAR_STEP, 0.15F, 1.0F);
    }

    // ANIMATION

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.sleep", true));
            return PlayState.CONTINUE;
        } else if (this.isSitting()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.sit", true));
            return PlayState.CONTINUE;
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.walk", true));
            return PlayState.CONTINUE;
        } else if (!this.isSniffing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.idle", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState sniffPredicate(AnimationEvent<E> event) {
        if (this.isSniffing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.sniff", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState swingPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.swing", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState eatPredicate(AnimationEvent<E> event) {
        if (this.isEating()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.eat", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "sniffController", 0, this::sniffPredicate));
        data.addAnimationController(new AnimationController<>(this, "swingController", 0, this::swingPredicate));
        data.addAnimationController(new AnimationController<>(this, "eatController", 5, this::eatPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    // GOALS

    static class BearAttackPlayerNearBabiesGoal extends ActiveTargetGoal<PlayerEntity> {
        private final Bear bear;

        public BearAttackPlayerNearBabiesGoal(Bear pMob, Class<PlayerEntity> pTargetType, int pRandomInterval, boolean pMustSee, boolean pMustReach, @org.jetbrains.annotations.Nullable Predicate<LivingEntity> pTargetPredicate) {
            super(pMob, pTargetType, pRandomInterval, pMustSee, pMustReach, pTargetPredicate);
            this.bear = pMob;
        }

        @Override
        public boolean canStart() {
            if (!bear.isBaby() && !bear.isSleeping()) {
                if (super.canStart()) {
                    for (Bear bear : bear.world.getNonSpectatingEntities(Bear.class, bear.getBoundingBox().expand(8.0D, 4.0D, 8.0D))) {
                        if (bear.isBaby()) {
                            return true;
                        }
                    }
                }

            }
            return false;
        }

        @Override
        protected double getFollowRange() {
            return super.getFollowRange() * 0.5D;
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

    static class BearHarvestFoodGoal extends MoveToTargetPosGoal {
        protected int ticksWaited;
        private final Bear bear;

        public BearHarvestFoodGoal(Bear pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.bear = pMob;
        }

        @Override
        public double getDesiredDistanceToTarget() {
            return 3.0D;
        }

        public boolean shouldResetPath() {
            return this.tryingTime % 100 == 0;
        }

        @Override
        protected boolean isTargetPos(WorldView pLevel, BlockPos pPos) {
            BlockState state = pLevel.getBlockState(pPos);
            if (state.getBlock() instanceof BeehiveBlock) {
                return state.get(BeehiveBlock.HONEY_LEVEL) >= 5;
            } else if (state.isOf(Blocks.SWEET_BERRY_BUSH)) {
                return state.get(SweetBerryBushBlock.AGE) >= 2;
            } else if (state.isOf(Blocks.CAMPFIRE) && pLevel.getBlockEntity(pPos) instanceof CampfireBlockEntity campfire) {
                return campfireIsTempting(campfire);
            }
            return false;
        }

        @Override
        public void tick() {
            if (this.hasReached()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            } else if (!this.hasReached() && bear.getRandom().nextFloat() < 0.05F) {
                bear.playSound(NaturalistSoundEvents.BEAR_SNIFF.get(), 1.0F, 1.0F);
                bear.setSniffing(true);
            }
            bear.getLookControl().lookAt(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, 10.0F, bear.getMaxLookPitchChange());
            super.tick();
        }

        protected void onReachedTarget() {
            if (bear.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                BlockState state = bear.world.getBlockState(targetPos);
                bear.setSniffing(false);
                if (state.getBlock() instanceof BeehiveBlock && state.get(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    this.harvestHoney(state);
                } else if (state.isOf(Blocks.SWEET_BERRY_BUSH) && state.get(SweetBerryBushBlock.AGE) >= 2) {
                    this.pickSweetBerries(state);
                } else if (state.isOf(Blocks.CAMPFIRE) && bear.world.getBlockEntity(targetPos) instanceof CampfireBlockEntity campfire && campfireIsTempting(campfire)) {
                    this.stealCampfireFood(state, campfire);
                }
            }
        }

        private void stealCampfireFood(BlockState state, CampfireBlockEntity campfire) {
            for (int i = 0; i < campfire.getItemsBeingCooked().size(); i++) {
                if (FOOD_ITEMS.test(campfire.getItemsBeingCooked().get(i))) {
                    ItemScatterer.spawn(bear.world, targetPos.getX(), targetPos.getY(), targetPos.getZ(), campfire.getItemsBeingCooked().get(i));
                    campfire.getItemsBeingCooked().set(i, ItemStack.EMPTY);
                    bear.world.updateListeners(targetPos, state, state, 3);
                    campfire.markDirty();
                    break;
                }
            }
        }

        private boolean campfireIsTempting(CampfireBlockEntity campfire) {
            for (int i = 0; i < campfire.getItemsBeingCooked().size(); i++) {
                if (FOOD_ITEMS.test(campfire.getItemsBeingCooked().get(i))) {
                    return true;
                }
            }
            return false;
        }

        private void harvestHoney(BlockState state) {
            state.with(BeehiveBlock.HONEY_LEVEL, 0);
            BeehiveBlock.dropHoneycomb(bear.world, targetPos);
            bear.playSound(SoundEvents.BLOCK_BEEHIVE_SHEAR, 1.0F, 1.0F);
            bear.world.setBlockState(targetPos, state.with(BeehiveBlock.HONEY_LEVEL, 0), 2);
            bear.swingHand(Hand.MAIN_HAND);
        }

        private void pickSweetBerries(BlockState state) {
            int age = state.get(SweetBerryBushBlock.AGE);
            state.with(SweetBerryBushBlock.AGE, 1);
            int berryAmount = 1 + bear.world.random.nextInt(2) + (age == 3 ? 1 : 0);
            Block.dropStack(bear.world, this.targetPos, new ItemStack(Items.SWEET_BERRIES, berryAmount));
            bear.playSound(SoundEvents.BLOCK_SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
            bear.world.setBlockState(this.targetPos, state.with(SweetBerryBushBlock.AGE, 1), 2);
            bear.swingHand(Hand.MAIN_HAND);
        }

        @Override
        public boolean canStart() {
            return !bear.isBaby() && bear.getMainHandStack().isEmpty() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return bear.getMainHandStack().isEmpty() && super.shouldContinue();
        }

        @Override
        public void start() {
            this.ticksWaited = 0;
            super.start();
        }

        @Override
        protected BlockPos getTargetPos() {
            BlockPos.Mutable mutable = new BlockPos.Mutable().set(targetPos);
            while (bear.world.getBlockState(mutable.down()).isAir()) {
                mutable.move(Direction.DOWN);
            }
            return mutable;
        }
    }

    static class BearFloatGoal extends SwimGoal {
        private final Bear bear;

        public BearFloatGoal(Bear pMob) {
            super(pMob);
            this.bear = pMob;
        }

        @Override
        public boolean canStart() {
            if (!bear.isBaby()) {
                return (bear.world.isWater(bear.getBlockPos().down()) || bear.world.isWater(bear.getBlockPos().up())) && super.canStart();
            } else {
                return super.canStart();
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
        public boolean canStart() {
            return bear.getMainHandStack().isEmpty() && super.canStart();
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
            this.setControls(EnumSet.of(Control.MOVE));
            this.bear = bear;
        }

        @Override
        public boolean canStart() {
            if (this.cooldown <= bear.age && !bear.isBaby() && !bear.isInWater() && !bear.isSleeping() && !bear.isSitting()) {
                return !bear.getMainHandStack().isEmpty();
            } else {
                return false;
            }
        }

        @Override
        public boolean shouldContinue() {
            return !bear.isInWater();
        }

        @Override
        public void tick() {
            if (!bear.isSitting() && !bear.getMainHandStack().isEmpty()) {
                bear.tryToSit();
            }
        }

        @Override
        public void start() {
            if (!bear.getMainHandStack().isEmpty()) {
                bear.tryToSit();
            }

            this.cooldown = 0;
        }

        @Override
        public void stop() {
            ItemStack stack = bear.getEquippedStack(EquipmentSlot.MAINHAND);
            if (!stack.isEmpty()) {
                bear.dropStack(stack);
                bear.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                int cooldownSeconds = bear.random.nextInt(150) + 10;
                this.cooldown = bear.age + cooldownSeconds * 20;
            }
            bear.setSitting(false);
        }
    }

    static class BearMeleeAttackGoal extends MeleeAttackGoal {

        public BearMeleeAttackGoal(PathAwareEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
            super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        }

        @Override
        public boolean canStart() {
            return mob.getMainHandStack().isEmpty() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return mob.getMainHandStack().isEmpty() && super.shouldContinue();
        }

        @Override
        protected double getSquaredMaxAttackDistance(LivingEntity pAttackTarget) {
            return pAttackTarget instanceof SchoolingFishEntity ? super.getSquaredMaxAttackDistance(pAttackTarget) : 4.0F + pAttackTarget.getWidth();
        }
    }
}
