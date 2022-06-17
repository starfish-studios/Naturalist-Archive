package com.starfish_studios.naturalist.platform.fabric;

import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import java.util.function.Supplier;

public class ClientPlatformHelperImpl {
    public static void setRenderLayer(Supplier<Block> block, RenderLayer type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), type);
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererFactory<T> renderProvider) {
        EntityRendererRegistry.register(type.get(), renderProvider);
    }
}
