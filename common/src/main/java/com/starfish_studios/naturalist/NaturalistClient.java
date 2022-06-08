package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.client.renderer.*;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.NaturalistRegistryHelper;
import net.minecraft.client.renderer.RenderType;

public class NaturalistClient {
    public static void init() {
        NaturalistRegistryHelper.setRenderLayer(NaturalistBlocks.CHRYSALIS, RenderType.cutout());

        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.SNAIL, SnailRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.BEAR, BearRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.BUTTERFLY, ButterflyRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.FIREFLY, FireflyRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.SNAKE, SnakeRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.CORAL_SNAKE, SnakeRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.RATTLESNAKE, SnakeRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.DEER, DeerRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.BLUEJAY, BirdRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.CARDINAL, BirdRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.CANARY, BirdRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.ROBIN, BirdRenderer::new);
        NaturalistRegistryHelper.registerEntityRenderers(NaturalistEntityTypes.CATERPILLAR, CaterpillarRenderer::new);
    }
}
