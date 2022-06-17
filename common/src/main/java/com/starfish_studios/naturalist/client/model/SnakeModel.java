package com.starfish_studios.naturalist.client.model;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Snake;
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
public class SnakeModel extends AnimatedGeoModel<Snake> {
    @Override
    public Identifier getModelLocation(Snake snake) {
        return new Identifier(Naturalist.MOD_ID, "geo/snake.geo.json");
    }

    @Override
    public Identifier getTextureLocation(Snake snake) {
        if (snake.getType().equals(NaturalistEntityTypes.CORAL_SNAKE)) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/snake/coral_snake.png");
        } else if (snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE)) {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/snake/rattlesnake.png");
        } else {
            return new Identifier(Naturalist.MOD_ID, "textures/entity/snake/snake.png");
        }
    }

    @Override
    public Identifier getAnimationFileLocation(Snake snake) {
        return new Identifier(Naturalist.MOD_ID, "animations/snake.animation.json");
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
            head.setRotationX(extraDataOfType.get(0).headPitch * MathHelper.RADIANS_PER_DEGREE);
            head.setRotationY(extraDataOfType.get(0).netHeadYaw * MathHelper.RADIANS_PER_DEGREE);
        }
        if (!snake.getMainHandStack().isEmpty()) {
            tail2.setScaleX(1.5F);
            tail2.setScaleY(1.5F);
        }
        tail4.setHidden(!snake.getType().equals(NaturalistEntityTypes.RATTLESNAKE));
    }
}
