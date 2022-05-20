package crispytwig.naturalist.client.model;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.AbstractBird;
import net.minecraft.resources.ResourceLocation;

public class BluejayModel extends AbstractBirdModel{
    @Override
    public ResourceLocation getTextureLocation(AbstractBird object) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bluejay.png");
    }
}
