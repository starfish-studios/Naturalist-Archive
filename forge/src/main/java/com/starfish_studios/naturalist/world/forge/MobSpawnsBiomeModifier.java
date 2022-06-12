package com.starfish_studios.naturalist.world.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.registry.forge.NaturalistBiomeModifierSerializers;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;

public record MobSpawnsBiomeModifier(HolderSet<Biome> biomes, MobSpawnSettings.SpawnerData spawn, MobCategory category) implements BiomeModifier {
	@Override
	public void modify(Holder<Biome> biome, Phase phase, ModifiableBiomeInfo.BiomeInfo.Builder builder) {
		if (phase == Phase.ADD && this.biomes.contains(biome) && biome.containsTag(BiomeTags.IS_OVERWORLD)) {
			builder.getMobSpawnSettings().getSpawner(category).add(spawn);

		}
	}

	@Override
	public Codec<? extends BiomeModifier> codec() {
		return NaturalistBiomeModifierSerializers.MOB_SPAWNS.get();
	}
}