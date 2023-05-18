package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;

public class NaturalistTags {
    public static class BlockTags {
        public static final TagKey<Block> LAND_SPAWNABLE_ON = tag("land_spawnable_on");
        public static final TagKey<Block> SEMI_AQUATIC_SPAWNABLE_ON = tag("semi_aquatic_spawnable_on");
        public static final TagKey<Block> FLYING_SPAWNABLE_ON = tag("flying_spawnable_on");


        public static final TagKey<Block> FIREFLIES_SPAWNABLE_ON = tag("fireflies_spawnable_on");
        public static final TagKey<Block> VULTURES_SPAWNABLE_ON = tag("vultures_spawnable_on");


        public static final TagKey<Block> RHINO_CHARGE_BREAKABLE = tag("rhino_charge_breakable");
        public static final TagKey<Block> VULTURE_PERCH_BLOCKS = tag("vulture_perch_blocks");


        public static final TagKey<Block> CATTAIL_PLACEABLE = tag("cattail_placeable");
        public static final TagKey<Block> ALLIGATOR_EGG_LAYABLE_ON = tag("alligator_egg_layable_on");
        public static final TagKey<Block> TORTOISE_EGG_LAYABLE_ON = tag("tortoise_egg_layable_on");


        private static TagKey<Block> tag(String name) {
            return TagKey.create(Registry.BLOCK_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }

    public static class ItemTags {
        public static final TagKey<Item> BEAR_TEMPT_ITEMS = tag("bear_tempt_items");
        public static final TagKey<Item> SNAKE_TEMPT_ITEMS = tag("snake_tempt_items");
        public static final TagKey<Item> SNAKE_TAME_ITEMS = tag("snake_tame_items");
        public static final TagKey<Item> BIRD_FOOD_ITEMS = tag("bird_food_items");
        public static final TagKey<Item> GIRAFFE_FOOD_ITEMS = tag("giraffe_food_items");
        public static final TagKey<Item> BOAR_FOOD_ITEMS = tag("boar_food_items");
        public static final TagKey<Item> ALLIGATOR_FOOD_ITEMS = tag("alligator_food_items");
        public static final TagKey<Item> LIZARD_TEMPT_ITEMS = tag("lizard_tempt_items");
        public static final TagKey<Item> TORTOISE_TEMPT_ITEMS = tag("tortoise_tempt_items");
        public static final TagKey<Item> DUCK_FOOD_ITEMS = tag("duck_food_items");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BEAR_HOSTILES = tag("bear_hostiles");
        public static final TagKey<EntityType<?>> SNAKE_HOSTILES = tag("snake_hostiles");
        public static final TagKey<EntityType<?>> DEER_PREDATORS = tag("deer_predators");
        public static final TagKey<EntityType<?>> LION_HOSTILES = tag("lion_hostiles");
        public static final TagKey<EntityType<?>> VULTURE_HOSTILES = tag("vulture_hostiles");
        public static final TagKey<EntityType<?>> CATFISH_HOSTILES = tag("catfish_hostiles");
        public static final TagKey<EntityType<?>> ALLIGATOR_HOSTILES = tag("alligator_hostiles");
        public static final TagKey<EntityType<?>> BOAR_HOSTILES = tag("boar_hostiles");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }

    public static class Biomes {
        public static final TagKey<Biome> HAS_BEAR = tag("has_bear");
        public static final TagKey<Biome> HAS_DEER = tag("has_deer");
        public static final TagKey<Biome> HAS_SNAIL = tag("has_snail");
        public static final TagKey<Biome> HAS_FIREFLY = tag("has_firefly");
        public static final TagKey<Biome> HAS_BUTTERFLY = tag("has_butterfly");
        public static final TagKey<Biome> HAS_SNAKE = tag("has_snake");
        public static final TagKey<Biome> HAS_RATTLESNAKE = tag("has_rattlesnake");
        public static final TagKey<Biome> HAS_CORAL_SNAKE = tag("has_coral_snake");
        public static final TagKey<Biome> HAS_BLUEJAY = tag("has_bluejay");
        public static final TagKey<Biome> HAS_CANARY = tag("has_canary");
        public static final TagKey<Biome> HAS_CARDINAL = tag("has_cardinal");
        public static final TagKey<Biome> HAS_ROBIN = tag("has_robin");
        public static final TagKey<Biome> HAS_RHINO = tag("has_rhino");
        public static final TagKey<Biome> HAS_LION = tag("has_lion");
        public static final TagKey<Biome> HAS_ELEPHANT = tag("has_elephant");
        public static final TagKey<Biome> HAS_ZEBRA = tag("has_zebra");
        public static final TagKey<Biome> HAS_GIRAFFE = tag("has_giraffe");
        public static final TagKey<Biome> HAS_HIPPO = tag("has_hippo");
        public static final TagKey<Biome> HAS_VULTURE = tag("has_vulture");
        public static final TagKey<Biome> HAS_BOAR = tag("has_boar");
        public static final TagKey<Biome> HAS_DRAGONFLY = tag("has_dragonfly");
        public static final TagKey<Biome> HAS_CATFISH = tag("has_catfish");
        public static final TagKey<Biome> HAS_ALLIGATOR = tag("has_alligator");
        public static final TagKey<Biome> HAS_BASS = tag("has_bass");
        public static final TagKey<Biome> HAS_LIZARD = tag("has_lizard");
        public static final TagKey<Biome> HAS_TORTOISE = tag("has_tortoise");
        public static final TagKey<Biome> HAS_DUCK = tag("has_duck");

        private static TagKey<Biome> tag(String name) {
            return TagKey.create(Registry.BIOME_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }
}
