package com.starfish_studios.naturalist.registry;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.data.worldgen.features.VegetationFeatures;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.util.valueproviders.BiasedToBottomInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.blockpredicates.BlockPredicate;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.BlockColumnConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.RandomPatchConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.SimpleBlockConfiguration;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.placement.*;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

public class NaturalistWorldGen {
    public static class NaturalistFeatures {

        // Cattail
        public static final Holder<ConfiguredFeature<RandomPatchConfiguration, ?>> PATCH_CATTAIL;

        static {
            PATCH_CATTAIL = FeatureUtils.register("patch_cattail", Feature.RANDOM_PATCH, FeatureUtils.simplePatchConfiguration(Feature.SIMPLE_BLOCK, new SimpleBlockConfiguration(BlockStateProvider.simple(NaturalistBlocks.CATTAIL.get()))));
        }
    }

    public static class NaturalistPlacements {
        public static final Holder<PlacedFeature> PATCH_CATTAIL;

        static {
            PATCH_CATTAIL = PlacementUtils.register("patch_cattail", NaturalistFeatures.PATCH_CATTAIL, RarityFilter.onAverageOnceEvery(512), InSquarePlacement.spread(), SurfaceWaterDepthFilter.forMaxDepth(1), PlacementUtils.HEIGHTMAP_OCEAN_FLOOR, BiomeFilter.biome());

        }
    }

}
