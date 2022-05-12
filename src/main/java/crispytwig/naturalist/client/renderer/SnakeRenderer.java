package crispytwig.naturalist.client.renderer;

import crispytwig.naturalist.client.model.SnakeModel;
import crispytwig.naturalist.entity.Snake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class SnakeRenderer extends GeoEntityRenderer<Snake> {
    public SnakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnakeModel());
        this.shadowRadius = 0.4F;
    }
}
