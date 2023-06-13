package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.block.*;
import com.starfish_studios.naturalist.common.block.crate.*;
import com.starfish_studios.naturalist.core.platform.*;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.*;
import net.minecraft.world.level.block.state.*;
import net.minecraft.world.level.material.*;

import java.util.function.*;

public class NaturalistBlockEntities {
    public static final Supplier<BlockEntityType<AnimalCrateBlockEntity>> ANIMAL_CRATE = CommonPlatformHelper.registerBlockEntityType("animal_crate", () -> BlockEntityType.Builder.of(AnimalCrateBlockEntity::new, NaturalistBlocks.ANIMAL_CRATE.get()).build(null));

    public static void init() {
    }

}
