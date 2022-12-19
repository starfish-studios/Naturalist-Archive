package com.starfish_studios.naturalist.registry.forge;

import com.mojang.serialization.Codec;
import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.world.forge.AddAnimalsBiomeModifier;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistBiomeModifiers {

    public static DeferredRegister<Codec<? extends BiomeModifier>> BIOME_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, Naturalist.MOD_ID);

    public static RegistryObject<Codec<AddAnimalsBiomeModifier>> ADD_ANIMALS_CODEC = BIOME_MODIFIER_SERIALIZERS.register("add_animals", () -> Codec.unit(AddAnimalsBiomeModifier::new));
}
