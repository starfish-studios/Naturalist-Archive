//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.starfish_studios.naturalist.entity.animal;

import java.util.Optional;

import com.starfish_studios.naturalist.entity.Butterfly;
import com.starfish_studios.naturalist.registry.NaturalistRegistry;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

public interface Catchable {
    boolean fromHand();

    void setFromHand(boolean fromHand);

    void saveToHandTag(ItemStack stack);

    void loadFromHandTag(CompoundTag tag);

    ItemStack getHandItemStack();

    SoundEvent getPickupSound();

    /** @deprecated */
    @Deprecated
    static void saveDefaultDataToHandTag(Mob mob, ItemStack hand) {
        CompoundTag compoundTag = hand.getOrCreateTag();
        if (mob.hasCustomName()) {
            hand.setHoverName(mob.getCustomName());
        }

        if (mob.isNoAi()) {
            compoundTag.putBoolean("NoAI", mob.isNoAi());
        }

        if (mob.isSilent()) {
            compoundTag.putBoolean("Silent", mob.isSilent());
        }

        if (mob.isNoGravity()) {
            compoundTag.putBoolean("NoGravity", mob.isNoGravity());
        }

        if (mob.hasGlowingTag()) {
            compoundTag.putBoolean("Glowing", true);
        }

        if (mob.isInvulnerable()) {
            compoundTag.putBoolean("Invulnerable", mob.isInvulnerable());
        }

        compoundTag.putFloat("Health", mob.getHealth());
    }

    /** @deprecated */
    @Deprecated
    static void loadDefaultDataFromHandTag(Mob mob, CompoundTag tag) {
        if (tag.contains("NoAI")) {
            mob.setNoAi(tag.getBoolean("NoAI"));
        }

        if (tag.contains("Silent")) {
            mob.setSilent(tag.getBoolean("Silent"));
        }

        if (tag.contains("NoGravity")) {
            mob.setNoGravity(tag.getBoolean("NoGravity"));
        }

        if (tag.contains("Glowing")) {
            mob.setGlowingTag(tag.getBoolean("Glowing"));
        }

        if (tag.contains("Invulnerable")) {
            mob.setInvulnerable(tag.getBoolean("Invulnerable"));
        }

        if (tag.contains("Health", 99)) {
            mob.setHealth(tag.getFloat("Health"));
        }

    }

    static <T extends LivingEntity & Catchable> Optional<InteractionResult> handMobPickup(Player player, InteractionHand hand, Butterfly entity) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == Items.AIR && entity.isAlive()) {
            ItemStack itemStack2 = entity.getHandItemStack();
            entity.saveToHandTag(itemStack2);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            player.setItemInHand(hand, itemStack3);
            player.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 1.0F);
            Level level = entity.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemStack2);
            }
            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }

    static <T extends LivingEntity & Catchable> Optional<InteractionResult> netMobPickup(Player player, InteractionHand hand, Butterfly entity) {
        ItemStack itemStack = player.getItemInHand(hand);
        if (itemStack.getItem() == NaturalistRegistry.BUG_NET.get() && entity.isAlive()) {
            ItemStack itemStack2 = entity.getHandItemStack();
            entity.saveToHandTag(itemStack2);
            ItemStack itemStack3 = ItemUtils.createFilledResult(itemStack, player, itemStack2, false);
            player.setItemInHand(hand, itemStack3);
            player.playSound(SoundEvents.ITEM_PICKUP, 0.3F, 1.0F);
            Level level = entity.level;
            if (!level.isClientSide) {
                CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer)player, itemStack2);
            }
            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }
}