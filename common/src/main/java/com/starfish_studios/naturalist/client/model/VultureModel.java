package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Vulture;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class VultureModel extends AnimatedGeoModel<Vulture> {
    @Override
    public ResourceLocation getModelResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/vulture.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/vulture.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Vulture vulture) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/vulture.animation.json");
    }

    @Override
    public void setLivingAnimations(Vulture vulture, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(vulture, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }


}
