package com.starfish_studios.naturalist.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import java.util.function.Supplier;
import net.minecraft.block.Block;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;

public class ClientPlatformHelper {
    @ExpectPlatform
    public static void setRenderLayer(Supplier<Block> block, RenderLayer type) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererFactory<T> renderProvider) {
        throw new AssertionError();
    }
}
