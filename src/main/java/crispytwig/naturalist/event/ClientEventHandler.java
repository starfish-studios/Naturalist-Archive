package crispytwig.naturalist.event;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.client.renderer.*;
import crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientEventHandler {

    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(NaturalistEntityTypes.SNAIL.get(), SnailRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.CROCODILE.get(), CrocodileRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.BEAR.get(), BearRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.BUTTERFLY.get(), ButterflyRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.FIREFLY.get(), FireflyRenderer::new);
        event.registerEntityRenderer(NaturalistEntityTypes.SNAKE.get(), SnakeRenderer::new);
    }
}
