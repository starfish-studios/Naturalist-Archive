package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class FireflyModel extends AnimatedGeoModel<Firefly> {
    @Override
    public Identifier getModelResource(Firefly firefly) {
        return new Identifier(Naturalist.MOD_ID, "geo/firefly.geo.json");
    }

    @Override
    public Identifier getTextureResource(Firefly firefly) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/firefly/firefly.png");
    }

    @Override
    public Identifier getAnimationResource(Firefly firefly) {
        return new Identifier(Naturalist.MOD_ID, "animations/firefly.animation.json");
    }
}
