package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Snail;
import com.starfish_studios.naturalist.common.entity.Snail;
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
public class SnailModel extends AnimatedGeoModel<Snail> {
    @Override
    public ResourceLocation getModelResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/snail.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snail.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Snail snail) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/snail.animation.json");
    }

    @Override
    public void setLivingAnimations(Snail snail, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(snail, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone leftEye = this.getAnimationProcessor().getBone("left_eye");
        IBone rightEye = this.getAnimationProcessor().getBone("right_eye");

        /*
        if (snail.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
        }
        */
        if (!snail.isClimbing() || !snail.canHide()) {
            leftEye.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD); leftEye.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
            rightEye.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD); rightEye.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        }
    }
}
