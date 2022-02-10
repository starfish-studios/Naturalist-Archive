package crispytwig.naturalist;

import crispytwig.naturalist.registry.NaturalistEntityTypes;
import crispytwig.naturalist.registry.NaturalistItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
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
    public static final CreativeModeTab TAB = new CreativeModeTab(MOD_ID) {
        @Override
        public ItemStack makeIcon() {
            return new ItemStack(Items.FERN);
        }
    };

    public Naturalist() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();

        NaturalistEntityTypes.ENTITY_TYPES.register(bus);
        NaturalistItems.ITEMS.register(bus);

        GeckoLib.initialize();

        MinecraftForge.EVENT_BUS.register(this);
    }
}
