package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class NaturalistTags {
    public static class Items {
        public static final TagKey<Item> BEAR_TEMPT_ITEMS = tag("bear_tempt_items");
        public static final TagKey<Item> SNAKE_TEMPT_ITEMS = tag("snake_tempt_items");
        public static final TagKey<Item> BIRD_FOOD_ITEMS = tag("bird_food_items");

        private static TagKey<Item> tag(String name) {
            return TagKey.create(Registry.ITEM_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BEAR_HOSTILES = tag("bear_hostiles");
        public static final TagKey<EntityType<?>> SNAKE_HOSTILES = tag("snake_hostiles");
        public static final TagKey<EntityType<?>> DEER_PREDATORS = tag("deer_predators");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }
}
