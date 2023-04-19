package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.block.ChrysalisBlock;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.WaterlilyBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;

import java.util.function.Supplier;

public class NaturalistBlocks {
    public static final Supplier<Block> CHRYSALIS = CommonPlatformHelper.registerBlock("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.GRASS).noOcclusion().noCollission()));
    public static final Supplier<Block> DUCKWEED = CommonPlatformHelper.registerBlock("duckweed", () -> new WaterlilyBlock(BlockBehaviour.Properties.of(Material.REPLACEABLE_PLANT).noCollission().randomTicks().instabreak().sound(SoundType.SMALL_DRIPLEAF)));

    public static void init() {
    }

}
