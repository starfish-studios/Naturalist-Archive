package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.*;
import com.starfish_studios.naturalist.common.block.*;
import com.starfish_studios.naturalist.common.block.crate.*;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.*;

import java.util.function.Supplier;

public class NaturalistBlocks {
    public static final Supplier<Block> CHRYSALIS = CommonPlatformHelper.registerBlock("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission()));
    public static final Supplier<Block> DUCKWEED = CommonPlatformHelper.registerBlock("duckweed", () -> new WaterlilyBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF)));
    public static final Supplier<Block> GLOW_GOOP = CommonPlatformHelper.registerBlock("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of(Material.AIR).strength(0.5F).noOcclusion().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK)));

    public static final Supplier<Block> ANIMAL_CRATE = CommonPlatformHelper.registerBlock("animal_crate", () -> new AnimalCrateBlock(BlockBehaviour.Properties.copy(Blocks.OAK_PLANKS)));
    public static final Supplier<Block> AZURE_FROGLASS = registerBlock("azure_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> VERDANT_FROGLASS = registerBlock("verdant_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> CRIMSON_FROGLASS = registerBlock("crimson_froglass", () -> new GlassBlock(BlockBehaviour.Properties.copy(Blocks.GLASS)));
    public static final Supplier<Block> CATTAIL = registerBlock("cattail", () -> new CattailBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF).offsetType(BlockBehaviour.OffsetType.XZ)));
    public static final Supplier<Block> TORTOISE_EGG = registerBlock("tortoise_egg", () -> new TortoiseEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));
    public static final Supplier<Block> ALLIGATOR_EGG = registerBlock("alligator_egg", () -> new AlligatorEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));
    public static final Supplier<Block> TEDDY_BEAR = registerBlock("teddy_bear", () -> new TeddyBearBlock(BlockBehaviour.Properties.of(Material.WOOL, MaterialColor.COLOR_BROWN).strength(0.8f).sound(SoundType.WOOL).noOcclusion()));
    public static final Supplier<Block> OSTRICH_EGG = registerBlock("ostrich_egg", () -> new OstrichEggBlock(BlockBehaviour.Properties.copy(Blocks.TURTLE_EGG)));

    public static void init() {
    }

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
            Supplier<T> supplier = CommonPlatformHelper.registerBlock(name, block);
            CommonPlatformHelper.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Properties().tab(Naturalist.TAB)));
            return supplier;
    }
}
