package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.DismountHelper;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.AnimationState;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

public class Giraffe extends Animal implements IAnimatable {
    private static final Ingredient FOOD_ITEMS = Ingredient.of(NaturalistTags.ItemTags.GIRAFFE_FOOD_ITEMS);
    private final AnimationFactory factory = new AnimationFactory(this);
    private static final EntityDataAccessor<Integer> TAME_TICKS = SynchedEntityData.defineId(Giraffe.class, EntityDataSerializers.INT);

    public Giraffe(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 1.0f;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 24.0D).add(Attributes.MOVEMENT_SPEED, 0.25F);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new PanicGoal(this, 1.5));
        this.goalSelector.addGoal(2, new BreedGoal(this, 1.0));
        this.goalSelector.addGoal(3, new TemptGoal(this, 1.0, FOOD_ITEMS, false));
        this.goalSelector.addGoal(4, new FollowParentGoal(this, 1.0));
        this.goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 0.7));
        this.goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0f));
        this.goalSelector.addGoal(7, new RandomLookAroundGoal(this));
    }

    @Override
    public boolean isFood(ItemStack stack) {
        return FOOD_ITEMS.test(stack);
    }

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.5D);
        } else {
            this.setSprinting(false);
        }
        super.customServerAiStep();
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel serverLevel, AgeableMob ageableMob) {
        return NaturalistEntityTypes.GIRAFFE.get().create(serverLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(TAME_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("TameTicks", this.getTameTicks());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setTameTicks(pCompound.getInt("TameTicks"));
    }

    public void setTameTicks(int ticks) {
        this.entityData.set(TAME_TICKS, ticks);
    }

    public int getTameTicks() {
        return this.entityData.get(TAME_TICKS);
    }

    public boolean isTame() {
        return this.getTameTicks() > 0;
    }

    @Override
    public boolean isPushable() {
        return !this.isVehicle();
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (!this.level.isClientSide()) {
            if ( this.getControllingPassenger() != null) {
                this.setTameTicks(Math.max(0, this.getTameTicks() - 1));
                if (!this.isTame()) {
                    this.getControllingPassenger().stopRiding();
                    this.playSound(SoundEvents.LLAMA_ANGRY, this.getSoundVolume(), this.getVoicePitch());
                }
            }
        }
    }

    protected void doPlayerRide(Player player) {
        if (!this.level.isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
        }
    }

    @Override
    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (!this.isBaby() && this.isVehicle()) {
            return super.mobInteract(player, hand);
        }
        if (!stack.isEmpty()) {
            if (this.isFood(stack)) {
                if (this.handleEating(player, stack)) {
                    if (!player.getAbilities().instabuild) {
                        stack.shrink(1);
                    }
                } else {
                    this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_ANGRY, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
                }
            }
            return InteractionResult.sidedSuccess(this.level.isClientSide);
        }
        if (this.isBaby()) {
            return super.mobInteract(player, hand);
        }
        if (this.isTame()) {
            this.doPlayerRide(player);
        }
        return InteractionResult.sidedSuccess(this.level.isClientSide);
    }

    protected boolean handleEating(Player player, ItemStack stack) {
        boolean shouldEat = false;
        float foodHealAmount = 0.0f;
        int ageUpAmount = 0;
        if (stack.is(Items.HAY_BLOCK)) {
            foodHealAmount = 20.0f;
            ageUpAmount = 180;
            if (!this.level.isClientSide() && this.getAge() == 0 && !this.isInLove()) {
                shouldEat = true;
                this.setInLove(player);
            }
        } else if (FOOD_ITEMS.test(stack)) {
            foodHealAmount = 2.0f;
            ageUpAmount = 20;
            if (!this.level.isClientSide()) {
                if (this.getTameTicks() > 0) {
                    this.level.broadcastEntityEvent(this, (byte)6);
                } else {
                    shouldEat = true;
                    this.setTameTicks(600);
                    this.level.broadcastEntityEvent(this, (byte)7);
                }
            }
        }
        if (this.getHealth() < this.getMaxHealth() && foodHealAmount > 0) {
            this.heal(foodHealAmount);
            shouldEat = true;
        }
        if (this.isBaby() && ageUpAmount > 0) {
            this.level.addParticle(ParticleTypes.HAPPY_VILLAGER, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), 0.0, 0.0, 0.0);
            if (!this.level.isClientSide) {
                this.ageUp(ageUpAmount);
            }
            shouldEat = true;
        }
        if (shouldEat) {
            this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.HORSE_EAT, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
            this.swing(InteractionHand.MAIN_HAND);
            this.gameEvent(GameEvent.EAT);
        }
        return shouldEat;
    }

    @Override
    public void travel(Vec3 travelVector) {
        if (!this.isAlive()) {
            return;
        }
        LivingEntity livingEntity = this.getControllingPassenger();
        if (!this.isVehicle() || livingEntity == null) {
            this.flyingSpeed = 0.02f;
            super.travel(travelVector);
            return;
        }
        this.setYRot(livingEntity.getYRot());
        this.yRotO = this.getYRot();
        this.setXRot(livingEntity.getXRot() * 0.5f);
        this.setRot(this.getYRot(), this.getXRot());
        this.yHeadRot = this.yBodyRot = this.getYRot();
        float f = livingEntity.xxa * 0.5f;
        float g = livingEntity.zza;
        this.flyingSpeed = this.getSpeed() * 0.1f;
        if (this.isControlledByLocalInstance()) {
            this.setSpeed((float)this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            super.travel(new Vec3(f, travelVector.y, g));
        } else if (livingEntity instanceof Player) {
            this.setDeltaMovement(Vec3.ZERO);
        }
        this.calculateEntityAnimation(this, false);
        this.tryCheckInsideBlocks();
    }

    @Override
    public boolean canMate(Animal otherAnimal) {
        return !this.isVehicle() && !this.isPassenger() && !this.isBaby() && super.canMate(otherAnimal);
    }

    @Override
    protected boolean isImmobile() {
        return super.isImmobile() && this.isVehicle();
    }

    protected void spawnTamingParticles(boolean tamed) {
        SimpleParticleType particleOptions = tamed ? ParticleTypes.HEART : ParticleTypes.SMOKE;
        for (int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level.addParticle(particleOptions, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
        }
    }

    @Override
    public void handleEntityEvent(byte id) {
        if (id == 7) {
            this.spawnTamingParticles(true);
        } else if (id == 6) {
            this.spawnTamingParticles(false);
        } else {
            super.handleEntityEvent(id);
        }
    }


    @Override
    public void positionRider(Entity passenger) {
        super.positionRider(passenger);
        if (passenger instanceof Mob mob) {
            this.yBodyRot = mob.yBodyRot;
        }
        passenger.setPos(this.getX(), this.getY() + this.getPassengersRidingOffset() + passenger.getMyRidingOffset(), this.getZ());
        if (passenger instanceof LivingEntity livingEntity) {
            livingEntity.yBodyRot = this.yBodyRot;
        }
    }

    @Override
    public double getPassengersRidingOffset() {
        return this.getBbHeight() * 0.6;
    }

    @Override
    public boolean onClimbable() {
        return false;
    }

    @Override
    @Nullable
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        if (entity instanceof LivingEntity livingEntity) {
            return livingEntity;
        }
        return null;
    }


    @Nullable
    private Vec3 getDismountLocationInDirection(Vec3 direction, LivingEntity passenger) {
        double d = this.getX() + direction.x;
        double e = this.getBoundingBox().minY;
        double f = this.getZ() + direction.z;
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos();
        block0: for (Pose pose : passenger.getDismountPoses()) {
            mutableBlockPos.set(d, e, f);
            double g = this.getBoundingBox().maxY + 0.75;
            do {
                Vec3 vec3;
                AABB aABB;
                double h = this.level.getBlockFloorHeight(mutableBlockPos);
                if ((double)mutableBlockPos.getY() + h > g) continue block0;
                if (DismountHelper.isBlockFloorValid(h) && DismountHelper.canDismountTo(this.level, passenger, (aABB = passenger.getLocalBoundsForPose(pose)).move(vec3 = new Vec3(d, (double)mutableBlockPos.getY() + h, f)))) {
                    passenger.setPose(pose);
                    return vec3;
                }
                mutableBlockPos.move(Direction.UP);
            } while ((double)mutableBlockPos.getY() < g);
        }
        return null;
    }

    @Override
    public Vec3 getDismountLocationForPassenger(LivingEntity passenger) {
        Vec3 vec3 = AbstractHorse.getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.RIGHT ? 90.0f : -90.0f));
        Vec3 vec32 = this.getDismountLocationInDirection(vec3, passenger);
        if (vec32 != null) {
            return vec32;
        }
        Vec3 vec33 = AbstractHorse.getCollisionHorizontalEscapeVector(this.getBbWidth(), passenger.getBbWidth(), this.getYRot() + (passenger.getMainArm() == HumanoidArm.LEFT ? 90.0f : -90.0f));
        Vec3 vec34 = this.getDismountLocationInDirection(vec33, passenger);
        if (vec34 != null) {
            return vec34;
        }
        return this.position();
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        return NaturalistSoundEvents.GIRAFFE_AMBIENT.get();
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting() || !this.getPassengers().isEmpty()) {
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
        if (this.swinging && event.getController().getAnimationState().equals(AnimationState.Stopped)) {
            event.getController().markNeedsReload();
            event.getController().setAnimation(new AnimationBuilder().addAnimation("giraffe.eat", false));
            this.swinging = false;
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
