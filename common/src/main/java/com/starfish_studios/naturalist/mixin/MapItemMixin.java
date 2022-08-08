package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.entity.Giraffe;
import net.minecraft.entity.Entity;
import net.minecraft.item.FilledMapItem;
import net.minecraft.item.map.MapState;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(FilledMapItem.class)
public class MapItemMixin {
    private boolean isRidingGiraffe = false;

    @Inject(method = "updateColors", at = @At("HEAD"))
    public void onUpdate(World world, Entity entity, MapState state, CallbackInfo ci) {
        this.isRidingGiraffe = entity.getVehicle() instanceof Giraffe;
    }

    @ModifyVariable(method = "updateColors", at = @At("STORE"), ordinal = 5)
    private int modifyRange(int i) {
        if (this.isRidingGiraffe) {
            return (int) (1.5F * i);
        }
        return i;
    }
}
