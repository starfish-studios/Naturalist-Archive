package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.entity.Snail;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropBlock;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.RandomSource;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Mixin(CropBlock.class)
public class CropBlockMixin {
    @Inject(at = @At(value = "HEAD"), method = "randomTick", cancellable = true)
    public void onRandomTick(BlockState state, ServerWorld level, BlockPos pos, RandomSource random, CallbackInfo ci) {
        List<Snail> snails = level.getNonSpectatingEntities(Snail.class, new Box(pos).expand(2.0D));
        if (!snails.isEmpty() && random.nextBoolean()) {
            ci.cancel();
        }
    }
}
