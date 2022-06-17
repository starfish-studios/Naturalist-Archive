package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.ButterflyModel;
import com.starfish_studios.naturalist.entity.Butterfly;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(value= EnvType.CLIENT)
public class ButterflyRenderer extends GeoEntityRenderer<Butterfly> {
    public ButterflyRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new ButterflyModel());
        this.shadowRadius = 0.4F;
    }

    @Override
    public RenderLayer getRenderType(Butterfly animatable, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(textureLocation);
    }
}
