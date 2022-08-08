package com.starfish_studios.naturalist.client.renderer.layers;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.entity.SleepingAnimal;
import com.starfish_studios.naturalist.entity.Snake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

@Environment(EnvType.CLIENT)
public class SleepLayer<T extends LivingEntity & IAnimatable> extends GeoLayerRenderer<T> {
    private static final Identifier LAYER = new Identifier(Naturalist.MOD_ID, "textures/entity/lion_sleep.png");
    private static final Identifier MODEL = new Identifier(Naturalist.MOD_ID, "geo/lion.geo.json");
    private final Identifier model;
    private final Identifier sleepLayer;

    public SleepLayer(IGeoRenderer<T> entityRendererIn, Identifier model, Identifier sleepLayer) {
        super(entityRendererIn);
        this.model = model;
        this.sleepLayer = sleepLayer;
    }

    @Override
    public void render(MatrixStack matrixStackIn, VertexConsumerProvider bufferIn, int packedLightIn, T entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        if (entitylivingbaseIn.isSleeping()) {
            RenderLayer renderType = RenderLayer.getEntityCutoutNoCull(sleepLayer);
            matrixStackIn.push();
            this.getRenderer().render(this.getEntityModel().getModel(model), entitylivingbaseIn, partialTicks, renderType, matrixStackIn, bufferIn,
                    bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.DEFAULT_UV, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.pop();
        }
    }
}
