package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.entity.Firefly;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.frog.Frog;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "doHurtTarget", at = @At("HEAD"))
    private void onDoHurtTarget(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (((Object)this) instanceof Frog frog && entity instanceof Firefly) {
            frog.addEffect(new MobEffectInstance(MobEffects.GLOWING, 60));
        }
    }
}
