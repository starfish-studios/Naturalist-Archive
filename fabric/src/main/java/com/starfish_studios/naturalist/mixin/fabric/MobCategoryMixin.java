package com.starfish_studios.naturalist.mixin.fabric;

import com.starfish_studios.naturalist.registry.fabric.NaturalistMobCategoriesImpl;
import net.minecraft.world.entity.MobCategory;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;

@Mixin(MobCategory.class)
public abstract class MobCategoryMixin {
    @SuppressWarnings("InvokerTarget")
    @Invoker("<init>")
    private static MobCategory newMobCategory(String internalName, int internalId, String name, int spawnCap, boolean peaceful, boolean rare, int immediateDespawnRange) {
        throw new AssertionError();
    }

    @SuppressWarnings("ShadowTarget")
    @Shadow
    private static @Final
    @Mutable
    MobCategory[] $VALUES;

    @Inject(method = "<clinit>", at = @At(value = "FIELD", opcode = Opcodes.PUTSTATIC, target = "Lnet/minecraft/world/entity/MobCategory;$VALUES:[Lnet/minecraft/world/entity/MobCategory;", shift = At.Shift.AFTER))
    private static void addCustomSpawnGroup(CallbackInfo ci) {
        var mobCategories = new ArrayList<>(Arrays.asList($VALUES));
        var last = mobCategories.get(mobCategories.size() - 1);
        // This means our code will still work if other mods or Mojang add more spawn groups!
        var firefliesCategory = newMobCategory("FIREFLIES", last.ordinal() + 1, "fireflies", 10, true, false, 128);
        NaturalistMobCategoriesImpl.FIREFLIES = firefliesCategory;
        mobCategories.add(firefliesCategory);
        $VALUES = mobCategories.toArray(new MobCategory[0]);
    }
}
