package com.starfish_studios.naturalist.common.item;

import com.starfish_studios.naturalist.*;
import com.starfish_studios.naturalist.common.block.crate.*;
import com.starfish_studios.naturalist.core.registry.*;
import net.minecraft.core.*;
import net.minecraft.nbt.*;
import net.minecraft.sounds.*;
import net.minecraft.world.*;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.animal.allay.*;
import net.minecraft.world.entity.player.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.context.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.phys.*;

public class AnimalCrateBlockItem extends BlockItem {
    public AnimalCrateBlockItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public InteractionResult interactLivingEntity(ItemStack stack, Player player, LivingEntity interactionTarget, InteractionHand usedHand) {
        if (player.getAbilities().instabuild) {
            stack = player.getItemInHand(usedHand);
        }
        final boolean b = !isHoldingLivingEntity(stack);
        final boolean b1 = canCaptureLivingEntity(interactionTarget);
        if (b && b1) {
            if (!player.level.isClientSide) {
                CompoundTag compoundTag = new CompoundTag();

                if (interactionTarget.isPassenger()) {
                    interactionTarget.getVehicle().ejectPassengers();
                }
                if (interactionTarget instanceof Mob mob && !(mob instanceof Allay)) {
                    mob.setPersistenceRequired();
                }
                if (interactionTarget instanceof Bucketable bucketable) {
                    bucketable.setFromBucket(true);
                }

                interactionTarget.save(compoundTag);
                compoundTag.remove("Passengers");
                compoundTag.remove("Leash");
                compoundTag.remove("UUID");
                stack.getOrCreateTag().put(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA, compoundTag);
                interactionTarget.remove(Entity.RemovalReason.DISCARDED);
                playCaptureSound(player.level, interactionTarget.blockPosition());
            }
            return InteractionResult.SUCCESS;
        }
        return super.interactLivingEntity(stack, player, interactionTarget, usedHand);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        final InteractionResult result = super.useOn(context);
        ItemStack stack = context.getItemInHand();
        Player player = context.getPlayer();
        if (!player.isShiftKeyDown() && isHoldingLivingEntity(stack)) {
            Level level = player.level;
            Vec3 position = new Vec3(Math.floor(context.getClickLocation().x)+0.5f, Math.floor(context.getClickLocation().y)+0.5f, Math.floor(context.getClickLocation().z)+0.5f);
            Entity entity = createEntityFromNBT(level, stack.getTag().getCompound(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA));
            if (entity != null) {
                entity.absMoveTo(position.x(), position.y(), position.z(), level.random.nextFloat() * 360f, 0);
                entity.setDeltaMovement(new Vec3(0, 0.25f, 0));
                level.addFreshEntity(entity);
                stack.getTag().remove(AnimalCrateBlockEntity.ANIMAL_CRATE_DATA);
                playReleaseSound(level, entity.blockPosition());
                return InteractionResult.SUCCESS;
            }
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

    public static Entity createEntityFromNBT(Level level, CompoundTag nbt) {
        return EntityType.loadEntityRecursive(nbt, level, e -> e);
    }

    public static void playCaptureSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 1, 1.2f);
        level.playSound(null, pos, SoundEvents.CHAIN_FALL, SoundSource.PLAYERS, 1, 0.7f);
    }

    public static void playReleaseSound(Level level, BlockPos pos) {
        level.playSound(null, pos, SoundEvents.CHICKEN_EGG, SoundSource.PLAYERS, 1, 0.7f);
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
