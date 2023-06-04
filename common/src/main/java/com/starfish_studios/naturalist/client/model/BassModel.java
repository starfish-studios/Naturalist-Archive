package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bass;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class BassModel extends AnimatedGeoModel<Bass> {
    @Override
    public ResourceLocation getModelResource(Bass bass) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/bass.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bass bass) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bass.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bass bass) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bass.animation.json");
    }
}
