package com.starfish_studios.naturalist.registry.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class NaturalistConfigForge {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.IntValue SNAIL_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue SNAKE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue CORAL_SNAKE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue RATTLESNAKE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BEAR_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue DEER_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue FIREFLY_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BLUEJAY_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue CANARY_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue CARDINAL_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue ROBIN_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTERFLY_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue FOREST_RABBIT_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue FOREST_FOX_SPAWN_WEIGHT;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("A higher spawn weight will make the mob spawn more often.",
                "To prevent a certain mob from spawning, set its weight to 0.");
        COMMON_BUILDER.push("Mob Spawn Weights");
        SNAIL_SPAWN_WEIGHT = COMMON_BUILDER.comment("Snail spawn weight").defineInRange("snailSpawnWeight", 5, 0, 1000);
        SNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Snake spawn weight").defineInRange("snakeSpawnWeight", 4, 0, 1000);
        CORAL_SNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Coral Snake spawn weight").defineInRange("coralSnakeSpawnWeight", 4, 0, 1000);
        RATTLESNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Rattlesnake spawn weight").defineInRange("rattlesnakeSpawnWeight", 4, 0, 1000);
        BEAR_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bear spawn weight").defineInRange("bearSpawnWeight", 8, 0, 1000);
        DEER_SPAWN_WEIGHT = COMMON_BUILDER.comment("Deer spawn weight").defineInRange("deerSpawnWeight", 8, 0, 1000);
        FIREFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Firefly spawn weight").defineInRange("fireflySpawnWeight", 8, 0, 1000);
        BLUEJAY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bluejay spawn weight").defineInRange("bluejaySpawnWeight", 8, 0, 1000);
        CANARY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Canary spawn weight").defineInRange("canarySpawnWeight", 8, 0, 1000);
        CARDINAL_SPAWN_WEIGHT = COMMON_BUILDER.comment("Cardinal spawn weight").defineInRange("cardinalSpawnWeight", 8, 0, 1000);
        ROBIN_SPAWN_WEIGHT = COMMON_BUILDER.comment("Robin spawn weight").defineInRange("robinSpawnWeight", 8, 0, 1000);
        BUTTERFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Butterfly spawn weight").defineInRange("butterflySpawnWeight", 6, 0, 1000);
        FOREST_RABBIT_SPAWN_WEIGHT = COMMON_BUILDER.comment("Rabbit spawn weight in forest biomes").defineInRange("forestRabbitSpawnWeight", 6, 0, 1000);
        FOREST_FOX_SPAWN_WEIGHT = COMMON_BUILDER.comment("Fox spawn weight in forest biomes").defineInRange("forestFoxSpawnWeight", 6, 0, 1000);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) { }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) { }
}
