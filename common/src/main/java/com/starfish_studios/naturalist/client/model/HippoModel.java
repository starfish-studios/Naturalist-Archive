package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Hippo;
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

@Environment(EnvType.CLIENT)
public class HippoModel extends AnimatedGeoModel<Hippo> {
    @Override
    public Identifier getModelLocation(Hippo hippo) {
        return new Identifier(Naturalist.MOD_ID, "geo/hippo.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Hippo hippo) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/hippo.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Hippo hippo) {
        return new Identifier(Naturalist.MOD_ID, "animations/hippo.animation.json");
    }

    @Override
    public void setLivingAnimations(Hippo hippo, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(hippo, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (hippo.isBaby()) {
            head.setScaleX(1.75F);
            head.setScaleY(1.75F);
            head.setScaleZ(1.75F);
        }

//        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
    }
}
