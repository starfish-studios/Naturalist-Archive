package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HostileEntity.class)
public class MonsterMixin {
    @Inject(method = "isAngryAt", at = @At(value = "HEAD"), cancellable = true)
    private void onIsPreventingPlayerRest(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
        if (player.isHolding(NaturalistBlocks.TEDDY_BEAR.get().asItem())) {
            cir.setReturnValue(false);
        }
    }
}
