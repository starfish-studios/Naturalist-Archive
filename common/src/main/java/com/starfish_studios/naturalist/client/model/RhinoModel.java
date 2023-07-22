package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Rhino;
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
public class RhinoModel extends AnimatedGeoModel<Rhino> {
    @Override
    public ResourceLocation getModelResource(Rhino rhino) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/rhino.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Rhino rhino) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/rhino.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Rhino rhino) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/rhino.animation.json");
    }

    @Override
    public void setLivingAnimations(Rhino rhino, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(rhino, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone bigHorn = this.getAnimationProcessor().getBone("big_horn");
        IBone smallHorn = this.getAnimationProcessor().getBone("small_horn");
        IBone babyHorn = this.getAnimationProcessor().getBone("baby_horn");

        if (rhino.isBaby()) {
            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        if (!rhino.isSprinting()) {
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        }

        bigHorn.setHidden(rhino.isBaby());
        smallHorn.setHidden(rhino.isBaby());
        babyHorn.setHidden(!rhino.isBaby());
    }
}
