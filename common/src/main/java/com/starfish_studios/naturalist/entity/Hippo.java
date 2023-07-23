package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.Path;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;
import software.bernie.geckolib3.util.GeckoLibUtil;

import java.util.EnumSet;
import java.util.List;

public class Hippo extends Animal implements IAnimatable {
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.of(Blocks.MELON.asItem());
    private int eatingTicks;

    public Hippo(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0f);
        this.maxUpStep = 1.0f;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 40.0D).add(Attributes.FOLLOW_RANGE, 20.0D).add(Attributes.MOVEMENT_SPEED, 0.25D).add(Attributes.ATTACK_DAMAGE, 6.0D).add(Attributes.KNOCKBACK_RESISTANCE, 0.6D);
    }

    public static boolean checkHippoSpawnRules(EntityType<? extends Animal> entityType, LevelAccessor levelAccessor, MobSpawnType mobSpawnType, BlockPos blockPos, RandomSource randomSource) {
        /* if (levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos)) {
            for (Direction direction : Direction.Plane.HORIZONTAL) {
                return levelAccessor.getFluidState(blockPos.below().relative(direction)).is(FluidTags.WATER);
            }
        }
        return false; */

        // THIS CRASHES

        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        if (levelAccessor.getBlockState(blockPos.below()).is(BlockTags.ANIMALS_SPAWNABLE_ON) && Animal.isBrightEnoughToSpawn(levelAccessor, blockPos)) {
            for (int x = -16; x <= 16; x++) {
                for (int y = -1; y <= 1; y++) {
                    for (int z = -16; z <= 16; z++) {
                        mutableBlockPos.setWithOffset(blockPos, x, y, z);
                        if (!levelAccessor.hasChunkAt(mutableBlockPos)) {
                            continue;
                        }
                        if (levelAccessor.getFluidState(mutableBlockPos).is(FluidTags.WATER)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(2, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.addGoal(3, new HippoAttackBoatsGoal(this, 1.25D));
        this.goalSelector.addGoal(4, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.addGoal(5, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.addGoal(7, new RandomSwimmingGoal(this, 1.0D, 10));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
        this.targetSelector.addGoal(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, 10, true, false, (entity) -> !this.isBaby() && entity.isInWater()));
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (this.isFood(itemStack)) {
            int age = this.getAge();
            if (!this.level.isClientSide && age == 0 && this.canFallInLove()) {
                this.eatingTicks = 10;
                this.setItemSlot(EquipmentSlot.MAINHAND, itemStack.copy());
                this.swing(InteractionHand.MAIN_HAND);
                float yRot = (this.getYRot() + 90) * Mth.DEG_TO_RAD;
                ((ServerLevel)level).sendParticles(new BlockParticleOption(ParticleTypes.BLOCK, Blocks.MELON.defaultBlockState()), this.getX() + Math.cos(yRot), this.getY() + 0.6, this.getZ() + Math.sin(yRot), 100, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
                ((ServerLevel)level).sendParticles(new ItemParticleOption(ParticleTypes.ITEM, new ItemStack(Items.MELON_SLICE)), this.getX() + Math.cos(yRot), this.getY() + 0.6, this.getZ() + Math.sin(yRot), 100, this.getBbWidth() / 4.0F, this.getBbHeight() / 4.0F, this.getBbWidth() / 4.0F, 0.05D);
                this.playSound(SoundEvents.HORSE_EAT);
                this.playSound(SoundEvents.WOOD_BREAK);
                this.usePlayerItem(player, hand, itemStack);
                this.setInLove(player);
                return InteractionResult.SUCCESS;
            }
            if (this.isBaby()) {
                this.usePlayerItem(player, hand, itemStack);
                this.ageUp(Animal.getSpeedUpSecondsWhenFeeding(-age), true);
                return InteractionResult.sidedSuccess(this.level.isClientSide);
            }
            if (this.level.isClientSide) {
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.PASS;
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide) {
            if (this.eatingTicks > 0) {
                this.eatingTicks--;
            } else {
                this.setItemSlot(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public double getFluidJumpThreshold() {
        return this.isBaby() ? super.getFluidJumpThreshold() : 1.25;
    }

    @Override
    protected float getWaterSlowDown() {
        return 0.98F;
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        return this.isInWater() && otherAnimal.isInWater() && super.canMate(otherAnimal);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return NaturalistEntityTypes.HIPPO.get().create(serverLevel);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return NaturalistSoundEvents.HIPPO_HURT.get();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.HIPPO_AMBIENT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            event.getController().setAnimation(new AnimationBuilder().loop("hippo.walk"));
            event.getController().setAnimationSpeed(1.0D);
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("hippo.idle"));
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().playOnce("hippo.bite"));
            this.swinging = false;
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

    static class HippoAttackBoatsGoal extends Goal {
        protected final PathfinderMob mob;
        private final double speedModifier;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;
        private Boat target;

        public HippoAttackBoatsGoal(PathfinderMob pathfinderMob, double speedModifier) {
            this.mob = pathfinderMob;
            this.speedModifier = speedModifier;
            this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK, Flag.TARGET));
        }

        @Override
        public boolean canUse() {
            if (this.mob.isBaby()) {
                return false;
            }
            long gameTime = this.mob.level.getGameTime();
            if (gameTime - this.lastCanUseCheck < 20L) {
                return false;
            }
            this.lastCanUseCheck = gameTime;
            List<Boat> entities = this.mob.level.getEntitiesOfClass(Boat.class, this.mob.getBoundingBox().inflate(8, 4, 8));
            if (!entities.isEmpty()) {
                this.target = entities.get(0);
            }
            if (this.target == null) {
                return false;
            }
            if (this.target.isRemoved()) {
                return false;
            }
            this.path = this.mob.getNavigation().createPath(this.target, 0);
            if (this.path != null) {
                return true;
            }
            return this.getAttackReachSqr(this.target) >= this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
        }

        @Override
        public boolean canContinueToUse() {
            if (this.target == null) {
                return false;
            }
            if (this.target.isRemoved()) {
                return false;
            }
            return !this.mob.getNavigation().isDone();
        }

        @Override
        public void start() {
            this.mob.getNavigation().moveTo(this.path, this.speedModifier);
            this.mob.setAggressive(true);
            this.ticksUntilNextPathRecalculation = 0;
            this.ticksUntilNextAttack = 0;
        }

        @Override
        public void stop() {
            LivingEntity livingEntity = this.mob.getTarget();
            if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity)) {
                this.mob.setTarget(null);
            }
            this.mob.setAggressive(false);
            this.mob.getNavigation().stop();
        }

        @Override
        public boolean requiresUpdateEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            if (this.target == null) {
                return;
            }
            this.mob.getLookControl().setLookAt(this.target, 30.0f, 30.0f);
            double d = this.mob.distanceToSqr(this.target.getX(), this.target.getY(), this.target.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.mob.getSensing().hasLineOfSight(this.target)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || this.target.distanceToSqr(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.pathedTargetX = this.target.getX();
                this.pathedTargetY = this.target.getY();
                this.pathedTargetZ = this.target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.mob.getNavigation().moveTo(this.target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.adjustedTickDelay(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(this.target, d);
        }

        protected void checkAndPerformAttack(Entity enemy, double distToEnemySqr) {
            double reach = this.getAttackReachSqr(enemy);
            if (distToEnemySqr <= reach && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(enemy);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.adjustedTickDelay(20);
        }

        protected double getAttackReachSqr(Entity attackTarget) {
            return Mth.square(this.mob.getBbWidth() * 1.2f) + attackTarget.getBbWidth();
        }
    }
}
