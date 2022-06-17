package com.starfish_studios.naturalist.platform.forge;

import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.RenderLayers;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.EntityRenderers;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class ClientPlatformHelperImpl {
    public static void setRenderLayer(Supplier<Block> block, RenderLayer type) {
        RenderLayers.setRenderLayer(block.get(), type);
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererFactory<T> renderProvider) {
        EntityRenderers.register(type.get(), renderProvider);
    }
}
