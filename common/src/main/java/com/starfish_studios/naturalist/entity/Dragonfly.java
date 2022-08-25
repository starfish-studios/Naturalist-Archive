package com.starfish_studios.naturalist.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.core.PlayState;
import software.bernie.geckolib3.core.builder.AnimationBuilder;
import software.bernie.geckolib3.core.controller.AnimationController;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.manager.AnimationData;
import software.bernie.geckolib3.core.manager.AnimationFactory;

import javax.annotation.Nullable;

public class Dragonfly extends PathfinderMob implements IAnimatable {
    private static final EntityDataAccessor<Integer> VARIANT_ID = SynchedEntityData.defineId(Dragonfly.class, EntityDataSerializers.INT);
    @Nullable
    private BlockPos targetPosition;
    private final AnimationFactory factory = new AnimationFactory(this);

    public Dragonfly(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }

    public static boolean checkDragonflySpawnRules(EntityType<Dragonfly> dragonfly, LevelAccessor level, MobSpawnType spawnType, BlockPos pos, RandomSource random) {
        BlockPos.MutableBlockPos mutable = pos.mutable();
        do {
            mutable.move(Direction.UP);
        } while (level.getFluidState(mutable).is(FluidTags.WATER));
        return level.getBlockState(mutable).isAir();
    }

    public int getVariant() {
        return Mth.clamp(this.entityData.get(VARIANT_ID), 0, 2);
    }

    public void setVariant(int variant) {
        this.entityData.set(VARIANT_ID, variant);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(VARIANT_ID, 0);
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant());
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(compound.getInt("Variant"));
    }

    @Override
    @Nullable
    public SpawnGroupData finalizeSpawn(ServerLevelAccessor level, DifficultyInstance difficulty, MobSpawnType reason, @Nullable SpawnGroupData spawnData, @Nullable CompoundTag dataTag) {
        this.setVariant(level.getRandom().nextInt(3));
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    protected void doPush(Entity entity) {
    }

    @Override
    protected void pushEntities() {
    }

    @Override
    public void tick() {
        super.tick();
        this.setDeltaMovement(this.getDeltaMovement().multiply(1.0, 0.6, 1.0));
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (!(this.targetPosition == null || this.level.isEmptyBlock(this.targetPosition) && this.targetPosition.getY() > this.level.getMinBuildHeight())) {
            this.targetPosition = null;
        }
        if (this.targetPosition == null || this.random.nextInt(30) == 0 || this.targetPosition.closerToCenterThan(this.position(), 2.0)) {
            this.targetPosition = new BlockPos(this.getX() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7), this.getY() + (double)this.random.nextInt(6) - 2.0, this.getZ() + (double)this.random.nextInt(7) - (double)this.random.nextInt(7));
        }
        double d = (double)this.targetPosition.getX() + 0.5 - this.getX();
        double e = (double)this.targetPosition.getY() + 0.1 - this.getY();
        double f = (double)this.targetPosition.getZ() + 0.5 - this.getZ();
        Vec3 vec3 = this.getDeltaMovement();
        Vec3 vec32 = vec3.add((Math.signum(d) * 0.5 - vec3.x) * (double)0.1f, (Math.signum(e) * (double)0.7f - vec3.y) * (double)0.1f, (Math.signum(f) * 0.5 - vec3.z) * (double)0.1f);
        this.setDeltaMovement(vec32);
        float g = (float)(Mth.atan2(vec32.z, vec32.x) * 57.2957763671875) - 90.0f;
        float h = Mth.wrapDegrees(g - this.getYRot());
        this.zza = 0.5f;
        this.setYRot(this.getYRot() + h);
    }

    @Override
    protected Entity.MovementEmission getMovementEmission() {
        return Entity.MovementEmission.EVENTS;
    }

    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    protected void checkFallDamage(double y, boolean onGround, BlockState state, BlockPos pos) {
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
    }

    @Override
    public float getWalkTargetValue(BlockPos blockPos) {
        if (this.level.getBlockState(blockPos).isAir()) {
            for (int i = 0; i < 4; i++) {
                if (this.level.isWaterAt(blockPos.below(i))) {
                    return 10.0F;
                }
            }
            return 5.0F;
        }
        return 0.0F;
    }

    private <E extends IAnimatable> PlayState predicate(AnimationEvent<E> event) {
        event.getController().setAnimation(new AnimationBuilder().addAnimation("dragonfly.fly", true));
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "controller", 0, this::predicate));
    }

    @Override
    public AnimationFactory getFactory() {
        return factory;
    }
}
