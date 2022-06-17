package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Deer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class DeerModel extends AnimatedGeoModel<Deer> {
    @Override
    public Identifier getModelLocation(Deer deer) {
        return new Identifier(Naturalist.MOD_ID, "geo/deer.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Deer deer) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Deer deer) {
        return new Identifier(Naturalist.MOD_ID, "animations/deer.animation.json");
    }

    @Override
    public void setLivingAnimations(Deer deer, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(deer, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (deer.isBaby()) {
            head.setScaleX(2.0F);
            head.setScaleY(2.0F);
            head.setScaleZ(2.0F);
        }

        if (!deer.isEating()) {
            head.setRotationX(extraDataOfType.get(0).headPitch * MathHelper.RADIANS_PER_DEGREE);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
        }
    }
}
