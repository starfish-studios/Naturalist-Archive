package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Caterpillar;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class CaterpillarModel extends AnimatedGeoModel<Caterpillar> {
    @Override
    public ResourceLocation getModelLocation(Caterpillar object) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/caterpillar.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Caterpillar object) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Caterpillar animatable) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/caterpillar.animation.json");
    }
}
