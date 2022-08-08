package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.entity.Zebra;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.HorseBaseEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import net.minecraft.util.Identifier;

@Environment(EnvType.CLIENT)
public class ZebraRenderer extends HorseBaseEntityRenderer<Zebra, ZebraModel> {
    public static final EntityModelLayer LAYER_LOCATION = new EntityModelLayer(new Identifier(Naturalist.MOD_ID, "zebra"), "main");

    public ZebraRenderer(EntityRendererFactory.Context context) {
        super(context, new ZebraModel(context.getPart(LAYER_LOCATION)), 1.1F);
    }

    @Override
    public Identifier getTexture(Zebra entity) {
        return new Identifier(Naturalist.MOD_ID, "textures/entity/zebra.png");
    }
}
