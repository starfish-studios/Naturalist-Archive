package com.starfish_studios.naturalist.mixin;

import com.starfish_studios.naturalist.entity.Giraffe;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MapItem.class)
public class MapItemMixin {
    private boolean isRidingGiraffe = false;

    @Inject(method = "update", at = @At("HEAD"))
    public void onUpdate(Level level, Entity viewer, MapItemSavedData data, CallbackInfo ci) {
        this.isRidingGiraffe = viewer.getVehicle() instanceof Giraffe;
    }

    @ModifyVariable(method = "update", at = @At("STORE"), ordinal = 5)
    private int modifyRange(int i) {
        if (this.isRidingGiraffe) {
            return (int) (1.5F * i);
        }
        return i;
    }
}
