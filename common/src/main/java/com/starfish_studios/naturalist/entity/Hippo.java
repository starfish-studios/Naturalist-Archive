package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.ai.pathing.PathNodeType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.PathAwareEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ItemStackParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.tag.BlockTags;
import net.minecraft.tag.FluidTags;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
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
import java.util.Random;

public class Hippo extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final Ingredient FOOD_ITEMS = Ingredient.ofItems(Blocks.MELON.asItem());
    private int eatingTicks;

    public Hippo(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.setPathfindingPenalty(PathNodeType.WATER, 0.0f);
        this.stepHeight = 1.0f;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 20.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D);
    }

    public static boolean checkHippoSpawnRules(EntityType<? extends AnimalEntity> entityType, WorldAccess levelAccessor, SpawnReason mobSpawnType, BlockPos blockPos, Random randomSource) {
        if (levelAccessor.getBlockState(blockPos.down()).isIn(BlockTags.ANIMALS_SPAWNABLE_ON) && AnimalEntity.isLightLevelValidForNaturalSpawn(levelAccessor, blockPos)) {
            for (Direction direction : Direction.Type.HORIZONTAL) {
                return levelAccessor.getFluidState(blockPos.down().offset(direction)).isIn(FluidTags.WATER);
            }
        }
        return false;
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public boolean canBreatheInWater() {
        return true;
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new AnimalMateGoal(this, 1.0D));
        this.goalSelector.add(2, new TemptGoal(this, 1.0D, FOOD_ITEMS, false));
        this.goalSelector.add(3, new HippoAttackBoatsGoal(this, 1.25D));
        this.goalSelector.add(4, new MeleeAttackGoal(this, 1.25D, true));
        this.goalSelector.add(5, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.add(6, new FollowParentGoal(this, 1.25D));
        this.goalSelector.add(7, new SwimAroundGoal(this, 1.0D, 10));
        this.goalSelector.add(8, new LookAtEntityGoal(this, PlayerEntity.class, 6.0F));
        this.goalSelector.add(9, new LookAroundGoal(this));
        this.targetSelector.add(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.add(2, new ActiveTargetGoal<>(this, PlayerEntity.class, 10, true, false, (entity) -> !this.isBaby() && entity.isTouchingWater()));
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack itemStack = player.getStackInHand(hand);
        if (this.isBreedingItem(itemStack)) {
            int age = this.getBreedingAge();
            if (!this.world.isClient && age == 0 && this.canEat()) {
                this.eatingTicks = 10;
                this.equipStack(EquipmentSlot.MAINHAND, itemStack.copy());
                this.swingHand(Hand.MAIN_HAND);
                float yRot = (this.getYaw() + 90) * MathHelper.RADIANS_PER_DEGREE;
                ((ServerWorld)world).spawnParticles(new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.MELON.getDefaultState()), this.getX() + Math.cos(yRot), this.getY() + 0.6, this.getZ() + Math.sin(yRot), 100, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
                ((ServerWorld)world).spawnParticles(new ItemStackParticleEffect(ParticleTypes.ITEM, new ItemStack(Items.MELON_SLICE)), this.getX() + Math.cos(yRot), this.getY() + 0.6, this.getZ() + Math.sin(yRot), 100, this.getWidth() / 4.0F, this.getHeight() / 4.0F, this.getWidth() / 4.0F, 0.05D);
                this.playSound(SoundEvents.ENTITY_HORSE_EAT, this.getSoundVolume(), this.getSoundPitch());
                this.playSound(SoundEvents.BLOCK_WOOD_BREAK, this.getSoundVolume(), this.getSoundPitch());
                this.eat(player, hand, itemStack);
                this.lovePlayer(player);
                return ActionResult.SUCCESS;
            }
            if (this.isBaby()) {
                this.eat(player, hand, itemStack);
                int i = -age;
                this.growUp((int) ((float) (i / 20) * 0.1f), true);
                return ActionResult.success(this.world.isClient);
            }
            if (this.world.isClient) {
                return ActionResult.CONSUME;
            }
        }
        return ActionResult.PASS;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient) {
            if (this.eatingTicks > 0) {
                this.eatingTicks--;
            } else {
                this.equipStack(EquipmentSlot.MAINHAND, ItemStack.EMPTY);
            }
        }
    }

    @Override
    public double getSwimHeight() {
        return this.isBaby() ? super.getSwimHeight() : 1.25;
    }

    @Override
    protected float getBaseMovementSpeedMultiplier() {
        return 0.98F;
    }

    @Override
    public boolean canBreedWith(AnimalEntity otherAnimal) {
        return this.isTouchingWater() && otherAnimal.isTouchingWater() && super.canBreedWith(otherAnimal);
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverLevel, PassiveEntity ageableMob) {
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
        if (this.getVelocity().horizontalLengthSquared() > 1.0E-6) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hippo.walk", true));
            event.getController().setAnimationSpeed(1.0D);
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hippo.idle", true));
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("hippo.bite", false));
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

    static class HippoAttackBoatsGoal extends Goal {
        protected final PathAwareEntity mob;
        private final double speedModifier;
        private Path path;
        private double pathedTargetX;
        private double pathedTargetY;
        private double pathedTargetZ;
        private int ticksUntilNextPathRecalculation;
        private int ticksUntilNextAttack;
        private long lastCanUseCheck;
        private BoatEntity target;

        public HippoAttackBoatsGoal(PathAwareEntity pathfinderMob, double speedModifier) {
            this.mob = pathfinderMob;
            this.speedModifier = speedModifier;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK, Control.TARGET));
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
            List<BoatEntity> entities = this.mob.world.getNonSpectatingEntities(BoatEntity.class, this.mob.getBoundingBox().expand(8, 4, 8));
            if (!entities.isEmpty()) {
                this.target = entities.get(0);
            }
            if (this.target == null) {
                return false;
            }
            if (this.target.isRemoved()) {
                return false;
            }
            this.path = this.mob.getNavigation().findPathTo(this.target, 0);
            if (this.path != null) {
                return true;
            }
            return this.getAttackReachSqr(this.target) >= this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
        }

        @Override
        public boolean shouldContinue() {
            if (this.target == null) {
                return false;
            }
            if (this.target.isRemoved()) {
                return false;
            }
            return !this.mob.getNavigation().isIdle();
        }

        @Override
        public void start() {
            this.mob.getNavigation().startMovingAlong(this.path, this.speedModifier);
            this.mob.setAttacking(true);
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
            if (this.target == null) {
                return;
            }
            this.mob.getLookControl().lookAt(this.target, 30.0f, 30.0f);
            double d = this.mob.squaredDistanceTo(this.target.getX(), this.target.getY(), this.target.getZ());
            this.ticksUntilNextPathRecalculation = Math.max(this.ticksUntilNextPathRecalculation - 1, 0);
            if ((this.mob.getVisibilityCache().canSee(this.target)) && this.ticksUntilNextPathRecalculation <= 0 && (this.pathedTargetX == 0.0 && this.pathedTargetY == 0.0 && this.pathedTargetZ == 0.0 || this.target.squaredDistanceTo(this.pathedTargetX, this.pathedTargetY, this.pathedTargetZ) >= 1.0 || this.mob.getRandom().nextFloat() < 0.05f)) {
                this.pathedTargetX = this.target.getX();
                this.pathedTargetY = this.target.getY();
                this.pathedTargetZ = this.target.getZ();
                this.ticksUntilNextPathRecalculation = 4 + this.mob.getRandom().nextInt(7);
                if (d > 1024.0) {
                    this.ticksUntilNextPathRecalculation += 10;
                } else if (d > 256.0) {
                    this.ticksUntilNextPathRecalculation += 5;
                }
                if (!this.mob.getNavigation().startMovingTo(this.target, this.speedModifier)) {
                    this.ticksUntilNextPathRecalculation += 15;
                }
                this.ticksUntilNextPathRecalculation = this.getTickCount(this.ticksUntilNextPathRecalculation);
            }
            this.ticksUntilNextAttack = Math.max(this.ticksUntilNextAttack - 1, 0);
            this.checkAndPerformAttack(this.target, d);
        }

        protected void checkAndPerformAttack(Entity enemy, double distToEnemySqr) {
            double reach = this.getAttackReachSqr(enemy);
            if (distToEnemySqr <= reach && this.ticksUntilNextAttack <= 0) {
                this.resetAttackCooldown();
                this.mob.swingHand(Hand.MAIN_HAND);
                this.mob.tryAttack(enemy);
            }
        }

        protected void resetAttackCooldown() {
            this.ticksUntilNextAttack = this.getTickCount(20);
        }

        protected double getAttackReachSqr(Entity attackTarget) {
            return MathHelper.square(this.mob.getWidth() * 1.2f) + attackTarget.getWidth();
        }
    }
}
