package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Snake;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
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
public class SnakeModel extends AnimatedGeoModel<Snake> {
    @Override
    public ResourceLocation getModelResource(Snake snake) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/snake.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(Snake snake) {
        if (snake.getType().equals(NaturalistEntityTypes.CORAL_SNAKE.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/coral_snake.png");
        } else if (snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/rattlesnake.png");
        } else {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/snake/snake.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(Snake snake) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/snake.animation.json");
    }

    @Override
    public void setLivingAnimations(Snake snake, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(snake, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");
        IBone tail2 = this.getAnimationProcessor().getBone("tail2");
        IBone tail4 = this.getAnimationProcessor().getBone("tail4");

        if (!snake.isSleeping()) {
            head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
        }
        if (!snake.getMainHandItem().isEmpty()) {
            tail2.setScaleX(1.5F);
            tail2.setScaleY(1.5F);
        }
        tail4.setHidden(!snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE.get()));
    }
}
