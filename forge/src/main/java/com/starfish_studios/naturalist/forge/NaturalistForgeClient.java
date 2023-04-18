package com.starfish_studios.naturalist.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.NaturalistClient;
import com.starfish_studios.naturalist.client.model.ZebraModel;
import com.starfish_studios.naturalist.client.renderer.ZebraRenderer;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturalistForgeClient {
    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        NaturalistClient.init();
        registerEntityRenderers();
    }

    @SubscribeEvent
    public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ZebraRenderer.LAYER_LOCATION, ZebraModel::createBodyLayer);
    }

    public static void registerEntityRenderers() {
        EntityRenderers.register(NaturalistEntityTypes.DUCK_EGG.get(), ThrownItemRenderer::new);
    }
}
