package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import java.util.function.Supplier;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;

public class NaturalistEntityTypes {
    public static final Supplier<EntityType<Snail>> SNAIL = CommonPlatformHelper.registerEntityType("snail", Snail::new, SpawnGroup.CREATURE, 0.4F, 0.4F, 10);
    public static final Supplier<EntityType<Bear>> BEAR = CommonPlatformHelper.registerEntityType("bear", Bear::new, SpawnGroup.CREATURE, 1.4F, 1.7F, 10);
    public static final Supplier<EntityType<Butterfly>> BUTTERFLY = CommonPlatformHelper.registerEntityType("butterfly", Butterfly::new, SpawnGroup.CREATURE, 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Firefly>> FIREFLY = CommonPlatformHelper.registerEntityType("firefly", Firefly::new, NaturalistMobCategories.getFireflyCategory(), 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Snake>> SNAKE = CommonPlatformHelper.registerEntityType("snake", Snake::new, SpawnGroup.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Snake>> CORAL_SNAKE = CommonPlatformHelper.registerEntityType("coral_snake", Snake::new, SpawnGroup.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Snake>> RATTLESNAKE = CommonPlatformHelper.registerEntityType("rattlesnake", Snake::new, SpawnGroup.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Deer>> DEER = CommonPlatformHelper.registerEntityType("deer", Deer::new, SpawnGroup.CREATURE, 1.3F, 1.6F, 10);
    public static final Supplier<EntityType<Bird>> BLUEJAY = CommonPlatformHelper.registerEntityType("bluejay", Bird::new, SpawnGroup.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> CANARY = CommonPlatformHelper.registerEntityType("canary", Bird::new, SpawnGroup.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> CARDINAL = CommonPlatformHelper.registerEntityType("cardinal", Bird::new, SpawnGroup.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> ROBIN = CommonPlatformHelper.registerEntityType("robin", Bird::new, SpawnGroup.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Caterpillar>> CATERPILLAR = CommonPlatformHelper.registerEntityType("caterpillar", Caterpillar::new, SpawnGroup.CREATURE, 0.4F, 0.4F, 10);

    public static void init() {}
}
