package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Crocodile;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.model.AnimatedGeoModel;

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
}
