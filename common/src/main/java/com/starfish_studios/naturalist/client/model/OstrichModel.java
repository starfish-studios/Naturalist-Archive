package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Ostrich;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class OstrichModel extends AnimatedGeoModel<Ostrich> {
    @Override
    public ResourceLocation getModelResource(Ostrich ostrich) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/ostrich.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Ostrich ostrich) {
        if (ostrich.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/ostrich.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/ostrich.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Ostrich ostrich) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/ostrich.animation.json");
    }

    @Override
    public void setLivingAnimations(Ostrich ostrich, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(ostrich, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone leftWing = this.getAnimationProcessor().getBone("left_wing");
        IBone rightWing = this.getAnimationProcessor().getBone("right_wing");
        IBone saddle = this.getAnimationProcessor().getBone("saddle");

        saddle.setHidden(!ostrich.isSaddled());


        if (ostrich.isBaby()) {
            head.setScaleX(1.5F); head.setScaleY(1.5F); head.setScaleZ(1.5F);
            leftWing.setScaleX(1.2F); leftWing.setScaleY(1.2F); leftWing.setScaleZ(1.2F);
            rightWing.setScaleX(1.2F); rightWing.setScaleY(1.2F); rightWing.setScaleZ(1.2F);
        } else {
            head.setScaleY(1.0F); head.setScaleZ(1.0F); head.setScaleX(1.0F);
            leftWing.setScaleX(1.0F); leftWing.setScaleY(1.0F); leftWing.setScaleZ(1.0F);
            rightWing.setScaleX(1.0F); rightWing.setScaleY(1.0F); rightWing.setScaleZ(1.0F);
        }

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
