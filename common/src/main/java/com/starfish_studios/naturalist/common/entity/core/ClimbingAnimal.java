package com.starfish_studios.naturalist.common.entity.core;

import com.starfish_studios.naturalist.common.entity.core.ai.navigation.BetterWallClimberNavigation;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;

public abstract class ClimbingAnimal extends Animal {
    private static final EntityDataAccessor<Byte> CLIMB_FLAG = SynchedEntityData.defineId(ClimbingAnimal.class, EntityDataSerializers.BYTE);

    protected ClimbingAnimal(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(CLIMB_FLAG, (byte)0);
    }

    @Override
    protected PathNavigation createNavigation(Level pLevel) {
        return new BetterWallClimberNavigation(this, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.level.isClientSide) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.horizontalCollision && this.onClimbable()) {
            this.setDeltaMovement(this.getDeltaMovement().x, this.getDeltaMovement().y * this.getClimbSpeedMultiplier(), this.getDeltaMovement().z);
        }
    }

    @Override
    public boolean onClimbable() {
        return this.isClimbing();
    }

    public boolean isClimbing() {
        return (this.entityData.get(CLIMB_FLAG) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte flag = this.entityData.get(CLIMB_FLAG);
        if (pClimbing) {
            flag = (byte)(flag | 1);
        } else {
            flag = (byte)(flag & -2);
        }

        this.entityData.set(CLIMB_FLAG, flag);
    }

    @Override
    protected float getJumpPower() {
        return 0.0F;
    }

    protected float getClimbSpeedMultiplier() {
        return 1.0F;
    }
}
