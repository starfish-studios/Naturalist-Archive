package crispytwig.naturalist.entity;

import crispytwig.naturalist.entity.ai.goal.CloseMeleeAttackGoal;
import crispytwig.naturalist.registry.NaturalistEntityTypes;
import crispytwig.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.TimeUtil;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.ResetUniversalAngerTargetGoal;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.event.ForgeEventFactory;
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

public class Bear extends Animal implements NeutralMob, IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.Items.BEAR_TEMPT_ITEMS);
    private static final EntityDataAccessor<Boolean> SLEEPING = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.BOOLEAN);
    private static final UniformInt PERSISTENT_ANGER_TIME = TimeUtil.rangeOfSeconds(20, 39);
    private static final EntityDataAccessor<Integer> REMAINING_ANGER_TIME = SynchedEntityData.defineId(Bear.class, EntityDataSerializers.INT);
    @Nullable
    private UUID persistentAngerTarget;

    public Bear(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return NaturalistEntityTypes.BEAR.get().create(level);
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return FOOD_ITEMS.test(pStack);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 30.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new BearFloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(3, new CloseMeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(3, new BearPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(4, new BearSleepGoal(this));
        this.goalSelector.addGoal(5, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(5, new BearHarvestFoodGoal(this, 1.2F, 12, 3));
        this.goalSelector.addGoal(6, new RandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(7, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(8, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BearHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new BearAttackPlayerNearBabiesGoal(this, Player.class, 20, true, true, null));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, this::isAngryAt));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, AbstractSchoolingFish.class, 10, true, false, (entity) -> entity.getType().is(NaturalistTags.EntityTypes.BEAR_HOSTILES) && !this.isSleeping()));
        this.targetSelector.addGoal(5, new ResetUniversalAngerTargetGoal<>(this, false));
    }

    // ENTITY DATA

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(SLEEPING, false);
        this.entityData.define(REMAINING_ANGER_TIME, 0);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.readPersistentAngerSaveData(this.level, pCompound);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        this.addPersistentAngerSaveData(pCompound);
    }

    @Override
    public boolean isSleeping() {
        return this.entityData.get(SLEEPING);
    }

    public void setSleeping(boolean sleeping) {
        this.entityData.set(SLEEPING, sleeping);
    }

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
        if (this.getTarget() != null) {
            if (this.getTarget() instanceof Player player) {
                if (FOOD_ITEMS.test(player.getMainHandItem()) || FOOD_ITEMS.test(player.getOffhandItem())) {
                    this.stopBeingAngry();
                }
            }
        }
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.85F;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isSleeping()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.sleep", true));
            return PlayState.CONTINUE;
        } else if (event.isMoving()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("bear.walk", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
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

    static class BearPanicGoal extends PanicGoal {
        public BearPanicGoal(PathfinderMob pMob, double pSpeedModifier) {
            super(pMob, pSpeedModifier);
        }

        @Override
        protected boolean shouldPanic() {
            return mob.getLastHurtByMob() != null && mob.isBaby() || mob.isOnFire();
        }
    }

    static class BearHurtByTargetGoal extends HurtByTargetGoal {
        private final Bear bear;

        public BearHurtByTargetGoal(Bear pMob, Class<?>... pToIgnoreDamage) {
            super(pMob, pToIgnoreDamage);
            this.bear = pMob;
        }

        @Override
        public void start() {
            super.start();
            if (bear.isBaby()) {
                this.alertOthers();
                this.stop();
            }
        }

        @Override
        protected void alertOther(Mob pMob, LivingEntity pTarget) {
            if (pMob instanceof PolarBear && !pMob.isBaby()) {
                super.alertOther(pMob, pTarget);
            }
        }
    }

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

    static class BearSleepGoal extends Goal {
        private final Bear bear;

        public BearSleepGoal(Bear bear) {
            this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.JUMP));
            this.bear = bear;
        }

        @Override
        public boolean canUse() {
            if (bear.xxa == 0.0F && bear.yya == 0.0F && bear.zza == 0.0F) {
                return this.canSleep() || bear.isSleeping();
            } else {
                return false;
            }
        }

        @Override
        public boolean canContinueToUse() {
            return this.canSleep();
        }

        private boolean canSleep() {
            long dayTime = bear.getLevel().getDayTime();
            return (dayTime < 12000 || dayTime > 18000) && dayTime < 23000 && dayTime > 6000 && !bear.isAngry();
        }

        @Override
        public void start() {
            bear.setJumping(false);
            bear.setSleeping(true);
            bear.getNavigation().stop();
            bear.getMoveControl().setWantedPosition(bear.getX(), bear.getY(), bear.getZ(), 0.0D);
        }

        @Override
        public void stop() {
            bear.setSleeping(false);
        }
    }

    static class BearHarvestFoodGoal extends MoveToBlockGoal {
        protected int ticksWaited;

        public BearHarvestFoodGoal(PathfinderMob pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
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
            } else if (!this.isReachedTarget() && mob.getRandom().nextFloat() < 0.05F) {
                mob.playSound(SoundEvents.FOX_SNIFF, 1.0F, 1.0F);
            }

            super.tick();
        }

        protected void onReachedTarget() {
            if (ForgeEventFactory.getMobGriefingEvent(mob.level, mob)) {
                BlockState state = mob.level.getBlockState(blockPos);
                if (state.getBlock() instanceof BeehiveBlock && state.getValue(BeehiveBlock.HONEY_LEVEL) >= 5) {
                    this.harvestHoney(state);
                } else if (state.is(Blocks.SWEET_BERRY_BUSH) && state.getValue(SweetBerryBushBlock.AGE) >= 2) {
                    this.pickSweetBerries(state);
                }
            }
        }

        private void harvestHoney(BlockState state) {
            if (mob.getRandom().nextInt(5) > 0) {
                state.setValue(BeehiveBlock.HONEY_LEVEL, 0);
                BeehiveBlock.dropHoneycomb(mob.level, blockPos);
                mob.playSound(SoundEvents.BEEHIVE_SHEAR, 1.0F, 1.0F);
                mob.level.setBlock(blockPos, state.setValue(BeehiveBlock.HONEY_LEVEL, 0), 2);
            } else {
                mob.level.setBlock(blockPos, Blocks.AIR.defaultBlockState(), 2);
                mob.level.destroyBlock(blockPos, false, mob);
                mob.level.playSound(null, blockPos, SoundEvents.WOOD_BREAK, SoundSource.BLOCKS, 1.0F, 1.0F);
            }
        }

        private void pickSweetBerries(BlockState state) {
            int age = state.getValue(SweetBerryBushBlock.AGE);
            state.setValue(SweetBerryBushBlock.AGE, 1);
            int berryAmount = 1 + mob.level.random.nextInt(2) + (age == 3 ? 1 : 0);
            Block.popResource(mob.level, this.blockPos, new ItemStack(Items.SWEET_BERRIES, berryAmount));
            mob.playSound(SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, 1.0F, 1.0F);
            mob.level.setBlock(this.blockPos, state.setValue(SweetBerryBushBlock.AGE, 1), 2);
        }

        @Override
        public boolean canUse() {
            return !mob.isBaby() && super.canUse();
        }
        
        @Override
        public void start() {
            this.ticksWaited = 0;
            super.start();
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
            return bear.level.isWaterAt(bear.blockPosition().below()) && super.canUse();
        }
    }
}
