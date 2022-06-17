package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Snail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class SnailModel extends AnimatedGeoModel<Snail> {
    @Override
    public ResourceLocation getModelResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/snail.animation.json");
    }
}
