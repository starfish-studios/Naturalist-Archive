package com.starfish_studios.naturalist.mixin.fabric;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(SpawnRestriction.class)
public interface SpawnPlacementsInvoker {
    @Invoker("register")
    static <T extends MobEntity> void invokeRegister(EntityType<T> entityType, SpawnRestriction.Location decoratorType, Heightmap.Type heightMapType, SpawnRestriction.SpawnPredicate<T> decoratorPredicate) {
        throw new AssertionError();
    }
}
