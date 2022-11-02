package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Lizard;
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
public class LizardModel extends AnimatedGeoModel<Lizard> {
    public static final ResourceLocation[] TEXTURE_LOCATIONS = new ResourceLocation[]{
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/green.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/brown.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/pink.png")
    };

    @Override
    public ResourceLocation getModelResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/lizard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lizard lizard) {
        return TEXTURE_LOCATIONS[lizard.getVariant()];
    }

    @Override
    public ResourceLocation getAnimationResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/lizard.animation.json");
    }

    @Override
    public void setLivingAnimations(Lizard lizard, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(lizard, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone tail = this.getAnimationProcessor().getBone("tail");

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);

        tail.setHidden(!lizard.hasTail());
    }
}
