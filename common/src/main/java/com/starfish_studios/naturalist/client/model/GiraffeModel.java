package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Giraffe;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@Environment(EnvType.CLIENT)
public class GiraffeModel extends AnimatedGeoModel<Giraffe> {
    @Override
    public ResourceLocation getModelResource(Giraffe giraffe) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/giraffe.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Giraffe giraffe) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/giraffe.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Giraffe giraffe) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/giraffe.animation.json");
    }

    @Override
    public void setLivingAnimations(Giraffe giraffe, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(giraffe, uniqueID, customPredicate);

        if (customPredicate == null) return;

        IBone neck = this.getAnimationProcessor().getBone("neck");

        if (giraffe.isBaby()) {
            neck.setScaleX(1.1F);
            neck.setScaleY(1.1F);
            neck.setScaleZ(1.1F);
        }
    }
}
