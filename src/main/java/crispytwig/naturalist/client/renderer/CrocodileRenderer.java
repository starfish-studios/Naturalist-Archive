package crispytwig.naturalist.client.renderer;

import crispytwig.naturalist.client.model.CrocodileModel;
import crispytwig.naturalist.entity.Crocodile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CrocodileRenderer extends GeoEntityRenderer<Crocodile> {
    public CrocodileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrocodileModel());
        this.shadowRadius = 0.2F;
    }
}
