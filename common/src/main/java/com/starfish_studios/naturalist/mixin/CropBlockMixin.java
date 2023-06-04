package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.common.entity.Snail;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "randomTick", cancellable = true)
    public void onRandomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        List<Snail> snails = level.getEntitiesOfClass(Snail.class, new AABB(pos).inflate(2.0D));
        if (!snails.isEmpty() && random.nextBoolean()) {
            ci.cancel();
        }
    }
}
