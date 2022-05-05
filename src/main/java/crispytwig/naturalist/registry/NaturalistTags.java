package crispytwig.naturalist.registry;

import crispytwig.naturalist.Naturalist;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;

public class NaturalistTags {
    public static final TagKey<EntityType<?>> BEAR_HOSTILES = tag("bear_hostiles");

    private static TagKey<EntityType<?>> tag(String name) {
        return TagKey.create(Registry.ENTITY_TYPE_REGISTRY, new ResourceLocation(Naturalist.MOD_ID, name));
    }
}
