package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.SleepingAnimal;
import com.starfish_studios.naturalist.entity.Snake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class SleepLayer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private final ResourceLocation model;
    private final ResourceLocation sleepLayer;

    public SleepLayer(IGeoRenderer<T> entityRendererIn, ResourceLocation model, ResourceLocation sleepLayer) {
        super(entityRendererIn);
        this.model = model;
        this.sleepLayer = sleepLayer;
    }

    @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isSleeping()) {
            RenderType renderType = RenderType.entityCutoutNoCull(sleepLayer);
            matrixStackIn.pushPose();
            this.getRenderer().render(this.getEntityModel().getModel(model), entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }
}
