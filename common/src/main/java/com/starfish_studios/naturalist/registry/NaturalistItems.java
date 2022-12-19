package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.food.Foods;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.level.material.Fluids;

import java.util.function.Supplier;

public class NaturalistItems {
    public static final Supplier<SpawnEggItem> SNAIL_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("snail_spawn_egg", NaturalistEntityTypes.SNAIL, 5457209, 8811878);
    public static final Supplier<SpawnEggItem> BEAR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("bear_spawn_egg", NaturalistEntityTypes.BEAR, 3745047, 7228984);
    public static final Supplier<SpawnEggItem> BUTTERFLY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("butterfly_spawn_egg", NaturalistEntityTypes.BUTTERFLY, 16742912, 4727321);
    public static final Supplier<SpawnEggItem> FIREFLY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("firefly_spawn_egg", NaturalistEntityTypes.FIREFLY, 10567424, 16764416);
    public static final Supplier<SpawnEggItem> SNAKE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("snake_spawn_egg", NaturalistEntityTypes.SNAKE, 9076003, 12954734);
    public static final Supplier<SpawnEggItem> CORAL_SNAKE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("coral_snake_spawn_egg", NaturalistEntityTypes.CORAL_SNAKE, 3485226, 12261376);
    public static final Supplier<SpawnEggItem> RATTLESNAKE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("rattlesnake_spawn_egg", NaturalistEntityTypes.RATTLESNAKE, 16039772, 7293214);
    public static final Supplier<SpawnEggItem> DEER_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("deer_spawn_egg", NaturalistEntityTypes.DEER, 10318165, 14531208);
    public static final Supplier<SpawnEggItem> BLUEJAY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("bluejay_spawn_egg", NaturalistEntityTypes.BLUEJAY, 3960484, 38835);
    public static final Supplier<SpawnEggItem> CARDINAL_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("cardinal_spawn_egg", NaturalistEntityTypes.CARDINAL, 9765900, 16739600);
    public static final Supplier<SpawnEggItem> CANARY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("canary_spawn_egg", NaturalistEntityTypes.CANARY, 14979584, 16769792);
    public static final Supplier<SpawnEggItem> ROBIN_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("robin_spawn_egg", NaturalistEntityTypes.ROBIN, 5327440, 16746770);
    public static final Supplier<SpawnEggItem> CATERPILLAR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("caterpillar_spawn_egg", NaturalistEntityTypes.CATERPILLAR, 3815473, 15647488);
    public static final Supplier<SpawnEggItem> RHINO_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("rhino_spawn_egg", NaturalistEntityTypes.RHINO, 7626842, 10982025);
    public static final Supplier<SpawnEggItem> LION_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("lion_spawn_egg", NaturalistEntityTypes.LION, 14990722, 6699537);
    public static final Supplier<SpawnEggItem> ELEPHANT_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("elephant_spawn_egg", NaturalistEntityTypes.ELEPHANT, 9539213, 6643034);
    public static final Supplier<SpawnEggItem> ZEBRA_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("zebra_spawn_egg", NaturalistEntityTypes.ZEBRA, 15263457, 1710104);
    public static final Supplier<SpawnEggItem> GIRAFFE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("giraffe_spawn_egg", NaturalistEntityTypes.GIRAFFE, 14329967, 7619616);
    public static final Supplier<SpawnEggItem> HIPPO_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("hippo_spawn_egg", NaturalistEntityTypes.HIPPO, 15702682, 9004386);
    public static final Supplier<SpawnEggItem> VULTURE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("vulture_spawn_egg", NaturalistEntityTypes.VULTURE, 4010022, 15325376);
    public static final Supplier<SpawnEggItem> BOAR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("boar_spawn_egg", NaturalistEntityTypes.BOAR, 6768433, 9854549);
    public static final Supplier<SpawnEggItem> DRAGONFLY_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("dragonfly_spawn_egg", NaturalistEntityTypes.DRAGONFLY, 7507200, 16771840);
    public static final Supplier<SpawnEggItem> CATFISH_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("catfish_spawn_egg", NaturalistEntityTypes.CATFISH, 8416033, 12233092);
    public static final Supplier<SpawnEggItem> ALLIGATOR_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("alligator_spawn_egg", NaturalistEntityTypes.ALLIGATOR, 7632930, 11770434);
    public static final Supplier<SpawnEggItem> BASS_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("bass_spawn_egg", NaturalistEntityTypes.BASS, 8159273, 14729339);
    public static final Supplier<SpawnEggItem> LIZARD_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("lizard_spawn_egg", NaturalistEntityTypes.LIZARD, 8423718, 11442803);
    public static final Supplier<SpawnEggItem> TORTOISE_SPAWN_EGG = CommonPlatformHelper.registerSpawnEggItem("tortoise_spawn_egg", NaturalistEntityTypes.TORTOISE, 13090702, 6901552);
    public static final Supplier<Item> VENISON = CommonPlatformHelper.registerItem("venison", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.MUTTON)));
    public static final Supplier<Item> COOKED_VENISON = CommonPlatformHelper.registerItem("cooked_venison", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.COOKED_MUTTON)));
    public static final Supplier<Item> ANTLER = CommonPlatformHelper.registerItem("antler", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> GLOW_GOOP = CommonPlatformHelper.registerItem("glow_goop", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> SNAIL_SHELL = CommonPlatformHelper.registerItem("snail_shell", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> CHRYSALIS = CommonPlatformHelper.registerItem("chrysalis", () -> new BlockItem(NaturalistBlocks.CHRYSALIS.get(), new Item.Properties().tab(Naturalist.TAB).stacksTo(1)));
    public static final Supplier<Item> SNAIL_BUCKET = CommonPlatformHelper.registerNoFluidMobBucketItem("snail_bucket", NaturalistEntityTypes.SNAIL, () -> Fluids.EMPTY, NaturalistSoundEvents.BUCKET_EMPTY_SNAIL);
    public static final Supplier<Item> BEAR_FUR = CommonPlatformHelper.registerItem("bear_fur", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> CATFISH_BUCKET = CommonPlatformHelper.registerMobBucketItem("catfish_bucket", NaturalistEntityTypes.CATFISH, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    public static final Supplier<Item> FISH_FILLET = CommonPlatformHelper.registerItem("fish_fillet", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.SALMON)));
    public static final Supplier<Item> COOKED_FISH_FILLET = CommonPlatformHelper.registerItem("cooked_fish_fillet", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.COOKED_SALMON)));
    public static final Supplier<Item> BASS_BUCKET = CommonPlatformHelper.registerMobBucketItem("bass_bucket", NaturalistEntityTypes.BASS, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH);
    public static final Supplier<Item> BASS = CommonPlatformHelper.registerItem("raw_bass", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.COD)));
    public static final Supplier<Item> COOKED_BASS = CommonPlatformHelper.registerItem("cooked_bass", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.COOKED_COD)));
    public static final Supplier<Item> LIZARD_TAIL = CommonPlatformHelper.registerItem("lizard_tail", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(new FoodProperties.Builder().nutrition(2).saturationMod(0.8F).meat().effect(new MobEffectInstance(MobEffects.POISON, 100, 0), 1.0f).build())));
    public static final Supplier<Item> COOKED_LIZARD_TAIL = CommonPlatformHelper.registerItem("cooked_lizard_tail", () -> new Item(new Item.Properties().tab(Naturalist.TAB).food(Foods.BAKED_POTATO)));
    public static final Supplier<Item> REPTILE_SKIN = CommonPlatformHelper.registerItem("reptile_skin", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static final Supplier<Item> TOOTH = CommonPlatformHelper.registerItem("tooth", () -> new Item(new Item.Properties().tab(Naturalist.TAB)));
    public static void init() {}
}
