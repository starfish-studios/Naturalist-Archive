package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.SnailModel;
import com.starfish_studios.naturalist.entity.Snail;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

import javax.annotation.Nullable;

@Environment(EnvType.CLIENT)
public class SnailRenderer extends GeoEntityRenderer<Snail> {
    public SnailRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SnailModel());
        this.shadowRadius = 0.2F;
    }

    @Override
    public Identifier getTextureLocation(Snail entity) {
        return getTextureResource(entity);
    }

    @Override
    public RenderLayer getRenderType(Snail animatable, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(textureLocation);
    }
}
