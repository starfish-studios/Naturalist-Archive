package com.starfish_studios.naturalist.mixin.fabric;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(PotionBrewing.class)
public interface PotionBrewingInvoker {
    @Invoker("addMix")
    static void invokeAddMix(Potion potionEntry, Item potionIngredient, Potion potionResult) {
        throw new AssertionError();
    }
}
