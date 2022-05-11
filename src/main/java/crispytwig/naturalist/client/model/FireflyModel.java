package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Firefly;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

@OnlyIn(Dist.CLIENT)
public class FireflyModel extends AnimatedGeoModel<Firefly> {
    @Override
    public ResourceLocation getModelLocation(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "geo/firefly.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(Firefly firefly) {
        return new ResourceLocation(Naturalist.MOD_ID, "animations/firefly.animation.json");
    }
}
