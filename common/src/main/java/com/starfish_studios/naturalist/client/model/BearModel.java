package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Bear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class BearModel extends AnimatedGeoModel<Bear> {
    @Override
    public Identifier getModelLocation(Bear crocodile) {
        return new Identifier(Naturalist.MOD_ID, "geo/bear.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Bear bear) {
        if (bear.hasAngerTime()) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/bear/bear_angry.png");
        } else if (bear.isSleeping()) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/bear/bear_sleep.png");
        } else if (bear.isEating()) {
            if (bear.getMainHandStack().isOf(Items.SWEET_BERRIES)) {
                return new Identifier(Naturalist.MOD_ID, "textures/entity/bear/bear_berries.png");
            } else if (bear.getMainHandStack().isOf(Items.HONEYCOMB)) {
                return new Identifier(Naturalist.MOD_ID, "textures/entity/bear/bear_honey.png");
            }
        }
        return new Identifier(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Bear bear) {
        return new Identifier(Naturalist.MOD_ID, "animations/bear.animation.json");
    }

    @Override
    public void setLivingAnimations(Bear bear, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(bear, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (bear.isBaby()) {
            head.setScaleX(2.0F);
            head.setScaleY(2.0F);
            head.setScaleZ(2.0F);
        }

        if (!bear.isSleeping() && !bear.isEating() && !bear.isSitting()) {
            head.setRotationX(extraDataOfType.get(0).headPitch * MathHelper.RADIANS_PER_DEGREE);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
