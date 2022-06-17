package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.SnakeModel;
import com.starfish_studios.naturalist.client.renderer.layers.SnakeSleepLayer;
import com.starfish_studios.naturalist.entity.Snake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class SnakeRenderer extends GeoEntityRenderer<Snake> {
    public SnakeRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SnakeModel());
        this.shadowRadius = 0.4F;
        this.addLayer(new SnakeSleepLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Snake entity) {
        return getTextureResource(entity);
    }
}
