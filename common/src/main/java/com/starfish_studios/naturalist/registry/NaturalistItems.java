package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class NaturalistItems {
    public static final Supplier<SpawnEggItem> SNAIL_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("snail_spawn_egg", NaturalistEntityTypes.SNAIL, 5457209, 8811878);
    public static final Supplier<SpawnEggItem> BEAR_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("bear_spawn_egg", NaturalistEntityTypes.BEAR, 3745047, 7228984);
    public static final Supplier<SpawnEggItem> BUTTERFLY_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("butterfly_spawn_egg", NaturalistEntityTypes.BUTTERFLY, 16742912, 4727321);
    public static final Supplier<SpawnEggItem> FIREFLY_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("firefly_spawn_egg", NaturalistEntityTypes.FIREFLY, 10567424, 16764416);
    public static final Supplier<SpawnEggItem> SNAKE_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("snake_spawn_egg", NaturalistEntityTypes.SNAKE, 9076003, 12954734);
    public static final Supplier<SpawnEggItem> CORAL_SNAKE_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("coral_snake_spawn_egg", NaturalistEntityTypes.CORAL_SNAKE, 3485226, 12261376);
    public static final Supplier<SpawnEggItem> RATTLESNAKE_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("rattlesnake_spawn_egg", NaturalistEntityTypes.RATTLESNAKE, 16039772, 7293214);
    public static final Supplier<SpawnEggItem> DEER_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("deer_spawn_egg", NaturalistEntityTypes.DEER, 10318165, 14531208);
    public static final Supplier<SpawnEggItem> BLUEJAY_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("bluejay_spawn_egg", NaturalistEntityTypes.BLUEJAY, 3960484, 38835);
    public static final Supplier<SpawnEggItem> CARDINAL_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("cardinal_spawn_egg", NaturalistEntityTypes.CARDINAL, 9765900, 16739600);
    public static final Supplier<SpawnEggItem> CANARY_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("canary_spawn_egg", NaturalistEntityTypes.CANARY, 14979584, 16769792);
    public static final Supplier<SpawnEggItem> ROBIN_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("robin_spawn_egg", NaturalistEntityTypes.ROBIN, 5327440, 16746770);
    public static final Supplier<SpawnEggItem> CATERPILLAR_SPAWN_EGG = NaturalistRegistryHelper.registerSpawnEggItem("caterpillar_spawn_egg", NaturalistEntityTypes.CATERPILLAR, 3815473, 15647488);
    public static final Supplier<Item> VENISON = NaturalistRegistryHelper.registerItem("venison", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.MUTTON)));
    public static final Supplier<Item> COOKED_VENISON = NaturalistRegistryHelper.registerItem("cooked_venison", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.COOKED_MUTTON)));
    public static final Supplier<Item> ANTLER = NaturalistRegistryHelper.registerItem("antler", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> GLOW_GOOP = NaturalistRegistryHelper.registerItem("glow_goop", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> SNAIL_SHELL = NaturalistRegistryHelper.registerItem("snail_shell", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> CHRYSALIS = NaturalistRegistryHelper.registerItem("chrysalis", () -> new BlockItem(NaturalistBlocks.CHRYSALIS.get(), new Item.Properties().tab(Naturalist.TAB).stacksTo(1)));
    public static final Supplier<Item> SNAIL_BUCKET = NaturalistRegistryHelper.registerMobBucketItem("snail_bucket", NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, () -> SoundEvents.BUCKET_EMPTY);

    public static void init() {}
}
