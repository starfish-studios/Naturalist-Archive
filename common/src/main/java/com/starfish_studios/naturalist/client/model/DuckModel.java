package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Duck;
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
public class DuckModel extends AnimatedGeoModel<Duck> {
    @Override
    public ResourceLocation getModelResource(Duck animal) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/duck.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Duck animal) {
        if (animal.getName().getString().equals("Queso")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/duck/queso.png");
        }
        else if (animal.getName().getString().equals("Ducky")) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/duck/rubber_ducky.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/duck/duck.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Duck animal) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/duck.animation.json");
    }

    @Override
    public void setLivingAnimations(Duck animal, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(animal, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (animal.isBaby()) {
            head.setScaleX(1.7F);
            head.setScaleY(1.7F);
            head.setScaleZ(1.7F);
        } else {
            head.setScaleX(1.0F);
            head.setScaleY(1.0F);
            head.setScaleZ(1.0F);
        }

        
        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        
    }
}
