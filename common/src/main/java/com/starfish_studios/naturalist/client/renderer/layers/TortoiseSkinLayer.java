package com.starfish_studios.naturalist.client.renderer.layers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.common.entity.Tortoise;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoLayerRenderer;
import software.bernie.geckolib3.renderers.geo.IGeoRenderer;

public class TortoiseSkinLayer extends GeoLayerRenderer<Tortoise> {
    private final ResourceLocation model;
    private static final ResourceLocation DONATELLO = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/donatello.png");
    private static final ResourceLocation LEONARDO = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/leonardo.png");
    private static final ResourceLocation MICHELANGELO = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/michelangelo.png");
    private static final ResourceLocation RAPHAEL = new ResourceLocation(Naturalist.MOD_ID, "textures/entity/tortoise/raphael.png");
    public TortoiseSkinLayer(IGeoRenderer<Tortoise> entityRendererIn, ResourceLocation model) {
        super(entityRendererIn);
        this.model = model;
    }

    @Override
    public void render(PoseStack ps, MultiBufferSource bufferIn, int packedLightIn, Tortoise entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
        ps.pushPose();
        String name = ChatFormatting.stripFormatting(entitylivingbaseIn.getName().getString());
        if(name == null) {
            ps.popPose();
            return;
        }
        ResourceLocation skin = switch (name) {
            case "Donatello" -> DONATELLO;
            case "Leonardo" -> LEONARDO;
            case "Michelangelo" -> MICHELANGELO;
            case "Raphael" -> RAPHAEL;
            default -> null;
        };
        if(skin == null) {
            ps.popPose();
            return;
        }
        RenderType renderType = RenderType.entityCutoutNoCull(skin);
        this.getRenderer().render(this.getEntityModel().getModel(model), entitylivingbaseIn, partialTicks, renderType, ps, bufferIn, bufferIn.getBuffer(renderType), packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        ps.popPose();
    }
}
