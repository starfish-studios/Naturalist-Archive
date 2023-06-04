package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Hyena;
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
public class HyenaModel extends AnimatedGeoModel<Hyena> {
    @Override
    public ResourceLocation getModelResource(Hyena hyena) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/hyena.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hyena hyena) {
        if (hyena.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/hyena.png");
        }

        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/hyena.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Hyena hyena) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/hyena.animation.json");
    }

    @Override
    public void setLivingAnimations(Hyena hyena, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(hyena, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone leftEar = this.getAnimationProcessor().getBone("leftEar");
        IBone rightEar = this.getAnimationProcessor().getBone("rightEar");

        if (hyena.isBaby()) {
            head.setScaleX(1.3F); head.setScaleY(1.3F); head.setScaleZ(1.3F);
            leftEar.setScaleX(1.2F); leftEar.setScaleY(1.2F); leftEar.setScaleZ(1.2F);
            rightEar.setScaleX(1.2F); rightEar.setScaleY(1.2F); rightEar.setScaleZ(1.2F);
        }

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
