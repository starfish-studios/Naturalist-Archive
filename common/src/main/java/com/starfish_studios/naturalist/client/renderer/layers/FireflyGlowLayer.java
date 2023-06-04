package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class FireflyGlowLayer extends GeoLayerRenderer<Firefly> {
    private static final ResourceLocation TOP_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_top.png");
    private static final ResourceLocation BACK_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_back.png");
    private static final ResourceLocation BOTTOM_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_bottom.png");
    private static final ResourceLocation LEFT_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_left.png");
    private static final ResourceLocation RIGHT_LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_right.png");
    private static final ResourceLocation MODEL = new ResourceLocation(Naturalist.MOD_ID, "geo/firefly.geo.json");

    public FireflyGlowLayer(IGeoRenderer<Firefly> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Firefly entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderType top = entityLivingBaseIn.isGlowing() ? RenderType.eyes(TOP_LAYER) : RenderType.entityCutoutNoCull(TOP_LAYER);
        RenderType back = entityLivingBaseIn.isGlowing() ? RenderType.eyes(BACK_LAYER) : RenderType.entityCutoutNoCull(BACK_LAYER);
        RenderType bottom = entityLivingBaseIn.isGlowing() ? RenderType.eyes(BOTTOM_LAYER) : RenderType.entityCutoutNoCull(BOTTOM_LAYER);
        RenderType left = entityLivingBaseIn.isGlowing() ? RenderType.eyes(LEFT_LAYER) : RenderType.entityCutoutNoCull(LEFT_LAYER);
        RenderType right = entityLivingBaseIn.isGlowing() ? RenderType.eyes(RIGHT_LAYER) : RenderType.entityCutoutNoCull(RIGHT_LAYER);

        matrixStackIn.pushPose();
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, top, matrixStackIn, bufferIn,
                bufferIn.getBuffer(top), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, back, matrixStackIn, bufferIn,
                bufferIn.getBuffer(back), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, bottom, matrixStackIn, bufferIn,
                bufferIn.getBuffer(bottom), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, left, matrixStackIn, bufferIn,
                bufferIn.getBuffer(left), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, right, matrixStackIn, bufferIn,
                bufferIn.getBuffer(right), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.popPose();
    }
}
