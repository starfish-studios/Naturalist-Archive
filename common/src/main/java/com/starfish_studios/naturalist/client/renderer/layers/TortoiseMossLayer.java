package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Tortoise;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class TortoiseMossLayer extends GeoLayerRenderer<Tortoise> {
    private static final ResourceLocation MODEL = new ResourceLocation(Naturalist.MOD_ID, "geo/tortoise.geo.json");
    private final ResourceLocation[] LAYERS = new ResourceLocation[]{
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/moss/stage_one.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/moss/stage_two.png"),
            new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/moss/stage_three.png")
    };

    public TortoiseMossLayer(IGeoRenderer<Tortoise> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack PoseStackIn, MultiBufferSource bufferIn, int packedLightIn, Tortoise entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {

    }

   /* @Override
    public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, Tortoise entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.getMossLevel() > 0) {
            RenderType renderType = RenderType.entityCutoutNoCull(LAYERS[entitylivingbaseIn.getMossLevel() - 1]);
            matrixStackIn.pushPose();
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
        }
    }

    */
}
