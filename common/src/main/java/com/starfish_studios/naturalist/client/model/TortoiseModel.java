package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Tortoise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.ChatFormatting;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(EnvType.CLIENT)
public class TortoiseModel extends AnimatedGeoModel<Tortoise> {

    @Override
    public ResourceLocation getModelResource(Tortoise tortoise) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/tortoise.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Tortoise tortoise) {
        if (tortoise.hasCustomName()) {
            String name = ChatFormatting.stripFormatting(tortoise.getName().getString());
            if (name != null) {
                switch (name) {
                    case "Donatello":
                        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/donatello.png");
                    case "Leonardo":
                        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/leonardo.png");
                    case "Michaelangelo":
                        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/michaelangelo.png");
                    case "Raphael":
                        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/raphael.png");
                }
            }
        } else if (tortoise.getVariant() == 0) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/brown.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/green.png");
    }

    @Override
    public ResourceLocation getAnimationResource(Tortoise tortoise) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/tortoise.animation.json");
    }

    @Override
    public void setLivingAnimations(Tortoise tortoise, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(tortoise, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
