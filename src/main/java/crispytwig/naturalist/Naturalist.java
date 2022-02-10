package crispytwig.naturalist;

import crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import software.bernie.geckolib3.GeckoLib;

@Mod(Naturalist.MOD_ID)
public class Naturalist {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String MOD_ID = "naturalist";

    public Naturalist() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        NaturalistEntityTypes.ENTITY_TYPES.register(bus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
