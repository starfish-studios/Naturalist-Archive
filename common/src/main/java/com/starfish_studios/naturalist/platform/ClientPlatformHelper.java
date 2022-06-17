package com.starfish_studios.naturalist.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class ClientPlatformHelper {
    @ExpectPlatform
    public static void setRenderLayer(Supplier<Block> block, RenderType type) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider) {
        throw new AssertionError();
    }
}
