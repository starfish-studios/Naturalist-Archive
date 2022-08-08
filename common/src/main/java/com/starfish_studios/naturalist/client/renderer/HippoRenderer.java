package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.HippoModel;
import com.starfish_studios.naturalist.entity.Hippo;
import com.starfish_studios.naturalist.platform.ClientPlatformHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3f;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib3.geo.render.built.GeoBone;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class HippoRenderer extends GeoEntityRenderer<Hippo> {
    public HippoRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new HippoModel());
        this.shadowRadius = 1.1F;
    }

    @Override
    public void renderEarly(Hippo animatable, MatrixStack stackIn, float ticks, VertexConsumerProvider renderTypeBuffer, VertexConsumer vertexBuilder, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float partialTicks) {
        super.renderEarly(animatable, stackIn, ticks, renderTypeBuffer, vertexBuilder, packedLightIn, packedOverlayIn, red, green, blue, partialTicks);
        if (animatable.isBaby()) {
            stackIn.scale(0.5F, 0.5F, 0.5F);
        }
    }

    @Override
    public RenderLayer getRenderType(Hippo animatable, float partialTicks, MatrixStack stack, @Nullable VertexConsumerProvider renderTypeBuffer, @Nullable VertexConsumer vertexBuilder, int packedLightIn, Identifier textureLocation) {
        return RenderLayer.getEntityCutoutNoCull(textureLocation);
    }

    @Override
    public void renderRecursively(GeoBone bone, MatrixStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        if (bone.getName().equals("botjaw") && mainHand.getItem() instanceof BlockItem blockItem) {
            stack.push();
            stack.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(0));
            stack.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(0));
            stack.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion(0));
            stack.translate(-0.4D, 0.76D, -1.8D);
            stack.scale(0.675F,0.675F,0.675F);
            MinecraftClient.getInstance().getBlockRenderManager().renderBlockAsEntity(blockItem.getBlock().getDefaultState(), stack, this.rtb, packedLightIn, packedOverlayIn);
            stack.pop();
            bufferIn = rtb.getBuffer(RenderLayer.getEntityTranslucent(whTexture));
        }
        super.renderRecursively(bone, stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
