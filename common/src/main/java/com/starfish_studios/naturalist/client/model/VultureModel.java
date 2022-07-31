package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Vulture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class VultureModel extends AnimatedGeoModel<Vulture> {
    @Override
    public ResourceLocation getModelResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/vulture.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/vulture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/vulture.animation.json");
    }
}
