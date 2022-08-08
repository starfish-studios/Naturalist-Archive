package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.entity.Lion;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.ai.goal.FleeEntityGoal;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CreeperEntity.class)
public class CreeperMixin extends HostileEntity {
    protected CreeperMixin(EntityType<? extends HostileEntity> entityType, World level) {
        super(entityType, level);
    }

    @Inject(at = @At(value = "HEAD"), method = "initGoals")
    public void onRegisterGoals(CallbackInfo ci) {
        this.goalSelector.add(3, new FleeEntityGoal<>(this, Lion.class, 6.0f, 1.0, 1.2));
    }
}
