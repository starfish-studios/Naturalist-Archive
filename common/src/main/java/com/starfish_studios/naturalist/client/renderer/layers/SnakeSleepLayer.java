package com.starfish_studios.naturalist.client.renderer.layers;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.Snake;
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
public class SnakeSleepLayer extends GeoLayerRenderer<Snake> {
    private static final Identifier LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/snake/snake_sleep.png");
    private static final Identifier MODEL = new Identifier(Naturalist.MOD_ID, "geo/snake.geo.json");

    public SnakeSleepLayer(IGeoRenderer<Snake> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, Snake entityLivingBaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entityLivingBaseIn.isSleeping()) {
            RenderLayer renderType = RenderLayer.getEntityCutoutNoCull(LAYER);
            matrixStackIn.push();
            this.getRenderer().render(this.getEntityModel().getModel(MODEL), entityLivingBaseIn, partialTicks, renderType, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
    }
}
