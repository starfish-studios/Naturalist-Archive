package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Butterfly;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class ButterflyModel extends AnimatedGeoModel<Butterfly> {
    @Override
    public ResourceLocation getModelLocation(Butterfly butterfly) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/butterfly.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Butterfly butterfly) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/butterfly.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Butterfly butterfly) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/butterfly.animation.json");
    }
}
