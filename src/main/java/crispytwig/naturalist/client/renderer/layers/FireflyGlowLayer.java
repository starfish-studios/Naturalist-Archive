package crispytwig.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Firefly;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@OnlyIn(Dist.CLIENT)
public class FireflyGlowLayer extends GeoLayerRenderer<Firefly> {
    private static final ResourceLocation LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow.png");
    private static final ResourceLocation MODEL = new ResourceLocation(Naturalist.MOD_ID, "geo/firefly.geo.json");

    public FireflyGlowLayer(IGeoRenderer<Firefly> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Firefly entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType renderType = entityLivingBaseIn.isGlowing() ? RenderType.eyes(LAYER) : RenderType.entityCutoutNoCull(LAYER);
        matrixStackIn.pushPose();
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, renderType, matrixStackIn, bufferIn,
                bufferIn.getBuffer(renderType), 15728640, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }
}
