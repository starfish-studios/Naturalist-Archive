package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bird;
import com.starfish_studios.naturalist.core.registry.NaturalistEntityTypes;
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
public class BirdModel extends AnimatedGeoModel<Bird> {
    @Override
    public ResourceLocation getTextureResource(Bird bird) {
        if (bird.getType().equals(NaturalistEntityTypes.BLUEJAY.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bluejay.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CANARY.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/canary.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CARDINAL.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/cardinal.png");
        } else {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/robin.png");
        }
    }

    @Override
    public ResourceLocation getModelResource(Bird bird) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/bird.geo.json");
    }

    @Override
    public ResourceLocation getAnimationResource(Bird bird) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bird.animation.json");
    }

    @Override
    public void setLivingAnimations(Bird bird, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(bird, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
