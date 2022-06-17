package com.starfish_studios.naturalist.util.forge;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionUtil;
import net.minecraftforge.common.brewing.IBrewingRecipe;

public record NaturalistBrewingRecipe(Potion input, Item ingredient, Potion output) implements IBrewingRecipe {

    @Override
    public boolean isInput(ItemStack input) {
        return PotionUtil.getPotion(input).equals(this.input);
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
        stack.setNbt(new NbtCompound());
        PotionUtil.setPotion(stack, this.output);
        return stack;
    }
}
