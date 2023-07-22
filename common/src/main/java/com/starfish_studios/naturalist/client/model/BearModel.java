package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BearModel extends AnimatedGeoModel<Bear> {
    @Override
    public ResourceLocation getModelResource(Bear bear) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/bear.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Bear bear) {
        // BEHAVIOR TEXTURES

        if (bear.isAngry()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_angry.png");
        } else if (bear.isSleeping()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_sleep.png");
        } else if (bear.isEating()) {
            if (bear.getMainHandItem().is(Items.SWEET_BERRIES)) {
                return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_berries.png");
            } else if (bear.getMainHandItem().is(Items.HONEYCOMB)) {
                return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_honey.png");
            }
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
        }

        // NORMAL TEXTURE

        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Bear bear) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bear.animation.json");
    }

    @Override
    public void setLivingAnimations(Bear bear, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(bear, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (bear.isBaby()) {
            head.setScaleX(1.8F);
            head.setScaleY(1.8F);
            head.setScaleZ(1.8F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        if (!bear.isSleeping() && !bear.isEating() && !bear.isSitting()) {
            head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        }
    }
}
