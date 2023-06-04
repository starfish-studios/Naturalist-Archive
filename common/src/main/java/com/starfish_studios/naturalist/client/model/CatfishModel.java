package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Catfish;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class CatfishModel extends AnimatedGeoModel<Catfish> {
    @Override
    public ResourceLocation getModelResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/catfish.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/catfish.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Catfish catfish) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/catfish.animation.json");
    }
}
