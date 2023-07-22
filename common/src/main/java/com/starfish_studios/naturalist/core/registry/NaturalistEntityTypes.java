package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.common.entity.*;
import com.starfish_studios.naturalist.common.entity.core.projectile.ThrownDuckEgg;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

import java.util.function.Supplier;

public class NaturalistEntityTypes {

    // PROJECTILES


    public static final Supplier<EntityType<ThrownDuckEgg>> DUCK_EGG = CommonPlatformHelper.registerEntityType("duck_egg", ThrownDuckEgg::new, MobCategory.MISC, 0.25F, 0.25F, 16);

    // MOBS

    public static final Supplier<EntityType<Snail>> SNAIL = CommonPlatformHelper.registerEntityType("snail", Snail::new, MobCategory.CREATURE, 0.7F, 0.7F, 10);
    public static final Supplier<EntityType<Bear>> BEAR = CommonPlatformHelper.registerEntityType("bear", Bear::new, MobCategory.CREATURE, 1.4F, 1.7F, 10);
    public static final Supplier<EntityType<Butterfly>> BUTTERFLY = CommonPlatformHelper.registerEntityType("butterfly", Butterfly::new, MobCategory.CREATURE, 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Moth>> MOTH = CommonPlatformHelper.registerEntityType("moth", Moth::new, MobCategory.CREATURE, 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Firefly>> FIREFLY = CommonPlatformHelper.registerEntityType("firefly", Firefly::new, MobCategory.AMBIENT, 0.7F, 0.6F, 8);
    public static final Supplier<EntityType<Snake>> SNAKE = CommonPlatformHelper.registerEntityType("snake", Snake::new, MobCategory.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Snake>> CORAL_SNAKE = CommonPlatformHelper.registerEntityType("coral_snake", Snake::new, MobCategory.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Snake>> RATTLESNAKE = CommonPlatformHelper.registerEntityType("rattlesnake", Snake::new, MobCategory.CREATURE, 0.6F, 0.7F, 8);
    public static final Supplier<EntityType<Deer>> DEER = CommonPlatformHelper.registerEntityType("deer", Deer::new, MobCategory.CREATURE, 1.3F, 1.6F, 10);
    public static final Supplier<EntityType<Bird>> BLUEJAY = CommonPlatformHelper.registerEntityType("bluejay", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> CANARY = CommonPlatformHelper.registerEntityType("canary", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> CARDINAL = CommonPlatformHelper.registerEntityType("cardinal", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Bird>> ROBIN = CommonPlatformHelper.registerEntityType("robin", Bird::new, MobCategory.CREATURE, 0.5F, 0.6F, 8);
    public static final Supplier<EntityType<Caterpillar>> CATERPILLAR = CommonPlatformHelper.registerEntityType("caterpillar", Caterpillar::new, MobCategory.CREATURE, 0.4F, 0.4F, 10);
    public static final Supplier<EntityType<Rhino>> RHINO = CommonPlatformHelper.registerEntityType("rhino", Rhino::new, MobCategory.CREATURE, 2.5F, 3.0F, 10);
    public static final Supplier<EntityType<Lion>> LION = CommonPlatformHelper.registerEntityType("lion", Lion::new, MobCategory.CREATURE, 1.5F, 1.8F, 10);
    public static final Supplier<EntityType<Elephant>> ELEPHANT = CommonPlatformHelper.registerEntityType("elephant", Elephant::new, MobCategory.CREATURE, 2.5F, 3.5F, 10);
    public static final Supplier<EntityType<Zebra>> ZEBRA = CommonPlatformHelper.registerEntityType("zebra", Zebra::new, MobCategory.CREATURE, 1.3964844f, 1.6f, 10);
    public static final Supplier<EntityType<Giraffe>> GIRAFFE = CommonPlatformHelper.registerEntityType("giraffe", Giraffe::new, MobCategory.CREATURE, 1.9f, 5.4f, 10);
    public static final Supplier<EntityType<Hippo>> HIPPO = CommonPlatformHelper.registerEntityType("hippo", Hippo::new, MobCategory.CREATURE, 1.8F, 1.8F, 10);
    public static final Supplier<EntityType<Vulture>> VULTURE = CommonPlatformHelper.registerEntityType("vulture", Vulture::new, MobCategory.CREATURE, 0.9f, 0.5f, 10);
    public static final Supplier<EntityType<Boar>> BOAR = CommonPlatformHelper.registerEntityType("boar", Boar::new, MobCategory.CREATURE, 0.9f, 0.9f, 10);

    public static final Supplier<EntityType<Dragonfly>> DRAGONFLY = CommonPlatformHelper.registerEntityType("dragonfly", Dragonfly::new, MobCategory.AMBIENT, 0.9F, 0.7F, 8);
    public static final Supplier<EntityType<Catfish>> CATFISH = CommonPlatformHelper.registerEntityType("catfish", Catfish::new, MobCategory.WATER_AMBIENT, 1.0F, 0.7F, 8);
    public static final Supplier<EntityType<Alligator>> ALLIGATOR = CommonPlatformHelper.registerEntityType("alligator", Alligator::new, MobCategory.CREATURE, 1.8F, 0.8F, 10);
    public static final Supplier<EntityType<Bass>> BASS = CommonPlatformHelper.registerEntityType("bass", Bass::new, MobCategory.WATER_AMBIENT, 0.7f, 0.4f, 4);
    public static final Supplier<EntityType<Lizard>> LIZARD = CommonPlatformHelper.registerEntityType("lizard", Lizard::new, MobCategory.CREATURE, 0.8F, 0.5F, 10);
    public static final Supplier<EntityType<LizardTail>> LIZARD_TAIL = CommonPlatformHelper.registerEntityType("lizard_tail", LizardTail::new, MobCategory.CREATURE, 0.7f, 0.5f, 10);
    public static final Supplier<EntityType<Tortoise>> TORTOISE = CommonPlatformHelper.registerEntityType("tortoise", Tortoise::new, MobCategory.CREATURE, 1.2F, 0.875F, 10);
    public static final Supplier<EntityType<Duck>> DUCK = CommonPlatformHelper.registerEntityType("duck", Duck::new, MobCategory.CREATURE, 0.6F, 1.0F, 10);
    public static final Supplier<EntityType<Hyena>> HYENA = CommonPlatformHelper.registerEntityType("hyena", Hyena::new, MobCategory.CREATURE, 1.1F, 1.3F, 10);
    public static final Supplier<EntityType<Ostrich>> OSTRICH = CommonPlatformHelper.registerEntityType("ostrich", Ostrich::new, MobCategory.CREATURE, 1.0F, 2.0F, 10);
    public static final Supplier<EntityType<Termite>> TERMITE = CommonPlatformHelper.registerEntityType("termite", Termite::new, MobCategory.CREATURE, 1.0F, 0.7F, 10);


    public static void init() {}
}
