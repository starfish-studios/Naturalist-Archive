package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Hippo;
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
public class HippoModel extends AnimatedGeoModel<Hippo> {
    @Override
    public ResourceLocation getModelResource(Hippo hippo) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/hippo.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Hippo hippo) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/hippo.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Hippo hippo) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/hippo.animation.json");
    }

    @Override
    public void setLivingAnimations(Hippo hippo, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(hippo, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (hippo.isBaby()) {
            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

//        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
