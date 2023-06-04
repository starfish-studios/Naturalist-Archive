package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Moth;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class MothModel extends AnimatedGeoModel<Moth> {
    @Override
    public ResourceLocation getModelResource(Moth moth) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/moth.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Moth moth) {
        if (moth.getVariant().getName().equals("domestic_silk")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/domestic_silk.png");
        } else if (moth.getVariant().getName().equals("polyphemus")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/polyphemus.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly/polyphemus.png");
    }


    @Override
    public ResourceLocation getAnimationResource(Moth moth) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/butterfly.animation.json");
    }
}
