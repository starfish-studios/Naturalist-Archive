package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.SearchForItemsGoal;
import com.starfish_studios.naturalist.entity.ai.goal.SleepGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.RevengeGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.UniversalAngerGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.Angerable;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.mob.SlimeEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.util.TimeHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.intprovider.UniformIntProvider;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.event.GameEvent;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public class Snake extends ClimbingAnimal implements SleepingAnimal, Angerable, IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.fromTag(NaturalistTags.ItemTags.SNAKE_TEMPT_ITEMS);
    private static final UniformIntProvider PERSISTENT_ANGER_TIME = TimeHelper.betweenSeconds(20, 39);
    private static final TrackedData<Integer> REMAINING_ANGER_TIME = DataTracker.registerData(Snake.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> SLEEPING = DataTracker.registerData(Snake.class, TrackedDataHandlerRegistry.BOOLEAN);
    private static final TrackedData<Integer> EAT_COUNTER = DataTracker.registerData(Snake.class, TrackedDataHandlerRegistry.INTEGER);
    @Nullable
    private UUID persistentAngerTarget;

    public Snake(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.setCanPickUpLoot(true);
    }

    // ATTRIBUTES/GOALS/LOGIC

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.18D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new SnakeMeleeAttackGoal(this, 1.75D, true));
        this.goalSelector.add(2, new SearchForItemsGoal(this, 1.2F, FOOD_ITEMS, 8.0D, 8.0D));
        this.goalSelector.add(3, new SleepGoal<>(this));
        this.goalSelector.add(4, new WanderAroundFarGoal(this, 1.0D));
        this.goalSelector.add(5, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(6, new LookAroundGoal(this));
        this.targetSelector.add(1, new RevengeGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, this::shouldAngerAt));
        this.targetSelector.add(3, new ActiveTargetGoal<>(this, MobEntity.class, 5, true, false, livingEntity -> livingEntity.getType().isIn(NaturalistTags.EntityTypes.SNAKE_HOSTILES) || (livingEntity instanceof SlimeEntity slime && slime.isSmall())));
        this.targetSelector.add(4, new UniversalAngerGoal<>(this, false));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld p_146743_, PassiveEntity p_146744_) {
        return null;
    }

    public static boolean checkSnakeSpawnRules(EntityType<Snake> entityType, WorldAccess level, SpawnReason type, BlockPos pos, Random random) {
        return level.getBlockState(pos.down()).isIn(BlockTags.RABBITS_SPAWNABLE_ON) && isLightLevelValidForNaturalSpawn(level, pos);
    }

    @Override
    public EntityData initialize(ServerWorldAccess pLevel, LocalDifficulty pDifficulty, SpawnReason pReason, @Nullable EntityData pSpawnData, @Nullable NbtCompound pDataTag) {
        this.initEquipment(random, pDifficulty);
        return super.initialize(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    protected void initEquipment(Random random, LocalDifficulty pDifficulty) {
        if (random.nextFloat() < 0.2F) {
            float chance = random.nextFloat();
            ItemStack stack;
            if (chance < 0.05F) {
                stack = new ItemStack(Items.RABBIT_FOOT);
            } else if (chance < 0.1F) {
                stack = new ItemStack(Items.SLIME_BALL);
            } else if (chance < 0.15F) {
                stack = new ItemStack(Items.FEATHER);
            } else if (chance < 0.3F) {
                stack = new ItemStack(Items.RABBIT);
            } else {
                stack = new ItemStack(Items.CHICKEN);
            }

            this.equipStack(EquipmentSlot.MAINHAND, stack);
        }
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return false;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(SLEEPING, false);
        this.dataTracker.startTracking(EAT_COUNTER, 0);
        this.dataTracker.startTracking(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.readAngerFromNbt(this.world, pCompound);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound pCompound) {
        super.writeCustomDataToNbt(pCompound);
        this.writeAngerToNbt(pCompound);
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
        }
        if (this.canRattle() && !this.isSleeping()) {
            this.playSound(NaturalistSoundEvents.SNAKE_RATTLE.get(), 0.15F, 1.0F);
        }
    }

    private void handleEating() {
        if (!this.isEating() && !this.isSleeping() && !this.getMainHandStack().isEmpty()) {
            this.eat(true);
        } else if (this.getMainHandStack().isEmpty()) {
            this.eat(false);
        }
        if (this.isEating()) {
            if (!this.world.isClient && this.getEatCounter() > 6000) {
                if (!this.getMainHandStack().isEmpty()) {
                    if (!this.world.isClient) {
                        this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
                        this.emitGameEvent(GameEvent.EAT);
                    }
                }
                this.eat(false);
                return;
            }
            this.setEatCounter(this.getEatCounter() + 1);
        }
    }

    // EATING

    @Override
    public boolean canEquip(ItemStack pItemstack) {
        EquipmentSlot slot = MobEntity.getPreferredEquipmentSlot(pItemstack);
        if (!this.getEquippedStack(slot).isEmpty()) {
            return false;
        } else {
            return slot == EquipmentSlot.MAINHAND && super.canEquip(pItemstack);
        }
    }

    @Override
    protected void loot(ItemEntity pItemEntity) {
        ItemStack stack = pItemEntity.getStack();
        if (this.getMainHandStack().isEmpty() && FOOD_ITEMS.test(stack)) {
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
            this.playSound(SoundEvents.ENTITY_FOX_SPIT, 1.0F, 1.0F);
            this.world.spawnEntity(itemEntity);
            this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
        }
        return super.damage(pSource, pAmount);
    }

    // MOVEMENT

    @Override
    protected float getClimbSpeedMultiplier() {
        return 0.5F;
    }

    @Override
    public float getMovementSpeed() {
        return this.getMainHandStack().isEmpty() ? super.getMovementSpeed() : super.getMovementSpeed() * 0.5F;
    }

    // SLEEPING

    @Override
    public boolean canSleep() {
        long dayTime = this.world.getTimeOfDay();
        if (this.hasAngerTime() || this.world.isWater(this.getBlockPos())) {
            return false;
        } else if (dayTime > 18000 && dayTime < 23000) {
            return false;
        } else return dayTime > 12000 && dayTime < 28000;
    }

    @Override
    public void setSleeping(boolean sleeping) {
        this.dataTracker.set(SLEEPING, sleeping);
    }

    @Override
    public boolean isSleeping() {
        return this.dataTracker.get(SLEEPING);
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

    // SNAKE VARIANTS

    @Override
    public boolean tryAttack(Entity pEntity) {
        if ((this.getType().equals(NaturalistEntityTypes.CORAL_SNAKE.get()) || this.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get())) && pEntity instanceof LivingEntity living) {
            living.addStatusEffect(new StatusEffectInstance(StatusEffects.POISON, 40));
        }
        return super.tryAttack(pEntity);
    }

    private boolean canRattle() {
        List<PlayerEntity> players = this.getWorld().getPlayers(TargetPredicate.createNonAttackable().setBaseMaxDistance(4.0D), this, this.getBoundingBox().expand(4.0D, 2.0D, 4.0D));
        return !players.isEmpty() && this.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get());
    }

    @Override
    protected float getSoundVolume() {
        return 0.15F;
    }

    // SOUNDS

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return NaturalistSoundEvents.SNAKE_HURT.get();
    }

    // ANIMATION

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.sleep", true));
            return PlayState.CONTINUE;
        } else if (this.isClimbing()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.climb", true));
            return PlayState.CONTINUE;
        } else if (!(event.getLimbSwingAmount() > -0.04F && event.getLimbSwingAmount() < 0.04F)) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.move", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.attack", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState tonguePredicate(AnimationEvent<E> event) {
        if (this.random.nextInt(1000) < this.ambientSoundChance && !this.isSleeping() && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.tongue", false));
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState rattlePredicate(AnimationEvent<E> event) {
        if (this.canRattle() && !this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("snake.rattle", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
    }

    private void soundListener(SoundKeyframeEvent<Snake> event) {
        Snake snake = event.getEntity();
        if (snake.world.isClient) {
            if (event.sound.equals("hiss")) {
                snake.world.playSound(snake.getX(), snake.getY(), snake.getZ(), NaturalistSoundEvents.SNAKE_HISS.get(), snake.getSoundCategory(), snake.getSoundVolume(), snake.getSoundPitch(), false);
            }
        }
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
        AnimationController<Snake> tongueController = new AnimationController<>(this, "tongueController", 0, this::tonguePredicate);
        tongueController.registerSoundListener(this::soundListener);
        data.addAnimationController(tongueController);
        data.addAnimationController(new AnimationController<>(this, "rattleController", 0, this::rattlePredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    // GOALS

    static class SnakeMeleeAttackGoal extends MeleeAttackGoal {

        public SnakeMeleeAttackGoal(PathAwareEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
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
            return 4.0F + pAttackTarget.getWidth();
        }
    }
}
