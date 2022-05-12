package crispytwig.naturalist.registry;

import crispytwig.naturalist.Naturalist;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Naturalist.MOD_ID);

    public static final RegistryObject<Item> SNAIL_SPAWN_EGG = ITEMS.register("snail_spawn_egg", () -> new ForgeSpawnEggItem(() -> NaturalistEntityTypes.SNAIL.get(), 5457209, 8811878, new Item.Properties().tab(Naturalist.TAB)));
    public static final RegistryObject<Item> CROCODILE_SPAWN_EGG = ITEMS.register("crocodile_spawn_egg", () -> new ForgeSpawnEggItem(() -> NaturalistEntityTypes.CROCODILE.get(), 7567361, 10784324, new Item.Properties().tab(Naturalist.TAB)));
    public static final RegistryObject<Item> BEAR_SPAWN_EGG = ITEMS.register("bear_spawn_egg", () -> new ForgeSpawnEggItem(() -> NaturalistEntityTypes.BEAR.get(), 3745047, 7228984, new Item.Properties().tab(Naturalist.TAB)));
    public static final RegistryObject<Item> BUTTERFLY_SPAWN_EGG = ITEMS.register("butterfly_spawn_egg", () -> new ForgeSpawnEggItem(() -> NaturalistEntityTypes.BUTTERFLY.get(), 16742912, 4727321, new Item.Properties().tab(Naturalist.TAB)));
    public static final RegistryObject<Item> FIREFLY_SPAWN_EGG = ITEMS.register("firefly_spawn_egg", () -> new ForgeSpawnEggItem(() -> NaturalistEntityTypes.FIREFLY.get(), 10567424, 16764416, new Item.Properties().tab(Naturalist.TAB)));
    public static final RegistryObject<Item> SNAKE_SPAWN_EGG = ITEMS.register("snake_spawn_egg", () -> new ForgeSpawnEggItem(() -> NaturalistEntityTypes.SNAKE.get(), 9076003, 12954734, new Item.Properties().tab(Naturalist.TAB)));

}
