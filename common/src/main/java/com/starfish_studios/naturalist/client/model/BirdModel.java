package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Bird;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
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
public class BirdModel extends AnimatedGeoModel<Bird> {
    @Override
    public Identifier getTextureLocation(Bird bird) {
        if (bird.getType().equals(NaturalistEntityTypes.BLUEJAY)) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/bluejay.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CANARY)) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/canary.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CARDINAL)) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/cardinal.png");
        } else {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/robin.png");
        }
    }

    @Override
    public Identifier getModelLocation(Bird bird) {
        return new Identifier(Naturalist.MOD_ID, "geo/bird.geo.json");
    }

    @Override
    public Identifier getAnimationFileLocation(Bird bird) {
        return new Identifier(Naturalist.MOD_ID, "animations/bird.animation.json");
    }

    @Override
    public void setLivingAnimations(Bird bird, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(bird, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        head.setRotationX(extraDataOfType.get(0).headPitch * MathHelper.RADIANS_PER_DEGREE);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
    }
}
