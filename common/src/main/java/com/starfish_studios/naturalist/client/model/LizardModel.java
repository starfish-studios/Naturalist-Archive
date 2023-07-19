package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Lizard;
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
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/beardie.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko.png")
    };

    @Override
    public ResourceLocation getModelResource(Lizard lizard) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/lizard.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Lizard lizard) {
        if (lizard.getVariant() == 0) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/green.png");
        } else if (lizard.getVariant() == 1) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/brown.png");
        } else if (lizard.getVariant() == 2) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/beardie.png");
        } else if (lizard.getVariant() == 3) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/leopard_gecko.png");
        } else {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/lizard/brown.png");
        }
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

        IBone basiliskBody = this.getAnimationProcessor().getBone("basilisk_body");
        IBone basiliskTail = this.getAnimationProcessor().getBone("basilisk_tail");

        IBone beardieHead = this.getAnimationProcessor().getBone("beardie_head");
        IBone beardieBody = this.getAnimationProcessor().getBone("beardie_body");

        IBone gecko = this.getAnimationProcessor().getBone("gecko");

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);

        tail.setHidden(!lizard.hasTail());
    }
}
