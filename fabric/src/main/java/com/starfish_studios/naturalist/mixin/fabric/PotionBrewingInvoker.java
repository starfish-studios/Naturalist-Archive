package com.starfish_studios.naturalist.mixin.fabric;

import net.minecraft.item.Item;
import net.minecraft.potion.Potion;
import net.minecraft.recipe.BrewingRecipeRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(BrewingRecipeRegistry.class)
public interface PotionBrewingInvoker {
    @Invoker("addMix")
    static void invokeAddMix(Potion potionEntry, Item potionIngredient, Potion potionResult) {
        throw new AssertionError();
    }
}
