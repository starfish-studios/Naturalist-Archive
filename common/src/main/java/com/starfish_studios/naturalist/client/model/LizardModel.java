package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Lizard;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class LizardModel extends AnimatedGeoModel<Lizard> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/green.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/brown.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/pink.png")
    };

    @Override
    public ResourceLocation getModelResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/lizard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lizard lizard) {
        return TEXTURE_LOCATIONS[lizard.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lizard.animation.json");
    }
}
