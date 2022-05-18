package crispytwig.naturalist.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import crispytwig.naturalist.client.model.FireflyModel;
import crispytwig.naturalist.client.renderer.layers.FireflyGlowLayer;
import crispytwig.naturalist.entity.Firefly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.GlowSquid;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class FireflyRenderer extends GeoEntityRenderer<Firefly> {
    public FireflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new FireflyModel());
        this.shadowRadius = 0.4F;
        this.addLayer(new FireflyGlowLayer(this));
    }

    @Override
    public RenderType getRenderType(Firefly animatable, float partialTicks, PoseStack stack, @Nullable MultiBufferSource renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, ResourceLocation textureLocation) {
        return RenderType.entityCutoutNoCull(textureLocation);
    }
}
