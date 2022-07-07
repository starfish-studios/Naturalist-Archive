package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Zebra;
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
public class ZebraModel extends AnimatedGeoModel<Zebra> {
    @Override
    public ResourceLocation getModelResource(Zebra zebra) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/zebra.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Zebra zebra) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/zebra.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Zebra zebra) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/zebra.animation.json");
    }

    @Override
    public void setLivingAnimations(Zebra zebra, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(zebra, uniqueID, customPredicate);

        if (customPredicate == null) return;

        IBone head = this.getAnimationProcessor().getBone("head");

        if (zebra.isBaby()) {
            head.setScaleX(1.1F);
            head.setScaleY(1.1F);
            head.setScaleZ(1.1F);
        }
    }
}
