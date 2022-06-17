package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.block.ChrysalisBlock;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import java.util.function.Supplier;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.BlockSoundGroup;

public class NaturalistBlocks {
    public static final Supplier<Block> CHRYSALIS = CommonPlatformHelper.registerBlock("chrysalis", () -> new ChrysalisBlock(AbstractBlock.Settings.of(Material.PLANT).ticksRandomly().strength(0.2F, 3.0F).sounds(BlockSoundGroup.GRASS).nonOpaque().noCollision()));

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        Supplier<T> supplier = CommonPlatformHelper.registerBlock(name, block);
        CommonPlatformHelper.registerItem(name, () -> new BlockItem(supplier.get(), new Item.Settings().group(Naturalist.TAB)));
        return supplier;
    }

    public static void init() {}
}
