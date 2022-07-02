package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Elephant;
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
public class ElephantModel extends AnimatedGeoModel<Elephant> {
    @Override
    public ResourceLocation getModelResource(Elephant elephant) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/elephant.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Elephant elephant) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/elephant.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Elephant elephant) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/elephant.animation.json");
    }

    @Override
    public void setLivingAnimations(Elephant elephant, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(elephant, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (elephant.isBaby()) {
            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        }

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
