package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
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
import net.minecraft.world.Difficulty;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Zoglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.UUID;
import java.util.function.Predicate;

public class Boar extends Animal implements NeutralMob, IAnimatable, ItemSteerable, Saddleable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.BOAR_FOOD_ITEMS);
    private final ItemBasedSteering steering;

    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private int remainingPersistentAngerTime;
    @Nullable
    private UUID persistentAngerTarget;

    public Boar(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 14.0).add(Attributes.MOVEMENT_SPEED, 0.2).add(Attributes.ATTACK_DAMAGE, 1.0D);
    }

    @Override
    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BOOST_TIME.equals(key) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(key);
    }

    public Entity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity != null && this.canBeControlledBy(entity) ? entity : null;
    }

    private boolean canBeControlledBy(Entity entity) {
        if (this.isSaddled() && entity instanceof Player player) {
            return player.getMainHandItem().is(Items.CARROT_ON_A_STICK) || player.getOffhandItem().is(Items.CARROT_ON_A_STICK);
        } else {
            return false;
        }
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob otherParent) {
        return NaturalistEntityTypes.BOAR.get().create(level);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.2, FOOD_ITEMS, false));
        this.goalSelector.addGoal(3, new BoarAvoidPlayerGoal(this, Player.class, 16.0f, 1.5D, 1.5D, entity -> !entity.isHolding(FOOD_ITEMS)));
        this.goalSelector.addGoal(4, new BoarMeleeAttackGoal(this, 1.5, false));
        this.goalSelector.addGoal(5, new BabyPanicGoal(this, 1.4));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.1));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, (new HurtByTargetGoal(this)).setAlertOthers(this.getClass()));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(3, new ResetUniversalAngerTargetGoal<>(this, true));
    }

    public boolean isAggro() {
        return this.getHealth() > this.getMaxHealth() / 2;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (!this.isAggro()) {
                this.stopBeingAngry();
            }
            this.updatePersistentAnger((ServerLevel)this.level, true);
        }
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

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.BOAR_AMBIENT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return NaturalistSoundEvents.BOAR_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getDeathSound() {
        return NaturalistSoundEvents.BOAR_DEATH.get();
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.PIG_STEP, 0.15f, 1.0f);
    }

    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.75F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.5F;
    }

    @Override
    public void thunderHit(ServerLevel level, LightningBolt lightning) {
        if (level.getDifficulty() != Difficulty.PEACEFUL) {
            Zoglin zoglin = EntityType.ZOGLIN.create(level);
            zoglin.moveTo(this.getX(), this.getY(), this.getZ(), this.getYRot(), this.getXRot());
            zoglin.setNoAi(this.isNoAi());
            zoglin.setBaby(this.isBaby());
            if (this.hasCustomName()) {
                zoglin.setCustomName(this.getCustomName());
                zoglin.setCustomNameVisible(this.isCustomNameVisible());
            }
            zoglin.setPersistenceRequired();
            level.addFreshEntity(zoglin);
            this.discard();
        } else {
            super.thunderHit(level, lightning);
        }
    }

    @Override
    public void startPersistentAngerTimer() {
        this.setRemainingPersistentAngerTime(PERSISTENT_ANGER_TIME.sample(this.random));
    }

    @Override
    public void setRemainingPersistentAngerTime(int remainingPersistentAngerTime) {
        this.remainingPersistentAngerTime = remainingPersistentAngerTime;
    }

    @Override
    public int getRemainingPersistentAngerTime() {
        return this.remainingPersistentAngerTime;
    }

    @Override
    public void setPersistentAngerTarget(@Nullable UUID persistentAngerTarget) {
        this.persistentAngerTarget = persistentAngerTarget;
    }

    @Override
    @Nullable
    public UUID getPersistentAngerTarget() {
        return this.persistentAngerTarget;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().loop("boar.run"));
                event.getController().setAnimationSpeed(2.4D);
            } else {
                event.getController().setAnimation(new AnimationBuilder().loop("boar.walk"));
                event.getController().setAnimationSpeed(1.4D);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("boar.idle"));
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

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        this.steering.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.steering.readAdditionalSaveData(compound);
    }

    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    @Override
    public void equipSaddle(@Nullable SoundSource source) {
        this.steering.setSaddle(true);
        if (source != null) {
            this.level.playSound((Player)null, this, SoundEvents.PIG_SADDLE, source, 0.5F, 1.0F);
        }
    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean bl = this.isFood(player.getItemInHand(hand));
        if (!this.isAngry() && !bl && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
            if (!this.level.isClientSide) {
                player.startRiding(this);
            }

            return InteractionResult.sidedSuccess(this.level.isClientSide);
        } else {
            InteractionResult interactionResult = super.mobInteract(player, hand);
            if (!interactionResult.consumesAction()) {
                ItemStack itemStack = player.getItemInHand(hand);
                return itemStack.is(Items.SADDLE) ? itemStack.interactLivingEntity(player, this, hand) : InteractionResult.PASS;
            } else {
                return interactionResult;
            }
        }
    }

    public void travel(Vec3 travelVector) {
        this.travel(this, this.steering, travelVector);

        LivingEntity livingEntity = (LivingEntity) this.getControllingPassenger();

        if (this.isControlledByLocalInstance()) {
            this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
        } else if (livingEntity instanceof Player) {
            this.setDeltaMovement(Vec3.ZERO);
            this.setSprinting(true);
        }
    }

    public void travelWithInput(Vec3 travelVector) {
        super.travel(travelVector);
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
    }
    @Override
    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 0.5F;
    }

    static class BoarMeleeAttackGoal extends MeleeAttackGoal {
        private final Boar boar;

        public BoarMeleeAttackGoal(Boar mob, double speedModifier, boolean followingTargetEvenIfNotSeen) {
            super(mob, speedModifier, followingTargetEvenIfNotSeen);
            this.boar = mob;
        }

        @Override
        public boolean canUse() {
            return this.boar.isAggro() && !this.boar.isBaby() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return this.boar.isAggro() && !this.boar.isBaby() && super.canContinueToUse();
        }
    }

    static class BoarAvoidPlayerGoal extends AvoidEntityGoal<Player> {
        private final Boar boar;

        public BoarAvoidPlayerGoal(Boar mob, Class<Player> entityClassToAvoid, float maxDistance, double walkSpeedModifier, double sprintSpeedModifier, Predicate<LivingEntity> predicateOnAvoidEntity) {
            super(mob, entityClassToAvoid, maxDistance, walkSpeedModifier, sprintSpeedModifier, predicateOnAvoidEntity);
            this.boar = mob;
        }

        @Override
        public boolean canUse() {
            return !this.boar.isAggro() && !this.boar.isBaby() && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return !this.boar.isAggro() && !this.boar.isBaby() && super.canContinueToUse();
        }

        @Override
        public void start() {
            this.boar.stopBeingAngry();
            super.start();
        }
    }

    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(Boar.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(Boar.class, EntityDataSerializers.INT);
    }
}
