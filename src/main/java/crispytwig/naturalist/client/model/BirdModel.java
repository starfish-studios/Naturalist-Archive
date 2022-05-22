package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Bird;
import crispytwig.naturalist.registry.NaturalistEntityTypes;
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
public class BirdModel extends AnimatedGeoModel<Bird> {
    @Override
    public ResourceLocation getTextureLocation(Bird bird) {
        if (bird.getType().equals(NaturalistEntityTypes.BLUEJAY.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bluejay.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CANARY.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/canary.png");
        } else if (bird.getType().equals(NaturalistEntityTypes.CARDINAL.get())) {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/cardinal.png");
        } else {
            return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/robin.png");
        }
    }

    @Override
    public ResourceLocation getModelLocation(Bird bird) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/bird.geo.json");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Bird bird) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/bird.animation.json");
    }

    @Override
    public void setLivingAnimations(Bird bird, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(bird, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);
        IBone head = this.getAnimationProcessor().getBone("head");

        head.setRotationX(extraDataOfType.get(0).headPitch * Mth.DEG_TO_RAD);
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
