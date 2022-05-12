package crispytwig.naturalist.registry;

import crispytwig.naturalist.Naturalist;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class NaturalistTags {
    public static class Items {
        public static final TagKey<Item> BEAR_TEMPT_ITEMS = tag("bear_tempt_items");

        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }

    public static class EntityTypes {
        public static final TagKey<EntityType<?>> BEAR_HOSTILES = tag("bear_hostiles");
        public static final TagKey<EntityType<?>> SNAKE_HOSTILES = tag("snake_hostiles");

        private static TagKey<EntityType<?>> tag(String name) {
            return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
        }
    }
}
