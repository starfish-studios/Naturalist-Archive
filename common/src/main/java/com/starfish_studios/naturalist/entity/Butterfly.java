package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.FlyingWanderGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.Flutterer;
import net.minecraft.entity.ai.control.FlightMoveControl;
import net.minecraft.entity.ai.goal.AnimalMateGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.MoveToTargetPosGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.TemptGoal;
import net.minecraft.entity.ai.pathing.BirdNavigation;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.ItemTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldView;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class Butterfly extends AnimalEntity implements IAnimatable, Flutterer {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Boolean> HAS_NECTAR = DataTracker.registerData(Butterfly.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int numCropsGrownSincePollination;

    public Butterfly(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.moveControl = new FlightMoveControl(this, 20, true);
        this.setPathfindingPenalty(PathNodeType.DANGER_FIRE, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER, -1.0F);
        this.setPathfindingPenalty(PathNodeType.WATER_BORDER, 16.0F);
        this.setPathfindingPenalty(PathNodeType.COCOA, -1.0F);
        this.setPathfindingPenalty(PathNodeType.FENCE, -1.0F);
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 10.0D).add(EntityAttributes.GENERIC_FLYING_SPEED, 0.6F).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.3F);
    }

    @Override
    protected EntityNavigation createNavigation(World pLevel) {
        BirdNavigation navigation = new BirdNavigation(this, pLevel) {
            public boolean isValidPosition(BlockPos pPos) {
                return !this.world.getBlockState(pPos.down()).isAir();
            }
        };
        navigation.setCanPathThroughDoors(false);
        navigation.setCanSwim(false);
        navigation.setCanEnterOpenDoors(true);
        return navigation;
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(HAS_NECTAR, false);
    }

    public boolean hasNectar() {
        return this.dataTracker.get(HAS_NECTAR);
    }

    void setHasNectar(boolean hasNectar) {
        this.dataTracker.set(HAS_NECTAR, hasNectar);
    }

    @Override
    public float getPathfindingFavor(BlockPos pPos, WorldView pLevel) {
        return pLevel.getBlockState(pPos).isAir() ? 10.0F : 0.0F;
    }

    @Override
    public boolean isBreedingItem(ItemStack pStack) {
        return pStack.isIn(ItemTags.FLOWERS);
    }

    int getCropsGrownSincePollination() {
        return this.numCropsGrownSincePollination;
    }

    private void resetNumCropsGrownSincePollination() {
        this.numCropsGrownSincePollination = 0;
    }

    void incrementNumCropsGrownSincePollination() {
        ++this.numCropsGrownSincePollination;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(2, new TemptGoal(this, 1.25D, Ingredient.fromTag(ItemTags.FLOWERS), false));
        this.goalSelector.add(3, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(4, new ButterflyGrowCropGoal(this, 1.0D, 16, 4));
        this.goalSelector.add(5, new ButterflyPollinateGoal(this, 1.0D, 16, 4));
        this.goalSelector.add(6, new FlyingWanderGoal(this));
        this.goalSelector.add(7, new SwimGoal(this));
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (this.getCropsGrownSincePollination() >= 10) {
            this.resetNumCropsGrownSincePollination();
            this.setHasNectar(false);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.hasNectar() && this.getCropsGrownSincePollination() < 10 && this.random.nextFloat() < 0.05F) {
            for(int i = 0; i < this.random.nextInt(2) + 1; ++i) {
                this.spawnFluidParticle(this.world, this.getX() - 0.3F, this.getX() + 0.3F, this.getZ() - 0.3F, this.getZ() + 0.3F, this.getBodyY(0.5D), ParticleTypes.FALLING_NECTAR);
            }
        }
    }

    private void spawnFluidParticle(World level, double x1, double x2, double z1, double z2, double y, ParticleEffect particleOptions) {
        level.addParticle(particleOptions, MathHelper.lerp(level.random.nextDouble(), x1, x2), y, MathHelper.lerp(level.random.nextDouble(), z1, z2), 0.0D, 0.0D, 0.0D);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld level, PassiveEntity mob) {
        return NaturalistEntityTypes.CATERPILLAR.get().create(level);
    }

    @Override
    public boolean isBaby() {
        return false;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.isInAir()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("butterfly.fly", true));
            return PlayState.CONTINUE;
        }
        event.getController().markNeedsReload();
        return PlayState.STOP;
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

    @Override
    public boolean hasWings() {
        return this.isInAir() && this.age % MathHelper.ceil(1.4959966F) == 0;
    }

    @Override
    public boolean isInAir() {
        return !this.onGround;
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pPose, EntityDimensions pSize) {
        return pSize.height * 0.5F;
    }

    @Override
    public boolean handleFallDamage(float pFallDistance, float pMultiplier, DamageSource pSource) {
        return false;
    }

    @Override
    protected void fall(double pY, boolean pOnGround, BlockState pState, BlockPos pPos) {
    }

    @Override
    protected void playStepSound(BlockPos pPos, BlockState pState) {
    }

    static class ButterflyPollinateGoal extends MoveToTargetPosGoal {
        protected int ticksWaited;
        private final Butterfly butterfly;

        public ButterflyPollinateGoal(Butterfly pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.butterfly = pMob;
        }

        @Override
        protected boolean isTargetPos(WorldView pLevel, BlockPos pPos) {
            return pLevel.getBlockState(pPos).isIn(BlockTags.FLOWERS);
        }

        @Override
        public void tick() {
            if (this.hasReached()) {
                if (this.ticksWaited >= 40) {
                    this.onReachedTarget();
                } else {
                    ++this.ticksWaited;
                }
            }
            super.tick();
        }

        protected void onReachedTarget() {
            BlockState state = butterfly.world.getBlockState(targetPos);
            if (state.isIn(BlockTags.FLOWERS)) {
                butterfly.setHasNectar(true);
                this.stop();
            }
        }

        @Override
        public boolean canStart() {
            return !butterfly.hasNectar() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return !butterfly.hasNectar() && super.shouldContinue();
        }

        @Override
        public void start() {
            this.ticksWaited = 0;
            super.start();
        }
    }

    static class ButterflyGrowCropGoal extends MoveToTargetPosGoal {
        private final Butterfly butterfly;

        public ButterflyGrowCropGoal(Butterfly pMob, double pSpeedModifier, int pSearchRange, int pVerticalSearchRange) {
            super(pMob, pSpeedModifier, pSearchRange, pVerticalSearchRange);
            this.butterfly = pMob;
        }

        @Override
        protected boolean isTargetPos(WorldView pLevel, BlockPos pPos) {
            BlockState state = pLevel.getBlockState(pPos);
            return state.getBlock() instanceof CropBlock cropBlock && state.get(cropBlock.getAgeProperty()) < cropBlock.getMaxAge();
        }

        public void tick() {
            BlockPos blockpos = this.getTargetPos();
            if (!blockpos.isWithinDistance(this.mob.getPos(), this.getDesiredDistanceToTarget())) {
                ++this.tryingTime;
                if (this.shouldResetPath()) {
                    this.mob.getNavigation().startMovingTo((double)((float)blockpos.getX()) + 0.5D, blockpos.getY(), (double)((float)blockpos.getZ()) + 0.5D, this.speed);
                }
            } else {
                this.onReachedTarget();
            }

        }

        protected void onReachedTarget() {
            BlockState state = butterfly.world.getBlockState(targetPos);
            if (state.getBlock() instanceof CropBlock cropBlock) {
                cropBlock.applyGrowth(butterfly.world, targetPos, state);
                butterfly.incrementNumCropsGrownSincePollination();
                this.stop();
            }
        }

        @Override
        public boolean canStart() {
            return butterfly.hasNectar() && super.canStart();
        }

        @Override
        public boolean shouldContinue() {
            return butterfly.hasNectar() && super.shouldContinue();
        }
    }

}
