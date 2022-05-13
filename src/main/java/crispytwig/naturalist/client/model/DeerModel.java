package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Deer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class DeerModel extends AnimatedGeoModel<Deer> {
    @Override
    public ResourceLocation getModelLocation(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/deer.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/deer.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Deer deer) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/deer.animation.json");
    }
}
