package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Firefly;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class FireflyModel extends AnimatedGeoModel<Firefly> {
    @Override
    public ResourceLocation getModelResource(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/firefly.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/firefly.animation.json");
    }
}
