package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Deer;
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
public class DeerModel extends AnimatedGeoModel<Deer> {
    @Override
    public ResourceLocation getModelResource(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/deer.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Deer deer) {
        if (deer.isBaby()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/deer.png");
        }

        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/deer.animation.json");
    }

    @Override
    public void setLivingAnimations(Deer deer, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(deer, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone antlers = this.getAnimationProcessor().getBone("antlers");

        if (deer.isBaby()) {
            head.setScaleX(1.4F);
            head.setScaleY(1.4F);
            head.setScaleZ(1.4F);
            head.setScaleX(1.6F);
            head.setScaleY(1.6F);
            head.setScaleZ(1.6F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        antlers.setHidden(deer.isBaby());

        if (!deer.isEating()) {
            head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        }
    }
}
