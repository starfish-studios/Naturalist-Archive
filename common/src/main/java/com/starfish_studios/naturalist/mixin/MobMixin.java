package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.entity.Firefly;
import net.minecraft.entity.Entity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MobEntity.class)
public class MobMixin {
    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    private void onDoHurtTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof Frog frog && entity instanceof Firefly) {
            frog.addEffect(new StatusEffectInstance(StatusEffects.GLOWING, 60));
        }
    }
}
