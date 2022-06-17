package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Butterfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class ButterflyModel extends AnimatedGeoModel<Butterfly> {
    @Override
    public Identifier getModelResource(Butterfly butterfly) {
        return new Identifier(Naturalist.MOD_ID, "geo/butterfly.geo.json");
    }

    @Override
    public Identifier getTextureResource(Butterfly butterfly) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/butterfly.png");
    }

    @Override
    public Identifier getAnimationResource(Butterfly butterfly) {
        return new Identifier(Naturalist.MOD_ID, "animations/butterfly.animation.json");
    }
}
