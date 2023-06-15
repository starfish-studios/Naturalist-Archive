package com.starfish_studios.naturalist.common.item;

import com.starfish_studios.naturalist.*;
import com.starfish_studios.naturalist.common.block.crate.*;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

public class AnimalCrateBlockItem extends BlockItem {
    public AnimalCrateBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (!player.level.isClientSide) {
            final boolean b = !isHoldingLivingEntity(stack);
            final boolean b1 = canCaptureLivingEntity(interactionTarget);
            if (b && b1) {
                CompoundTag compoundTag = new CompoundTag();
                interactionTarget.save(compoundTag);
                stack.getOrCreateTag().put(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA, compoundTag);
                interactionTarget.remove(Entity.RemovalReason.DISCARDED);
                return InteractionResult.SUCCESS;
            }
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final InteractionResult result = super.useOn(context);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (!player.isShiftKeyDown() && isHoldingLivingEntity(stack)) {
            Vec3 position = new Vec3(Math.floor(context.getClickLocation().x)+0.5f, Math.floor(context.getClickLocation().y)+0.5f, Math.floor(context.getClickLocation().z)+0.5f);
            Entity entity = EntityType.loadEntityRecursive(stack.getTag().getCompound(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA), player.level, e -> e);
            entity.absMoveTo(position.x(), position.y(), position.z(), context.getRotation(), 0);
            player.level.addFreshEntity(entity);
            stack.getTag().remove(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA);
            return InteractionResult.SUCCESS;
        }
        return result;
    }

    @Override
    public InteractionResult place(BlockPlaceContext context) {
        if ((context.getPlayer() != null && !context.getPlayer().isShiftKeyDown()) && isHoldingLivingEntity(context.getItemInHand())) {
            return InteractionResult.FAIL;
        }
        return super.place(context);
    }

    public boolean isHoldingLivingEntity(ItemStack stack) {
        return stack.hasTag() && stack.getTag().contains(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA);
    }

    public boolean canCaptureLivingEntity(LivingEntity livingEntity) {
        boolean hasWeakness = livingEntity.hasEffect(MobEffects.WEAKNESS);
        boolean hasTag = livingEntity.getType().is(NaturalistTags.EntityTypes.ANIMAL_CRATE_BLACKLISTED);
        return hasWeakness && !hasTag;
    }
}
