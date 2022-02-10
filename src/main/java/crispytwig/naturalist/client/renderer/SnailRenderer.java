package crispytwig.naturalist.client.renderer;

import crispytwig.naturalist.client.model.SnailModel;
import crispytwig.naturalist.entity.Snail;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SnailRenderer extends GeoEntityRenderer<Snail> {
    public SnailRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnailModel());
        this.shadowRadius = 0.2F;
    }
}
