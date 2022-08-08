package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityType;
import net.minecraft.item.Item;
import net.minecraft.tag.TagKey;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.biome.Biome;

public class NaturalistTags {
    public static class BlockTags {
        public static final TagKey<Block> FIREFLIES_SPAWNABLE_ON = tag("fireflies_spawnable_on");
        public static final TagKey<Block> RHINO_CHARGE_BREAKABLE = tag("rhino_charge_breakable");
        public static final TagKey<Block> VULTURE_PERCH_BLOCKS = tag("vulture_perch_blocks");
        public static final TagKey<Block> VULTURES_SPAWNABLE_ON = tag("vultures_spawnable_on");

        private static TagKey<Block> tag(String name) {
            return TagKey.of(Registry.BLOCK_KEY, new Identifier(Naturalist.MOD_ID, name));
        }
    }

    public static class ItemTags {
        public static final TagKey<Item> BEAR_TEMPT_ITEMS = tag("bear_tempt_items");
        public static final TagKey<Item> SNAKE_TEMPT_ITEMS = tag("snake_tempt_items");
        public static final TagKey<Item> BIRD_FOOD_ITEMS = tag("bird_food_items");
        public static final TagKey<Item> GIRAFFE_FOOD_ITEMS = tag("giraffe_food_items");
        public static final TagKey<Item> BOAR_FOOD_ITEMS = tag("boar_food_items");

        private static TagKey<Item> tag(String name) {
            return TagKey.of(Registry.ITEM_KEY, new Identifier(Naturalist.MOD_ID, name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BEAR_HOSTILES = tag("bear_hostiles");
        public static final TagKey<EntityType<?>> SNAKE_HOSTILES = tag("snake_hostiles");
        public static final TagKey<EntityType<?>> DEER_PREDATORS = tag("deer_predators");
        public static final TagKey<EntityType<?>> LION_HOSTILES = tag("lion_hostiles");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.of(Registry.ENTITY_TYPE_KEY, new Identifier(Naturalist.MOD_ID, name));
        }
    }
}
