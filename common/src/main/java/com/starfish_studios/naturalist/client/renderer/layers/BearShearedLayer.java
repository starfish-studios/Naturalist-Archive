package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Bear;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class BearShearedLayer extends GeoLayerRenderer<Bear> {
    private static final ResourceLocation LAYER = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/bear/bear_sheared.png");
    private static final ResourceLocation MODEL = new ResourceLocation(Naturalist.MOD_ID, "geo/bear.geo.json");

    public BearShearedLayer(IGeoRenderer<Bear> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Bear entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isSheared()) {
            RenderType renderType = RenderType.entityCutoutNoCull(LAYER);
            matrixStackIn.pushPose();
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}
