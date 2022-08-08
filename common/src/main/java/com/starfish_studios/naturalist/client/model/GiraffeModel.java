package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Giraffe;
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
public class GiraffeModel extends AnimatedGeoModel<Giraffe> {
    @Override
    public Identifier getModelLocation(Giraffe giraffe) {
        return new Identifier(Naturalist.MOD_ID, "geo/giraffe.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Giraffe giraffe) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/giraffe.png");
    }

    @Override
    public Identifier getAnimationFileLocation(Giraffe giraffe) {
        return new Identifier(Naturalist.MOD_ID, "animations/giraffe.animation.json");
    }

    @Override
    public void setLivingAnimations(Giraffe giraffe, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(giraffe, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        if (giraffe.isBaby()) {
            head.setScaleX(1.3F);
            head.setScaleY(1.3F);
            head.setScaleZ(1.3F);
        }

        head.setRotationX(extraDataOfType.get(0).headPitch * MathHelper.RADIANS_PER_DEGREE);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
    }
}
