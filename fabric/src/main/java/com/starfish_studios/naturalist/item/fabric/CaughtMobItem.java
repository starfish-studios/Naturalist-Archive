package com.starfish_studios.naturalist.item.fabric;

import com.starfish_studios.naturalist.entity.Butterfly;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.data.models.blockstates.Variant;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class CaughtMobItem extends MobBucketItem {
    private final EntityType<?> type;

    public CaughtMobItem(EntityType<?> entitySupplier, Fluid fluid, SoundEvent emptyingSound, Properties settings) {
        super(entitySupplier, fluid, emptyingSound, settings);
        this.type = entitySupplier;
    }

    @Override
    public void appendHoverText(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if (this.type == NaturalistEntityTypes.BUTTERFLY.get()) {
            CompoundTag compoundnbt = stack.getTag();
            if (compoundnbt != null && compoundnbt.contains("Variant", 3)) {
                Butterfly.Variant variant = Butterfly.Variant.getTypeById(compoundnbt.getInt("Variant"));
                tooltip.add((Component.translatable(String.format("tooltip.naturalist.%s", variant.toString().toLowerCase())).withStyle(ChatFormatting.ITALIC, ChatFormatting.GRAY)));
            }
        }
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.NONE);
        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos pos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = pos.relative(direction);
            if (pLevel.mayInteract(pPlayer, pos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                this.checkExtraContent(pPlayer, pLevel, itemstack, pos);
                this.playEmptySound(pPlayer, pLevel, pos);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    public static @NotNull ItemStack getEmptySuccessItem(ItemStack bucketStack, Player player) {
        return !player.getAbilities().instabuild ? new ItemStack(Items.AIR) : bucketStack;
    }
}