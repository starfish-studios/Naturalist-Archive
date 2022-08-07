package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.entity.*;
import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class NaturalistEntityTypes {
    public static final Supplier<EntityType<Snail>> SNAIL = CommonPlatformHelper.registerEntityType("snail", Snail::new, MobCategory.CREATURE, 0.4F, 0.4F, 10);
    public static final Supplier<EntityType<Bear>> BEAR = CommonPlatformHelper.registerEntityType("bear", Bear::new, MobCategory.CREATURE, 1.4F, 1.7F, 10);
    public static final Supplier<EntityType<Butterfly>> BUTTERFLY = CommonPlatformHelper.registerEntityType("butterfly", Butterfly::new, MobCategory.CREATURE, 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Firefly>> FIREFLY = CommonPlatformHelper.registerEntityType("firefly", Firefly::new, NaturalistMobCategories.getFireflyCategory(), 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Snake>> SNAKE = CommonPlatformHelper.registerEntityType("snake", Snake::new, MobCategory.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Snake>> CORAL_SNAKE = CommonPlatformHelper.registerEntityType("coral_snake", Snake::new, MobCategory.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Snake>> RATTLESNAKE = CommonPlatformHelper.registerEntityType("rattlesnake", Snake::new, MobCategory.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Deer>> DEER = CommonPlatformHelper.registerEntityType("deer", Deer::new, MobCategory.CREATURE, 1.3F, 1.6F, 10);
    public static final Supplier<EntityType<Bird>> BLUEJAY = CommonPlatformHelper.registerEntityType("bluejay", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> CANARY = CommonPlatformHelper.registerEntityType("canary", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> CARDINAL = CommonPlatformHelper.registerEntityType("cardinal", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> ROBIN = CommonPlatformHelper.registerEntityType("robin", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Caterpillar>> CATERPILLAR = CommonPlatformHelper.registerEntityType("caterpillar", Caterpillar::new, MobCategory.CREATURE, 0.4F, 0.4F, 10);
    public static final Supplier<EntityType<Rhino>> RHINO = CommonPlatformHelper.registerEntityType("rhino", Rhino::new, MobCategory.CREATURE, 1.95F, 2.2F, 10);
    public static final Supplier<EntityType<Lion>> LION = CommonPlatformHelper.registerEntityType("lion", Lion::new, MobCategory.CREATURE, 1.5F, 1.8F, 10);
    public static final Supplier<EntityType<Elephant>> ELEPHANT = CommonPlatformHelper.registerEntityType("elephant", Elephant::new, MobCategory.CREATURE, 2.5F, 3.5F, 10);
    public static final Supplier<EntityType<Zebra>> ZEBRA = CommonPlatformHelper.registerEntityType("zebra", Zebra::new, MobCategory.CREATURE, 1.3964844f, 1.6f, 10);
    public static final Supplier<EntityType<Giraffe>> GIRAFFE = CommonPlatformHelper.registerEntityType("giraffe", Giraffe::new, MobCategory.CREATURE, 1.9f, 5.4f, 10);
    public static final Supplier<EntityType<Hippo>> HIPPO = CommonPlatformHelper.registerEntityType("hippo", Hippo::new, MobCategory.CREATURE, 1.8F, 1.8F, 10);
    public static final Supplier<EntityType<Vulture>> VULTURE = CommonPlatformHelper.registerEntityType("vulture", Vulture::new, MobCategory.CREATURE, 0.9f, 0.5f, 10);
    public static final Supplier<EntityType<Boar>> BOAR = CommonPlatformHelper.registerEntityType("boar", Boar::new, MobCategory.CREATURE, 0.9f, 0.9f, 10);

    public static void init() {}
}
