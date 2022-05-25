package com.starfish_studios.naturalist.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.ShoulderRidingEntity;
import net.minecraft.world.level.Level;

public class Bluejay extends Bird {
    public Bluejay(EntityType<? extends ShoulderRidingEntity> entityType, Level level) {
        super(entityType, level);
    }
}
