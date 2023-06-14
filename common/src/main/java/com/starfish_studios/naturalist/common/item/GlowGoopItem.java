package com.starfish_studios.naturalist.common.item;

import com.starfish_studios.naturalist.common.entity.Butterfly;
import com.starfish_studios.naturalist.common.entity.Moth;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;

import java.util.List;

public class GlowGoopItem extends ItemNameBlockItem {

    public GlowGoopItem(Block block, Properties properties) {
        super(block, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Level level, List<Component> tooltip, TooltipFlag flagIn) {
        // This makes it so that there's a different tooltip when the Player is holding Shift.
        if (Screen.hasShiftDown()) {
            tooltip.add(Component.literal("Place up to 3").withStyle(ChatFormatting.GRAY));
            tooltip.add(Component.literal("in one space!").withStyle(ChatFormatting.GRAY));
        } else {
            tooltip.add(Component.literal("Hold ").withStyle(ChatFormatting.GRAY).append(Component.literal("Shift").withStyle(ChatFormatting.YELLOW, ChatFormatting.ITALIC)).append(Component.literal(" for info!").withStyle(ChatFormatting.GRAY)));
        }
    }
}
