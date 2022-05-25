package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.ButterflyModel;
import com.starfish_studios.naturalist.entity.Butterfly;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class ButterflyRenderer extends GeoEntityRenderer<Butterfly> {
    public ButterflyRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ButterflyModel());
        this.shadowRadius = 0.4F;
    }
}
