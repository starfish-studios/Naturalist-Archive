package crispytwig.naturalist.client.renderer;

import crispytwig.naturalist.client.model.FireflyModel;
import crispytwig.naturalist.entity.Firefly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.GlowSquid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class FireflyRenderer extends GeoEntityRenderer<Firefly> {
    public FireflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FireflyModel());
        this.shadowRadius = 0.4F;
    }

    protected int getBlockLightLevel(Firefly firefly, BlockPos pPos) {
        return firefly.isGlowing() ? 15 : super.getBlockLightLevel(firefly, pPos);
    }
}
