package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.entity.Zebra;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.AbstractHorseRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ZebraRenderer extends AbstractHorseRenderer<Zebra, ZebraModel> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(Naturalist.MOD_ID, "zebra"), "main");

    public ZebraRenderer(EntityRendererProvider.Context context) {
        super(context, new ZebraModel(context.bakeLayer(LAYER_LOCATION)), 1.1F);
    }

    @Override
    public ResourceLocation getTextureLocation(Zebra entity) {
        return new ResourceLocation(Naturalist.MOD_ID, "textures/entity/zebra.png");
    }
}
