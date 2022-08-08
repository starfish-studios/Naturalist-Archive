package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.BabyHurtByTargetGoal;
import com.starfish_studios.naturalist.entity.ai.goal.BabyPanicGoal;
import com.starfish_studios.naturalist.entity.ai.navigation.BetterGroundPathNavigation;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityData;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.ai.goal.FollowParentGoal;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.LookAroundGoal;
import net.minecraft.entity.ai.goal.LookAtEntityGoal;
import net.minecraft.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.entity.ai.goal.SwimGoal;
import net.minecraft.entity.ai.goal.WanderAroundFarGoal;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.Path;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
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
import net.minecraft.particle.ParticleTypes;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.GameRules;
import net.minecraft.world.LocalDifficulty;
import net.minecraft.world.ServerWorldAccess;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.SoundKeyframeEvent;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import java.util.EnumSet;
import java.util.List;

public class Rhino extends AnimalEntity implements IAnimatable {
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> CHARGE_COOLDOWN_TICKS = DataTracker.registerData(Rhino.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Boolean> HAS_TARGET = DataTracker.registerData(Rhino.class, TrackedDataHandlerRegistry.BOOLEAN);
    private int stunnedTick;
    private boolean canBePushed = true;

    public Rhino(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.stepHeight = 1.0f;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 40.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.2D).add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 6.0D).add(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE, 0.6D).add(EntityAttributes.GENERIC_FOLLOW_RANGE, 12.0D);
    }

    @Override
    public EntityData initialize(ServerWorldAccess pLevel, LocalDifficulty pDifficulty, SpawnReason pReason, @javax.annotation.Nullable EntityData pSpawnData, @javax.annotation.Nullable NbtCompound pDataTag) {
        if (pSpawnData == null) {
            pSpawnData = new PassiveData(1.0F);
        }

        return super.initialize(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return false;
    }

    @Override
    protected EntityNavigation createNavigation(World level) {
        return new BetterGroundPathNavigation(this, level);
    }

    @Override
    protected void initGoals() {
        super.initGoals();
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new RhinoMeleeAttackGoal(this, 1.2D, false));
        this.goalSelector.add(2, new RhinoPrepareChargeGoal(this));
        this.goalSelector.add(3, new RhinoChargeGoal(this, 2.5F));
        this.goalSelector.add(3, new BabyPanicGoal(this, 2.0D));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.1));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 1.0));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
        this.targetSelector.add(1, new BabyHurtByTargetGoal(this));
        this.targetSelector.add(2, new RhinoNearestAttackablePlayerTargetGoal(this));
    }

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverLevel, PassiveEntity ageableMob) {
        return NaturalistEntityTypes.RHINO.get().create(serverLevel);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CHARGE_COOLDOWN_TICKS, 0);
        this.dataTracker.startTracking(HAS_TARGET, false);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound compound) {
        super.writeCustomDataToNbt(compound);
        compound.putInt("StunTick", this.stunnedTick);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound compound) {
        super.readCustomDataFromNbt(compound);
        this.stunnedTick = compound.getInt("StunTick");
    }

    public void setChargeCooldownTicks(int ticks) {
        this.dataTracker.set(CHARGE_COOLDOWN_TICKS, ticks);
    }

    public int getChargeCooldownTicks() {
        return this.dataTracker.get(CHARGE_COOLDOWN_TICKS);
    }

    public boolean hasChargeCooldown() {
        return this.dataTracker.get(CHARGE_COOLDOWN_TICKS) > 0;
    }

    public void resetChargeCooldownTicks() {
        this.dataTracker.set(CHARGE_COOLDOWN_TICKS, 50);
    }

    public void setHasTarget(boolean hasTarget) {
        this.dataTracker.set(HAS_TARGET, hasTarget);
    }

    public boolean hasTarget() {
        return this.dataTracker.get(HAS_TARGET);
    }

    @Override
    public int getMaxHeadRotation() {
        return this.isSprinting() ? 1 : 50;
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.isAlive()) {
            return;
        }
        this.getAttributeInstance(EntityAttributes.GENERIC_MOVEMENT_SPEED).setBaseValue(this.isImmobile() ? 0.0 : 0.2);
        if (this.stunnedTick > 0) {
            --this.stunnedTick;
            this.stunEffect();
        }
    }

    private void stunEffect() {
        if (this.random.nextInt(6) == 0) {
            double d = this.getX() - (double)this.getWidth() * Math.sin(this.bodyYaw * ((float)Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            double e = this.getY() + (double)this.getHeight() - 0.3;
            double f = this.getZ() + (double)this.getWidth() * Math.cos(this.bodyYaw * ((float)Math.PI / 180)) + (this.random.nextDouble() * 0.6 - 0.3);
            this.world.addParticle(ParticleTypes.ENTITY_EFFECT, d, e, f, 0.4980392156862745, 0.5137254901960784, 0.5725490196078431);
        }
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() || this.stunnedTick > 0;
    }

    @Override
    protected void knockback(LivingEntity defender) {
        this.stunnedTick = 60;
        this.resetChargeCooldownTicks();
        this.getNavigation().stop();
        this.playSound(SoundEvents.ENTITY_RAVAGER_STUNNED, 1.0f, 1.0f);
        this.world.sendEntityStatus(this, (byte)39);
        defender.pushAwayFrom(this);
        defender.velocityModified = true;
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 39) {
            this.stunnedTick = 60;
        }
        super.handleStatus(id);
    }

    @Override
    public void mobTick() {
        if (this.getMoveControl().isMoving()) {
            this.setSprinting(this.getMoveControl().getSpeed() >= 1.5D);
        } else {
            this.setSprinting(false);
        }
        super.mobTick();
    }

    private boolean isWithinYRange(LivingEntity target) {
        if (target == null) {
            return false;
        }
        return Math.abs(target.getY() - this.getY()) < 3;
    }

    @Override
    public boolean isPushable() {
        return this.canBePushed;
    }

    @Override
    public float getSoundPitch() {
        return (this.random.nextFloat() - this.random.nextFloat()) * 0.2f + 1.0f;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return this.isBaby() ? NaturalistSoundEvents.RHINO_AMBIENT_BABY.get() : NaturalistSoundEvents.RHINO_AMBIENT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.stunnedTick > 0) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rhino.stunned", true));
            event.getController().setAnimationSpeed(1.0F);
        } else if (event.isMoving()) {
            if (this.isSprinting()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("rhino.run", true));
                event.getController().setAnimationSpeed(3.0F);
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("rhino.walk", true));
                event.getController().setAnimationSpeed(1.0F);
            }
        } else if (this.hasChargeCooldown() && this.hasTarget()) {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rhino.foot", true));
            event.getController().setAnimationSpeed(1.0F);
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rhino.idle", true));
            event.getController().setAnimationSpeed(1.0F);
        }
        return PlayState.CONTINUE;
    }

    private void soundListener(SoundKeyframeEvent<Rhino> event) {
        Rhino rhino = event.getEntity();
        if (rhino.world.isClient) {
            if (event.sound.equals("scrape")) {
                rhino.world.playSound(rhino.getX(), rhino.getY(), rhino.getZ(), NaturalistSoundEvents.RHINO_SCRAPE.get(), rhino.getSoundCategory(), 1.0F, rhino.getSoundPitch(), false);
            }
        }
    }

    private <E extends IAnimatable> PlayState attackPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("rhino.attack", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(5);
        AnimationController<Rhino> controller = new AnimationController<>(this, "controller", 5, this::predicate);
        controller.registerSoundListener(this::soundListener);
        data.addAnimationController(controller);
        data.addAnimationController(new AnimationController<>(this, "attackController", 0, this::attackPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }

    static class RhinoPrepareChargeGoal extends Goal {
        protected final Rhino rhino;

        public RhinoPrepareChargeGoal(Rhino rhino) {
            this.rhino = rhino;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.rhino.getTarget();
            if (target == null || !target.isAlive() || this.rhino.stunnedTick > 0 || !this.rhino.isWithinYRange(target)) {
                this.rhino.resetChargeCooldownTicks();
                return false;
            }
            return target instanceof PlayerEntity && rhino.hasChargeCooldown();
        }

        @Override
        public void start() {
            LivingEntity target = this.rhino.getTarget();
            if (target == null) {
                return;
            }
            this.rhino.setHasTarget(true);
            this.rhino.resetChargeCooldownTicks();
            this.rhino.canBePushed = false;
        }

        @Override
        public void stop() {
            this.rhino.setHasTarget(false);
            this.rhino.canBePushed = true;
        }

        @Override
        public void tick() {
            LivingEntity target = this.rhino.getTarget();
            if (target == null) {
                return;
            }
            this.rhino.getLookControl().lookAt(target);
            this.rhino.setChargeCooldownTicks(Math.max(0, this.rhino.getChargeCooldownTicks() - 1));
        }
    }

    static class RhinoChargeGoal extends Goal {
        protected final Rhino mob;
        private final double speedModifier;
        private Path path;
        private Vec3d chargeDirection;

        public RhinoChargeGoal(Rhino pathfinderMob, double speedModifier) {
            this.mob = pathfinderMob;
            this.speedModifier = speedModifier;
            this.setControls(EnumSet.of(Control.MOVE, Control.LOOK));
            this.chargeDirection = Vec3d.ZERO;
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || this.mob.hasChargeCooldown() || this.mob.stunnedTick > 0) {
                return false;
            }
            this.path = this.mob.getNavigation().findPathTo(target, 0);
            return target instanceof PlayerEntity && this.path != null;
        }

        @Override
        public boolean shouldContinue() {
            LivingEntity target = this.mob.getTarget();
            if (target == null || !target.isAlive() || this.mob.hasChargeCooldown() || this.mob.stunnedTick > 0) {
                return false;
            }
            return !this.mob.getNavigation().isIdle();
        }

        @Override
        public void start() {
            BlockPos blockPosition = this.mob.getBlockPos();
            BlockPos target = this.path.getTarget();
            this.chargeDirection = new Vec3d(blockPosition.getX() - target.getX(), 0.0, blockPosition.getZ() - target.getZ()).normalize();
            this.mob.getNavigation().startMovingAlong(this.path, this.speedModifier);
            this.mob.setAttacking(true);
        }

        @Override
        public void stop() {
            this.mob.resetChargeCooldownTicks();
            this.mob.getNavigation().stop();
        }

        @Override
        public boolean shouldRunEveryTick() {
            return true;
        }

        @Override
        public void tick() {
            this.mob.getLookControl().lookAt(Vec3d.ofCenter(this.path.getTarget()));
            if (this.mob.horizontalCollision && this.mob.onGround) {
                this.mob.jump();
            }
            if (this.mob.world.getGameRules().getBoolean(GameRules.DO_MOB_GRIEFING)) {
                Box boundingBox = this.mob.getBoundingBox().expand(0.2);
                for (BlockPos pos : BlockPos.iterate(MathHelper.floor(boundingBox.minX), MathHelper.floor(boundingBox.minY), MathHelper.floor(boundingBox.minZ), MathHelper.floor(boundingBox.maxX), MathHelper.floor(boundingBox.maxY), MathHelper.floor(boundingBox.maxZ))) {
                    BlockState state = this.mob.world.getBlockState(pos);
                    if (!state.isIn(NaturalistTags.BlockTags.RHINO_CHARGE_BREAKABLE)) continue;
                    this.mob.world.breakBlock(pos, true, this.mob);
                }
            }
            if (!this.mob.world.isClient()) {
                ((ServerWorld) this.mob.world).spawnParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 5, this.mob.getWidth() / 4.0F, 0, this.mob.getWidth() / 4.0F, 0.01D);
            }
            if (this.mob.world.getTime() % 2L == 0L) {
                this.mob.playSound(SoundEvents.ENTITY_HOGLIN_STEP, 0.5F, this.mob.getSoundPitch());
            }
            this.tryToHurt();
        }

        protected void tryToHurt() {
            List<LivingEntity> nearbyEntities = this.mob.world.getTargets(LivingEntity.class, TargetPredicate.createAttackable(), this.mob, this.mob.getBoundingBox());
            if (!nearbyEntities.isEmpty()) {
                LivingEntity livingEntity = nearbyEntities.get(0);
                if (!(livingEntity instanceof Rhino)) {
                    livingEntity.damage(DamageSource.mob(this.mob), (float) this.mob.getAttributeValue(EntityAttributes.GENERIC_ATTACK_DAMAGE));
                    float speed = MathHelper.clamp(this.mob.getMovementSpeed() * 1.65f, 0.2f, 3.0f);
                    float shieldBlockModifier = livingEntity.blockedByShield(DamageSource.mob(this.mob)) ? 0.5f : 1.0f;
                    livingEntity.takeKnockback(shieldBlockModifier * speed * 2.0D, this.chargeDirection.getX(), this.chargeDirection.getZ());
                    double knockbackResistance = Math.max(0.0, 1.0 - livingEntity.getAttributeValue(EntityAttributes.GENERIC_KNOCKBACK_RESISTANCE));
                    livingEntity.setVelocity(livingEntity.getVelocity().add(0.0, 0.4f * knockbackResistance, 0.0));
                    this.mob.swingHand(Hand.MAIN_HAND);
                    if (livingEntity.equals(this.mob.getTarget())) {
                        this.stop();
                    }
                }
            }
        }
    }

    static class RhinoNearestAttackablePlayerTargetGoal extends ActiveTargetGoal<PlayerEntity> {
        private final Rhino rhino;

        public RhinoNearestAttackablePlayerTargetGoal(Rhino mob) {
            super(mob, PlayerEntity.class, 10, true, true, EntityPredicates.EXCEPT_CREATIVE_OR_SPECTATOR::test);
            this.rhino = mob;
        }

        @Override
        public boolean canStart() {
            if (this.rhino.isBaby()) {
                return false;
            }
            if (super.canStart()) {
                if (!rhino.isWithinYRange(targetEntity)) {
                    return false;
                }
                List<Rhino> nearbyEntities = this.rhino.world.getNonSpectatingEntities(Rhino.class, this.rhino.getBoundingBox().expand(8.0, 4.0, 8.0));
                for (Rhino mob : nearbyEntities) {
                    if (!mob.isBaby()) continue;
                    return true;
                }
            }
            return false;
        }
    }

    static class RhinoMeleeAttackGoal extends MeleeAttackGoal {
        public RhinoMeleeAttackGoal(PathAwareEntity pathfinderMob, double speedModifier, boolean followEvenIfNotSeen) {
            super(pathfinderMob, speedModifier, followEvenIfNotSeen);
        }

        @Override
        public boolean canStart() {
            LivingEntity target = this.mob.getTarget();
            if (target instanceof PlayerEntity) {
                return false;
            }
            return super.canStart();
        }
    }
}
