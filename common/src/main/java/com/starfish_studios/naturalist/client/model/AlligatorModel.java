package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Alligator;
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
public class AlligatorModel extends AnimatedGeoModel<Alligator> {
    @Override
    public ResourceLocation getModelResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/alligator.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/alligator.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Alligator alligator) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/alligator.animation.json");
    }

    @Override
    public void setLivingAnimations(Alligator alligator, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(alligator, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (alligator.isBaby()) {
            head.setScaleX(1.5F);
            head.setScaleY(1.5F);
            head.setScaleZ(1.5F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
