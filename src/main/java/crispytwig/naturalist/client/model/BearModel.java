package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Bear;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

import java.util.List;

@OnlyIn(Dist.CLIENT)
public class BearModel extends AnimatedGeoModel<Bear> {
    @Override
    public ResourceLocation getModelLocation(Bear crocodile) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/bear.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Bear bear) {
        if (bear.isAngry()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_angry.png");
        } else if (bear.isSleeping()) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_sleep.png");
        }
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Bear bear) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bear.animation.json");
    }

    @Override
    public void setLivingAnimations(Bear bear, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(bear, uniqueID, customPredicate);

        if (customPredicate == null || bear.isSleeping()) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);

        IBone head = this.getAnimationProcessor().getBone("head");
        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
