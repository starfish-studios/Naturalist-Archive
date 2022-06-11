package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.registry.NaturalistConfig;
import com.starfish_studios.naturalist.registry.NaturalistEntityTypes;
import com.starfish_studios.naturalist.registry.forge.NaturalistBiomeModifierSerializers;
import me.shedaniel.autoconfig.AutoConfig;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record BearBiomeModifier(HolderSet<Biome> biomes) implements BiomeModifier {
    @Override
    public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD && biomes.contains(biome)) {
            NaturalistConfig config = AutoConfig.getConfigHolder(NaturalistConfig.class).getConfig();
            builder.getMobSpawnSettings().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BEAR.get(), config.bearSpawnWeight, 1, 2));
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return NaturalistBiomeModifierSerializers.BEAR_CODEC.get();
    }
}
