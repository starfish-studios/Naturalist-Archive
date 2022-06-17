package com.starfish_studios.naturalist.entity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.pathing.EntityNavigation;
import net.minecraft.entity.ai.pathing.SpiderNavigation;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.world.World;

public abstract class ClimbingAnimal extends AnimalEntity {
    private static final TrackedData<Byte> CLIMB_FLAG = DataTracker.registerData(ClimbingAnimal.class, TrackedDataHandlerRegistry.BYTE);

    protected ClimbingAnimal(EntityType<? extends AnimalEntity> entityType, World level) {
        super(entityType, level);
    }

    @Override
    protected void initDataTracker() {
        super.initDataTracker();
        this.dataTracker.startTracking(CLIMB_FLAG, (byte)0);
    }

    @Override
    protected EntityNavigation createNavigation(World pLevel) {
        return new SpiderNavigation(this, pLevel);
    }

    @Override
    public void tick() {
        super.tick();
        if (!this.world.isClient) {
            this.setClimbing(this.horizontalCollision);
        }
        if (this.horizontalCollision && this.isClimbing()) {
            this.setVelocity(this.getVelocity().x, this.getVelocity().y * this.getClimbSpeedMultiplier(), this.getVelocity().z);
        }
    }

    @Override
    public boolean isClimbing() {
        return (this.dataTracker.get(CLIMB_FLAG) & 1) != 0;
    }

    public void setClimbing(boolean pClimbing) {
        byte flag = this.dataTracker.get(CLIMB_FLAG);
        if (pClimbing) {
            flag = (byte)(flag | 1);
        } else {
            flag = (byte)(flag & -2);
        }

        this.dataTracker.set(CLIMB_FLAG, flag);
    }

    @Override
    protected float getJumpVelocity() {
        return 0.0F;
    }

    protected float getClimbSpeedMultiplier() {
        return 1.0F;
    }
}
