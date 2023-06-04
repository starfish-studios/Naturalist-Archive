package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.block.ChrysalisBlock;
import com.starfish_studios.naturalist.common.block.GlowGoopBlock;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LightBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class NaturalistBlocks {
    public static final Supplier<Block> CHRYSALIS = CommonPlatformHelper.registerBlock("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission()));
    public static final Supplier<Block> DUCKWEED = CommonPlatformHelper.registerBlock("duckweed", () -> new WaterlilyBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF)));
    public static final Supplier<Block> GLOW_GOOP = CommonPlatformHelper.registerBlock("glow_goop", () -> new GlowGoopBlock(BlockBehaviour.Properties.of(Material.AIR).strength(-1.0F, 3600000.8F).noLootTable().noOcclusion().lightLevel(GlowGoopBlock.LIGHT_EMISSION).sound(SoundType.HONEY_BLOCK)));



    public static void init() {
    }

}
