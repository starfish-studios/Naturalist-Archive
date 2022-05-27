package com.starfish_studios.naturalist.event;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.client.renderer.*;
import com.starfish_studios.naturalist.registry.NaturalistBlocks;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void init(FMLCommonSetupEvent event) {
        ItemBlockRenderTypes.setRenderLayer(NaturalistBlocks.CHRYSALIS.get(), RenderType.cutout());
    }

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NaturalistEntityTypes.SNAIL.get(), SnailRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.CROCODILE.get(), CrocodileRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.BEAR.get(), BearRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.BUTTERFLY.get(), ButterflyRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.FIREFLY.get(), FireflyRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.SNAKE.get(), SnakeRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.CORAL_SNAKE.get(), SnakeRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.RATTLESNAKE.get(), SnakeRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.DEER.get(), DeerRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.BLUEJAY.get(), BirdRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.CARDINAL.get(), BirdRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.CANARY.get(), BirdRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.ROBIN.get(), BirdRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.CATERPILLAR.get(), CaterpillarRenderer::new);
    }
}
