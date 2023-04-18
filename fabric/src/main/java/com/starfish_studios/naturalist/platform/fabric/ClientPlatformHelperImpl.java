package com.starfish_studios.naturalist.platform.fabric;

import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib3.core.IAnimatable;
import software.bernie.geckolib3.model.AnimatedGeoModel;

import java.util.function.Supplier;

public class ClientPlatformHelperImpl {
    public static void setRenderLayer(Supplier<Block> block, RenderType type) {
        BlockRenderLayerMap.INSTANCE.putBlock(block.get(), type);
        BlockRenderLayerMap.INSTANCE.putBlock(NaturalistBlocks.CATTAIL.get(), RenderType.cutout());
    }

    public static <T extends Entity> void registerEntityRenderers(Supplier<EntityType<T>> type, EntityRendererProvider<T> renderProvider) {
        EntityRendererRegistry.register(type.get(), renderProvider);
    }
}
