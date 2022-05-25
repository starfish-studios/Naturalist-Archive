package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Snail;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class SnailModel extends AnimatedGeoModel<Snail> {
    @Override
    public ResourceLocation getModelLocation(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snail.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/snail.animation.json");
    }
}
