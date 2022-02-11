package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Crocodile;
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
public class CrocodileModel extends AnimatedGeoModel<Crocodile> {
    @Override
    public ResourceLocation getModelLocation(Crocodile crocodile) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/crocodile.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Crocodile crocodile) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/crocodile.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Crocodile crocodile) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/crocodile.animation.json");
    }

    @Override
    public void setLivingAnimations(Crocodile entity, Integer uniqueID, @Nullable AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);

        if (customPredicate == null) return;

        List<EntityModelData> extraDataOfType = customPredicate.getExtraDataOfType(EntityModelData.class);

        IBone head = this.getAnimationProcessor().getBone("head");
        head.setRotationY(extraDataOfType.get(0).netHeadYaw * Mth.DEG_TO_RAD);
    }
}
