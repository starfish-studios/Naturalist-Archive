package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.block.ChrysalisBlock;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Material;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Supplier;

public class NaturalistBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Naturalist.MOD_ID);

    public static final RegistryObject<Block> CHRYSALIS = BLOCKS.register("chrysalis", () -> new ChrysalisBlock(BlockBehaviour.Properties.of(Material.PLANT).randomTicks().strength(0.2F, 3.0F).sound(SoundType.WOOD).noOcclusion().noCollission()));

    public static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = NaturalistBlocks.BLOCKS.register(name, block);
        NaturalistItems.ITEMS.register(name, () -> new BlockItem(toReturn.get(), new Item.Properties().tab(Naturalist.TAB)));
        return toReturn;
    }
}
