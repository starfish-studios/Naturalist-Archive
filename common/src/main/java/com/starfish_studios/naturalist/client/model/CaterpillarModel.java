package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Caterpillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class CaterpillarModel extends AnimatedGeoModel<Caterpillar> {
    @Override
    public Identifier getModelResource(Caterpillar object) {
        return new Identifier(Naturalist.MOD_ID, "geo/caterpillar.geo.json");
    }

    @Override
    public Identifier getTextureResource(Caterpillar object) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public Identifier getAnimationResource(Caterpillar animatable) {
        return new Identifier(Naturalist.MOD_ID, "animations/caterpillar.animation.json");
    }
}
