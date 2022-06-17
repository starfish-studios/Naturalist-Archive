package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Caterpillar;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(value= EnvType.CLIENT)
public class CaterpillarModel extends AnimatedGeoModel<Caterpillar> {
    @Override
    public Identifier getModelLocation(Caterpillar object) {
        return new Identifier(Naturalist.MOD_ID, "geo/caterpillar.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Caterpillar object) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/caterpillar.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Caterpillar animatable) {
        return new Identifier(Naturalist.MOD_ID, "animations/caterpillar.animation.json");
    }
}
