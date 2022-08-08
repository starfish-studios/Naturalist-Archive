package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Vulture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class VultureModel extends AnimatedGeoModel<Vulture> {
    @Override
    public Identifier getModelLocation(Vulture vulture) {
        return new Identifier(Naturalist.MOD_ID, "geo/vulture.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Vulture vulture) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/vulture.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Vulture vulture) {
        return new Identifier(Naturalist.MOD_ID, "animations/vulture.animation.json");
    }
}
