package com.starfish_studios.naturalist.util.forge;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public record NaturalistBrewingRecipe(Potion input, Item ingredient, Potion output) implements IBrewingRecipe {

    @Override
    public boolean isInput(ItemStack input) {
        return PotionUtils.getPotion(input).equals(this.input);
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return ingredient.getItem().equals(this.ingredient);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        if (!this.isInput(input) || !this.isIngredient(ingredient)) {
            return ItemStack.EMPTY;
        }

        ItemStack stack = new ItemStack(input.getItem());
        stack.setTag(new CompoundTag());
        PotionUtils.setPotion(stack, this.output);
        return stack;
    }
}
