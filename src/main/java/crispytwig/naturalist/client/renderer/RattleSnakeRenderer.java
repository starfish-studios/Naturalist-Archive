package crispytwig.naturalist.client.renderer;

import crispytwig.naturalist.client.model.CoralSnakeModel;
import crispytwig.naturalist.client.model.RattleSnakeModel;
import crispytwig.naturalist.client.renderer.layers.SnakeSleepLayer;
import crispytwig.naturalist.entity.Snake;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class RattleSnakeRenderer extends GeoEntityRenderer<Snake> {
    public RattleSnakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new RattleSnakeModel());
        this.shadowRadius = 0.4F;
        this.addLayer(new SnakeSleepLayer(this));
    }
}
