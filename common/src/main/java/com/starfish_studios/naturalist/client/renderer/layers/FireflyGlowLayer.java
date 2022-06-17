package com.starfish_studios.naturalist.client.renderer.layers;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Firefly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(value= EnvType.CLIENT)
public class FireflyGlowLayer extends GeoLayerRenderer<Firefly> {
    private static final Identifier TOP_LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_top.png");
    private static final Identifier BACK_LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_back.png");
    private static final Identifier BOTTOM_LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_bottom.png");
    private static final Identifier LEFT_LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_left.png");
    private static final Identifier RIGHT_LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/firefly/firefly_glow_right.png");
    private static final Identifier MODEL = new Identifier(Naturalist.MOD_ID, "geo/firefly.geo.json");

    public FireflyGlowLayer(IGeoRenderer<Firefly> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, Firefly entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        RenderLayer top = entityLivingBaseIn.isTailGlowing() ? RenderLayer.getEyes(TOP_LAYER) : RenderLayer.getEntityCutoutNoCull(TOP_LAYER);
        RenderLayer back = entityLivingBaseIn.isTailGlowing() ? RenderLayer.getEyes(BACK_LAYER) : RenderLayer.getEntityCutoutNoCull(BACK_LAYER);
        RenderLayer bottom = entityLivingBaseIn.isTailGlowing() ? RenderLayer.getEyes(BOTTOM_LAYER) : RenderLayer.getEntityCutoutNoCull(BOTTOM_LAYER);
        RenderLayer left = entityLivingBaseIn.isTailGlowing() ? RenderLayer.getEyes(LEFT_LAYER) : RenderLayer.getEntityCutoutNoCull(LEFT_LAYER);
        RenderLayer right = entityLivingBaseIn.isTailGlowing() ? RenderLayer.getEyes(RIGHT_LAYER) : RenderLayer.getEntityCutoutNoCull(RIGHT_LAYER);

        matrixStackIn.push();
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, top, matrixStackIn, bufferIn,
                bufferIn.getBuffer(top), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, back, matrixStackIn, bufferIn,
                bufferIn.getBuffer(back), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, bottom, matrixStackIn, bufferIn,
                bufferIn.getBuffer(bottom), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, left, matrixStackIn, bufferIn,
                bufferIn.getBuffer(left), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, right, matrixStackIn, bufferIn,
                bufferIn.getBuffer(right), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
        matrixStackIn.pop();
    }
}
