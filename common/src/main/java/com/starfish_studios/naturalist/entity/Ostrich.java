package com.starfish_studios.naturalist.entity;

import com.starfish_studios.naturalist.entity.ai.goal.AlertOthersPanicGoal;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistSoundEvents;
import com.starfish_studios.naturalist.registry.NaturalistTags;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.Level;
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

import java.util.*;

public class Ostrich extends Animal implements IAnimatable, ItemSteerable, Saddleable {
    @Nullable
    protected Animal partner;
    private int loveTime;
    private static final EntityDataAccessor<Boolean> DATA_SADDLE_ID;
    private static final EntityDataAccessor<Integer> DATA_BOOST_TIME;
    private static final EntityDataAccessor<Optional<UUID>> DATA_ID_OWNER_UUID;
    private static final EntityDataAccessor<Byte> DATA_ID_FLAGS;
    private final ItemBasedSteering steering;
    protected int temper;
    private final AnimationFactory factory = GeckoLibUtil.createFactory(this);
    private int panicTicks = 0;

    public Ostrich(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
        this.maxUpStep = 2.0F;
        this.steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);
    }

    // GOALS/ATTRIBUTES/BREEDING

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(ServerLevel level, AgeableMob mob) {
        return NaturalistEntityTypes.OSTRICH.get().create(level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 8.0D).add(Attributes.MOVEMENT_SPEED, 0.2F);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RunAroundLikeCrazyGoal(this, 1.7D));
        this.goalSelector.addGoal(2, new AlertOthersPanicGoal(this, 1.7D));
        this.goalSelector.addGoal(3, new BreedGoal(this, 1.0D));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.CARROT_ON_A_STICK), false));
        this.goalSelector.addGoal(4, new TemptGoal(this, 1.2D, Ingredient.of(Items.APPLE), true));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Player.class, 8.0F, 1.5D, 2.0D, livingEntity -> EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingEntity) && !livingEntity.isDiscrete()));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Monster.class, 4.0F, 1.5D, 2.0D));
        this.goalSelector.addGoal(5, new AvoidEntityGoal<>(this, Animal.class, 10.0F, 1.5D, 2.0D, livingEntity -> livingEntity.getType().is(NaturalistTags.EntityTypes.OSTRICH_PREDATORS)));
        this.goalSelector.addGoal(6, new FollowParentGoal(this, 1.4D));
        this.goalSelector.addGoal(7, new WaterAvoidingRandomStrollGoal(this, 1.0D));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 6.0F));
        this.goalSelector.addGoal(9, new RandomLookAroundGoal(this));
    }

    public void onSyncedDataUpdated(EntityDataAccessor<?> key) {
        if (DATA_BOOST_TIME.equals(key) && this.level.isClientSide) {
            this.steering.onSynced();
        }

        super.onSyncedDataUpdated(key);
    }

    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_SADDLE_ID, false);
        this.entityData.define(DATA_BOOST_TIME, 0);
        this.entityData.define(DATA_ID_FLAGS, (byte)0);
        this.entityData.define(DATA_ID_OWNER_UUID, Optional.empty());
    }

    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("Tame", this.isTamed());
        if (this.getOwnerUUID() != null) {
            compound.putUUID("Owner", this.getOwnerUUID());
        }
        compound.putInt("Temper", this.getTemper());
        this.steering.addAdditionalSaveData(compound);
    }

    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setTamed(compound.getBoolean("Tame"));
        UUID uUID;
        if (compound.hasUUID("Owner")) {
            uUID = compound.getUUID("Owner");
        } else {
            String string = compound.getString("Owner");
            uUID = OldUsersConverter.convertMobOwnerIfNecessary(Objects.requireNonNull(this.getServer()), string);
        }
        this.steering.readAdditionalSaveData(compound);
        this.setTemper(compound.getInt("Temper"));
    }
    
    protected boolean getFlag(int flagId) {
        return ((Byte)this.entityData.get(DATA_ID_FLAGS) & flagId) != 0;
    }

    protected void setFlag(int flagId, boolean value) {
        byte b = this.entityData.get(DATA_ID_FLAGS);
        if (value) {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b | flagId));
        } else {
            this.entityData.set(DATA_ID_FLAGS, (byte)(b & ~flagId));
        }

    }

    public boolean isTamed() {
        return this.getFlag(2);
    }

    public void tameWithName(Player player) {
        this.setOwnerUUID(player.getUUID());
        this.setTamed(true);
        if (player instanceof ServerPlayer) {
            CriteriaTriggers.TAME_ANIMAL.trigger((ServerPlayer)player, this);
        }

        this.level.broadcastEntityEvent(this, (byte)7);
    }

    @Nullable
    public UUID getOwnerUUID() {
        return (UUID)((Optional)this.entityData.get(DATA_ID_OWNER_UUID)).orElse((Object)null);
    }

    public void setOwnerUUID(@Nullable UUID uuid) {
        this.entityData.set(DATA_ID_OWNER_UUID, Optional.ofNullable(uuid));
    }


    public void setTamed(boolean tamed) {
        this.setFlag(2, tamed);
    }

    protected void spawnTamingParticles(boolean tamed) {
        ParticleOptions particleOptions = tamed ? ParticleTypes.HEART : ParticleTypes.SMOKE;

        for(int i = 0; i < 7; ++i) {
            double d = this.random.nextGaussian() * 0.02;
            double e = this.random.nextGaussian() * 0.02;
            double f = this.random.nextGaussian() * 0.02;
            this.level.addParticle(particleOptions, this.getRandomX(1.0), this.getRandomY() + 0.5, this.getRandomZ(1.0), d, e, f);
        }

    }

    public void makeMad() {
        this.level.playSound(null, this.getX(), this.getY(), this.getZ(), SoundEvents.LLAMA_ANGRY, this.getSoundSource(), 1.0f, 1.0f + (this.random.nextFloat() - this.random.nextFloat()) * 0.2f);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        // return this.isBaby() ? NaturalistSoundEvents.DEER_HURT_BABY.get() : NaturalistSoundEvents.DEER_HURT.get();
        return null;
    }

    @Nullable
    @Override
    protected SoundEvent getAmbientSound() {
        // return this.isBaby() ? NaturalistSoundEvents.DEER_AMBIENT_BABY.get() : NaturalistSoundEvents.DEER_AMBIENT.get();
        return NaturalistSoundEvents.OSTRICH_AMBIENT.get();
    }
    @Override
    public float getVoicePitch() {
        return this.isBaby() ? (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 1.5F : (this.random.nextFloat() - this.random.nextFloat()) * 0.2F + 0.6F;
    }

    @Override
    public boolean isFood(ItemStack pStack) {
        return pStack.is(Items.APPLE);
    }

    // EATING

    @Override
    public void aiStep() {
        super.aiStep();
    }

    protected void doPlayerRide(Player player) {
        if (!this.level.isClientSide) {
            player.setYRot(this.getYRot());
            player.setXRot(this.getXRot());
            player.startRiding(this);
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
        return this.getBbHeight() * 1.15;
    }

    @Nullable
    @Override
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

    public InteractionResult mobInteract(Player player, InteractionHand hand) {
        boolean foodItem = this.isFood(player.getItemInHand(hand));
        if (!foodItem && this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive()) {
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

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && !this.isBaby();
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }

    }

    @Override
    public boolean isSaddled() {
        return this.steering.hasSaddle();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource source) {
        this.steering.setSaddle(true);
        if (source != null) {
            this.level.playSound((Player)null, this, SoundEvents.PIG_SADDLE, source, 0.5F, 1.0F);
        }

    }

    public void travel(Vec3 travelVector) {
        this.travel(this, this.steering, travelVector);
    }

    public float getSteeringSpeed() {
        return (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED) * 1.6F;
    }

    public void travelWithInput(Vec3 travelVector) {
        super.travel(travelVector);
    }

    public boolean boost() {
        return this.steering.boost(this.getRandom());
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
    public void ate() {
        if (this.isBaby()) {
            this.ageUp(60);
        }
    }

    // MOVEMENT

    @Override
    public void customServerAiStep() {
        if (this.getMoveControl().hasWanted()) {
            this.setSprinting(this.getMoveControl().getSpeedModifier() >= 1.5D);
        } else {
            this.setSprinting(false);
        }
        super.customServerAiStep();
    }

    // PANICKING

    @Override
    public boolean hurt(DamageSource pSource, float pAmount) {
        boolean lastHurt = super.hurt(pSource, pAmount);
        if (lastHurt) {
            int ticks = 100 + this.random.nextInt(100);
            this.panicTicks = ticks;
            List<? extends Ostrich> ostriches = this.level.getEntitiesOfClass(Ostrich.class, this.getBoundingBox().inflate(8.0D, 4.0D, 8.0D));
            for (Ostrich ostrich : ostriches) {
                ostrich.panicTicks = ticks;
            }
        }
        return lastHurt;
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            if (panicTicks >= 0) {
                panicTicks--;
            }
            if (panicTicks == 0 && this.getLastHurtByMob() != null) {
                this.setLastHurtByMob(null);
            }
        }
    }
    
    // TEMPER

    public int getMaxTemper() {
        return 100;
    }

    public int getTemper() {
        return this.temper;
    }

    public void setTemper(int temper) {
        this.temper = temper;
    }

    public int modifyTemper(int addedTemper) {
        int i = Mth.clamp(this.getTemper() + addedTemper, 0, this.getMaxTemper());
        this.setTemper(i);
        return i;
    }

    // ANIMATION

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        /* if (this.loveTime >= 40 && !this.isBaby()) {
            event.getController().setAnimation(new AnimationBuilder().loop("dance"));
            event.getController().setAnimationSpeed(1.0D);
            return PlayState.CONTINUE;
        } */
        if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6) {
            if (this.isSprinting()) {
                if (this.isBaby() || this.hasControllingPassenger()) {
                    event.getController().setAnimationSpeed(2.7D);
                }
                event.getController().setAnimation(new AnimationBuilder().loop("run"));
                event.getController().setAnimationSpeed(2.0D);
            } else {
                if (this.isBaby()) {
                    event.getController().setAnimationSpeed(1.3D);
                }
                event.getController().setAnimation(new AnimationBuilder().loop("walk"));
                event.getController().setAnimationSpeed(1.0D);
            }
        } else {
            event.getController().setAnimation(new AnimationBuilder().loop("idle"));
            event.getController().setAnimationSpeed(0.5D);
        }
        return PlayState.CONTINUE;
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

    public static class RunAroundLikeCrazyGoal extends Goal {
        private final Ostrich mob;
        private final double speedModifier;
        private double posX;
        private double posY;
        private double posZ;

        public RunAroundLikeCrazyGoal(Animal mob, double d) {
            this.mob = (Ostrich) mob;
            this.speedModifier = d;
            this.setFlags(EnumSet.of(Flag.MOVE));
        }

        public boolean canUse() {
            if (!this.mob.isTamed() && this.mob.isVehicle()) {
                Vec3 vec3 = DefaultRandomPos.getPos(this.mob, 5, 4);
                if (vec3 == null) {
                    return false;
                } else {
                    this.posX = vec3.x;
                    this.posY = vec3.y;
                    this.posZ = vec3.z;
                    return true;
                }
            } else {
                return false;
            }
        }

        public void start() {
            this.mob.getNavigation().moveTo(this.posX, this.posY, this.posZ, this.speedModifier);
        }

        public boolean canContinueToUse() {
            return !this.mob.isTamed() && !this.mob.getNavigation().isDone() && this.mob.isVehicle();
        }

        public void tick() {
            if (!this.mob.isTamed() && this.mob.getRandom().nextInt(this.adjustedTickDelay(50)) == 0) {
                Entity entity = this.mob.getPassengers().get(0);
                if (entity == null) {
                    return;
                }

                if (entity instanceof Player) {
                    int i = this.mob.getTemper();
                    int j = this.mob.getMaxTemper();
                    if (j > 0 && this.mob.getRandom().nextInt(j) < i) {
                        this.mob.tameWithName((Player)entity);
                        return;
                    }

                    this.mob.modifyTemper(5);
                }

                this.mob.ejectPassengers();
                this.mob.makeMad();
                this.mob.level.broadcastEntityEvent(this.mob, (byte)6);
            }

        }

    }


    static {
        DATA_SADDLE_ID = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BOOLEAN);
        DATA_BOOST_TIME = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.INT);
        DATA_ID_OWNER_UUID = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.OPTIONAL_UUID);
        DATA_ID_FLAGS = SynchedEntityData.defineId(Ostrich.class, EntityDataSerializers.BYTE);
    }

}
