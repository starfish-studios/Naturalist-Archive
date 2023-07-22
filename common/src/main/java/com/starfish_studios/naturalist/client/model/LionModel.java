package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Lion;
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
public class LionModel extends AnimatedGeoModel<Lion> {
    @Override
    public ResourceLocation getModelResource(Lion lion) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/lion.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lion lion) {
        return lion.isSleeping() ? new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion_sleep.png") : new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lion.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Lion lion) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lion.animation.json");
    }

    @Override
    public void setLivingAnimations(Lion lion, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(lion, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone mane = this.getAnimationProcessor().getBone("mane");

        if (lion.isBaby()) {
            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        mane.setHidden(!lion.hasMane() || lion.isBaby());

        if (!lion.isSleeping()) {
            head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        }
    }
}
