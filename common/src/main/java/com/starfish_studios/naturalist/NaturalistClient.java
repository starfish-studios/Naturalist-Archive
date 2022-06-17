package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.client.renderer.*;
import com.starfish_studios.naturalist.platform.ClientPlatformHelper;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.client.render.RenderLayer;

public class NaturalistClient {
    public static void init() {
        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.CHRYSALIS, RenderLayer.getCutout());

        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.SNAIL, SnailRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BEAR, BearRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BUTTERFLY, ButterflyRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.FIREFLY, FireflyRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.SNAKE, SnakeRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.CORAL_SNAKE, SnakeRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.RATTLESNAKE, SnakeRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.DEER, DeerRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BLUEJAY, BirdRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.CARDINAL, BirdRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.CANARY, BirdRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.ROBIN, BirdRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.CATERPILLAR, CaterpillarRenderer::new);
    }
}
