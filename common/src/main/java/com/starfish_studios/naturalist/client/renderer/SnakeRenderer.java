package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.SnakeModel;
import com.starfish_studios.naturalist.client.renderer.layers.SnakeSleepLayer;
import com.starfish_studios.naturalist.entity.Snake;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.util.Identifier;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@Environment(EnvType.CLIENT)
public class SnakeRenderer extends GeoEntityRenderer<Snake> {
    public SnakeRenderer(EntityRendererFactory.Context renderManager) {
        super(renderManager, new SnakeModel());
        this.shadowRadius = 0.4F;
        this.addLayer(new SnakeSleepLayer(this));
    }

    @Override
    public Identifier getTextureLocation(Snake entity) {
        return getTextureResource(entity);
    }
}
