package com.starfish_studios.naturalist;

import com.starfish_studios.naturalist.client.renderer.*;
import com.starfish_studios.naturalist.core.platform.ClientPlatformHelper;
import com.starfish_studios.naturalist.core.registry.*;
import com.starfish_studios.naturalist.mixin.ClientLevelMixin;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.item.Item;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

public class NaturalistClient {
    public static void init() {
        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.CHRYSALIS, RenderType.cutout());
        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.AZURE_FROGLASS, RenderType.translucent());
        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.VERDANT_FROGLASS, RenderType.translucent());
        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.CRIMSON_FROGLASS, RenderType.translucent());

        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.CATTAIL, RenderType.cutout());
        ClientPlatformHelper.setRenderLayer(NaturalistBlocks.DUCKWEED, RenderType.cutout());

        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.SNAIL, SnailRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BEAR, BearRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BUTTERFLY, ButterflyRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.MOTH, MothRenderer::new);
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
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.RHINO, RhinoRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.LION, LionRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.ELEPHANT, ElephantRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.ZEBRA, ZebraRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.GIRAFFE, GiraffeRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.HIPPO, HippoRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.VULTURE, VultureRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BOAR, BoarRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.DRAGONFLY, DragonflyRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.CATFISH, CatfishRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.ALLIGATOR, AlligatorRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.BASS, BassRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.LIZARD, LizardRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.LIZARD_TAIL, LizardTailRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.TORTOISE, TortoiseRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.DUCK, DuckRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.HYENA, HyenaRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.OSTRICH, OstrichRenderer::new);
        ClientPlatformHelper.registerEntityRenderers(NaturalistEntityTypes.TERMITE, TermiteRenderer::new);


        //MenuScreens.register(NaturalistMenus.ELEPHANT_INVENTORY.get(), ElephantInventoryScreen::new);

        copyOldModelsResources();

        // Set<Item> particleMarkerBlocks = new HashSet<>(ClientLevelMixin.getMARKER_PARTICLE_ITEMS());
        // particleMarkerBlocks.add(NaturalistItems.GLOW_GOOP.get());
        // ClientLevelMixin.setMARKER_PARTICLE_ITEMS(particleMarkerBlocks);
    }



    private static void copyOldModelsResources() {
        File dir = new File(".", "resourcepacks");
        File target = new File(dir, "Naturalist Old Models.zip");

        if(!target.exists())
            try {
                dir.mkdirs();
                InputStream in = Naturalist.class.getResourceAsStream("/assets/naturalist/Naturalist Old Models.zip");
                FileOutputStream out = new FileOutputStream(target);

                byte[] buf = new byte[16384];
                int len;
                while((len = in.read(buf)) > 0)
                    out.write(buf, 0, len);

                in.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
    }
}
