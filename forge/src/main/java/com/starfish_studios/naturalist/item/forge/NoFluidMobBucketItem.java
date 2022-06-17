package com.starfish_studios.naturalist.item.forge;

import java.util.function.Supplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.EntityBucketItem;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundEvent;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class NoFluidMobBucketItem extends EntityBucketItem {
    public NoFluidMobBucketItem(Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier, Settings properties) {
        super(entitySupplier, fluidSupplier, soundSupplier, properties);
    }

    @Override
    public TypedActionResult<ItemStack> use(World pLevel, PlayerEntity pPlayer, Hand pHand) {
        ItemStack itemstack = pPlayer.getStackInHand(pHand);
        BlockHitResult blockhitresult = raycast(pLevel, pPlayer, RaycastContext.FluidHandling.NONE);
        TypedActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
        if (ret != null) return ret;
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return TypedActionResult.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return TypedActionResult.pass(itemstack);
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getSide();
            BlockPos blockpos1 = pos.offset(direction);
            if (pLevel.canPlayerModifyAt(pPlayer, pos) && pPlayer.canPlaceOn(blockpos1, direction, itemstack)) {
                this.onEmptied(pPlayer, pLevel, itemstack, pos);
                this.playEmptyingSound(pPlayer, pLevel, pos);
                pPlayer.incrementStat(Stats.USED.getOrCreateStat(this));
                return TypedActionResult.success(getEmptiedStack(itemstack, pPlayer), pLevel.isClient());
            } else {
                return TypedActionResult.fail(itemstack);
            }
        }
    }
}
