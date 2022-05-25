package com.starfish_studios.naturalist.client.renderer;

import com.starfish_studios.naturalist.client.model.CrocodileModel;
import com.starfish_studios.naturalist.entity.Crocodile;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

@OnlyIn(Dist.CLIENT)
public class CrocodileRenderer extends GeoEntityRenderer<Crocodile> {
    public CrocodileRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CrocodileModel());
        this.shadowRadius = 0.8F;
    }
}
