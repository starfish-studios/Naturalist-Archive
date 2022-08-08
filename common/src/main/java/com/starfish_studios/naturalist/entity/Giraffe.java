package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.goal.*;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.entity.passive.HorseBaseEntity;
import net.minecraft.entity.passive.PassiveEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.recipe.Ingredient;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Arm;
import net.minecraft.util.Hand;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Giraffe extends AnimalEntity implements IAnimatable {
    private static final Ingredient FOOD_ITEMS = Ingredient.fromTag(NaturalistTags.ItemTags.GIRAFFE_FOOD_ITEMS);
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final TrackedData<Integer> TAME_TICKS = DataTracker.registerData(Giraffe.class, TrackedDataHandlerRegistry.INTEGER);

    public Giraffe(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
        this.stepHeight = 1.0f;
    }

    public static DefaultAttributeContainer.Builder createAttributes() {
        return MobEntity.createMobAttributes().add(EntityAttributes.GENERIC_MAX_HEALTH, 24.0D).add(EntityAttributes.GENERIC_MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void initGoals() {
        this.goalSelector.add(0, new SwimGoal(this));
        this.goalSelector.add(1, new EscapeDangerGoal(this, 1.5));
        this.goalSelector.add(2, new AnimalMateGoal(this, 1.0));
        this.goalSelector.add(3, new TemptGoal(this, 1.0, FOOD_ITEMS, false));
        this.goalSelector.add(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.add(5, new WanderAroundFarGoal(this, 0.7));
        this.goalSelector.add(6, new LookAtEntityGoal(this, PlayerEntity.class, 6.0f));
        this.goalSelector.add(7, new LookAroundGoal(this));
    }

    @Override
    public boolean isBreedingItem(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
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

    @Nullable
    @Override
    public PassiveEntity createChild(ServerWorld serverLevel, PassiveEntity ageableMob) {
        return NaturalistEntityTypes.GIRAFFE.get().create(serverLevel);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(TAME_TICKS, 0);
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound pCompound) {
        super.writeCustomDataToNbt(pCompound);
        pCompound.putInt("TameTicks", this.getTameTicks());
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound pCompound) {
        super.readCustomDataFromNbt(pCompound);
        this.setTameTicks(pCompound.getInt("TameTicks"));
    }

    public void setTameTicks(int ticks) {
        this.dataTracker.set(TAME_TICKS, ticks);
    }

    public int getTameTicks() {
        return this.dataTracker.get(TAME_TICKS);
    }

    public boolean isTame() {
        return this.getTameTicks() > 0;
    }

    @Override
    public boolean isPushable() {
        return !this.hasPassengers();
    }

    @Override
    public void tickMovement() {
        super.tickMovement();
        if (!this.world.isClient()) {
            if ( this.getPrimaryPassenger() != null) {
                this.setTameTicks(Math.max(0, this.getTameTicks() - 1));
                if (!this.isTame()) {
                    this.getPrimaryPassenger().stopRiding();
                    this.playSound(SoundEvents.ENTITY_LLAMA_ANGRY, this.getSoundVolume(), this.getSoundPitch());
                }
            }
        }
    }

    protected void doPlayerRide(PlayerEntity player) {
        if (!this.world.isClient) {
            player.setYaw(this.getYaw());
            player.setPitch(this.getPitch());
            player.startRiding(this);
        }
    }

    @Override
    public ActionResult interactMob(PlayerEntity player, Hand hand) {
        ItemStack stack = player.getStackInHand(hand);
        if (!this.isBaby() && this.hasPassengers()) {
            return super.interactMob(player, hand);
        }
        if (!stack.isEmpty()) {
            if (this.isBreedingItem(stack)) {
                if (this.handleEating(player, stack)) {
                    if (!player.getAbilities().creativeMode) {
                        stack.decrement(1);
                    }
                } else {
                    this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_LLAMA_ANGRY, this.getSoundCategory(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
                }
            }
            return ActionResult.success(this.world.isClient);
        }
        if (this.isBaby()) {
            return super.interactMob(player, hand);
        }
        if (this.isTame()) {
            this.doPlayerRide(player);
        }
        return ActionResult.success(this.world.isClient);
    }

    protected boolean handleEating(PlayerEntity player, ItemStack stack) {
        boolean shouldEat = false;
        float foodHealAmount = 0.0f;
        int ageUpAmount = 0;
        if (stack.isOf(Items.HAY_BLOCK)) {
            foodHealAmount = 20.0f;
            ageUpAmount = 180;
            if (!this.world.isClient() && this.getBreedingAge() == 0 && !this.isInLove()) {
                shouldEat = true;
                this.lovePlayer(player);
            }
        } else if (FOOD_ITEMS.test(stack)) {
            foodHealAmount = 2.0f;
            ageUpAmount = 20;
            if (!this.world.isClient()) {
                if (this.getTameTicks() > 0) {
                    this.world.sendEntityStatus(this, (byte)6);
                } else {
                    shouldEat = true;
                    this.setTameTicks(600);
                    this.world.sendEntityStatus(this, (byte)7);
                }
            }
        }
        if (this.getHealth() < this.getMaxHealth() && foodHealAmount > 0) {
            this.heal(foodHealAmount);
            shouldEat = true;
        }
        if (this.isBaby() && ageUpAmount > 0) {
            this.world.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), 0.0, 0.0, 0.0);
            if (!this.world.isClient) {
                this.growUp(ageUpAmount);
            }
            shouldEat = true;
        }
        if (shouldEat) {
            this.world.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.ENTITY_HORSE_EAT, this.getSoundCategory(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            this.swingHand(Hand.MAIN_HAND);
            this.emitGameEvent(GameEvent.EAT);
        }
        return shouldEat;
    }

    @Override
    public void travel(Vec3d travelVector) {
        if (!this.isAlive()) {
            return;
        }
        LivingEntity livingEntity = this.getPrimaryPassenger();
        if (!this.hasPassengers() || livingEntity == null) {
            this.airStrafingSpeed = 0.02f;
            super.travel(travelVector);
            return;
        }
        this.setYaw(livingEntity.getYaw());
        this.prevYaw = this.getYaw();
        this.setPitch(livingEntity.getPitch() * 0.5f);
        this.setRotation(this.getYaw(), this.getPitch());
        this.headYaw = this.bodyYaw = this.getYaw();
        float f = livingEntity.sidewaysSpeed * 0.5f;
        float g = livingEntity.forwardSpeed;
        this.airStrafingSpeed = this.getMovementSpeed() * 0.1f;
        if (this.isLogicalSideForUpdatingMovement()) {
            this.setMovementSpeed((float)this.getAttributeValue(EntityAttributes.GENERIC_MOVEMENT_SPEED));
            super.travel(new Vec3d(f, travelVector.y, g));
        } else if (livingEntity instanceof PlayerEntity) {
            this.setVelocity(Vec3d.ZERO);
        }
        this.updateLimbs(this, false);
        this.tryCheckBlockCollision();
    }

    @Override
    public boolean canBeControlledByRider() {
        return this.getPrimaryPassenger() != null;
    }

    @Override
    public boolean canBreedWith(AnimalEntity otherAnimal) {
        return !this.hasPassengers() && !this.hasVehicle() && !this.isBaby() && super.canBreedWith(otherAnimal);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.hasPassengers();
    }

    protected void spawnTamingParticles(boolean tamed) {
        DefaultParticleType particleOptions = tamed ? ParticleTypes.HEART : ParticleTypes.SMOKE;
        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.world.addParticle(particleOptions, this.getParticleX(1.0), this.getRandomBodyY() + 0.5, this.getParticleZ(1.0), d, e, f);
        }
    }

    @Override
    public void handleStatus(byte id) {
        if (id == 7) {
            this.spawnTamingParticles(true);
        } else if (id == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleStatus(id);
        }
    }


    @Override
    public void updatePassengerPosition(Entity passenger) {
        super.updatePassengerPosition(passenger);
        if (passenger instanceof MobEntity mob) {
            this.bodyYaw = mob.bodyYaw;
        }
        passenger.setPosition(this.getX(), this.getY() + this.getMountedHeightOffset() + passenger.getHeightOffset(), this.getZ());
        if (passenger instanceof LivingEntity livingEntity) {
            livingEntity.bodyYaw = this.bodyYaw;
        }
    }

    @Override
    public double getMountedHeightOffset() {
        return this.getHeight() * 0.6;
    }

    @Override
    public boolean isClimbing() {
        return false;
    }

    @Override
    @Nullable
    public LivingEntity getPrimaryPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }


    @Nullable
    private Vec3d getDismountLocationInDirection(Vec3d direction, LivingEntity passenger) {
        double d = this.getX() + direction.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + direction.z;
        BlockPos.Mutable mutableBlockPos = new BlockPos.Mutable();
        block0: for (EntityPose pose : passenger.getPoses()) {
            mutableBlockPos.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;
            do {
                Vec3d vec3;
                Box aABB;
                double h = this.world.getDismountHeight(mutableBlockPos);
                if ((double)mutableBlockPos.getY() + h > g) continue block0;
                if (Dismounting.canDismountInBlock(h) && Dismounting.canPlaceEntityAt(this.world, passenger, (aABB = passenger.getBoundingBox(pose)).offset(vec3 = new Vec3d(d, (double)mutableBlockPos.getY() + h, f)))) {
                    passenger.setPose(pose);
                    return vec3;
                }
                mutableBlockPos.move(Direction.UP);
            } while ((double)mutableBlockPos.getY() < g);
        }
        return null;
    }

    @Override
    public Vec3d updatePassengerForDismount(LivingEntity passenger) {
        Vec3d vec3 = HorseBaseEntity.getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.RIGHT ? 90.0f : -90.0f));
        Vec3d vec32 = this.getDismountLocationInDirection(vec3, passenger);
        if (vec32 != null) {
            return vec32;
        }
        Vec3d vec33 = HorseBaseEntity.getPassengerDismountOffset(this.getWidth(), passenger.getWidth(), this.getYaw() + (passenger.getMainArm() == Arm.LEFT ? 90.0f : -90.0f));
        Vec3d vec34 = this.getDismountLocationInDirection(vec33, passenger);
        if (vec34 != null) {
            return vec34;
        }
        return this.getPos();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.GIRAFFE_AMBIENT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getVelocity().horizontalLengthSquared() > 1.0E-6) {
            if (this.isSprinting() || !this.getPassengerList().isEmpty()) {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("giraffe.run", true));
                event.getController().setAnimationSpeed(2.0D);
            } else {
                event.getController().setAnimation(new AnimationBuilder().addAnimation("giraffe.walk", true));
                event.getController().setAnimationSpeed(1.0D);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().addAnimation("giraffe.idle", true));
            event.getController().setAnimationSpeed(1.0D);
        }
        return PlayState.CONTINUE;
    }

    private <E extends IAnimatable> PlayState eatPredicate(AnimationEvent<E> event) {
        if (this.handSwinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("giraffe.eat", false));
            this.handSwinging = false;
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.setResetSpeedInTicks(10);
        data.addAnimationController(new AnimationController<>(this, "controller", 10, this::predicate));
        data.addAnimationController(new AnimationController<>(this, "eatController", 0, this::eatPredicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
