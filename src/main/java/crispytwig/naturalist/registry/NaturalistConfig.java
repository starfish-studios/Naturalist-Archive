package crispytwig.naturalist.registry;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

public class NaturalistConfig {
    public static ForgeConfigSpec COMMON_CONFIG;

    public static final ForgeConfigSpec.IntValue SNAIL_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue SNAKE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue CORAL_SNAKE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue RATTLESNAKE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BEAR_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue DEER_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue FIREFLY_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BIRD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BUTTERFLY_SPAWN_WEIGHT;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("A higher spawn weight will make the mob spawn more often.",
                "To prevent a certain mob from spawning, set its weight to 0.");
        COMMON_BUILDER.push("Mob Spawn Weights");
        SNAIL_SPAWN_WEIGHT = COMMON_BUILDER.comment("Snail Spawn Weight").defineInRange("snailSpawnWeight", 4, 0, 1000);
        SNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Snake Spawn Weight").defineInRange("snakeSpawnWeight", 4, 0, 1000);
        CORAL_SNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Coral Snake Spawn Weight").defineInRange("coralSnakeSpawnWeight", 4, 0, 1000);
        RATTLESNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Rattlesnake Spawn Weight").defineInRange("rattlesnakeSpawnWeight", 4, 0, 1000);
        BEAR_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bear Spawn Weight").defineInRange("bearSpawnWeight", 4, 0, 1000);
        DEER_SPAWN_WEIGHT = COMMON_BUILDER.comment("Deer Spawn Weight").defineInRange("deerSpawnWeight", 4, 0, 1000);
        FIREFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Firefly Spawn Weight").defineInRange("fireflySpawnWeight", 8, 0, 1000);
        BIRD_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bird Spawn Weight").defineInRange("birdSpawnWeight", 10, 0, 1000);
        BUTTERFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Butterfly Spawn Weight").defineInRange("butterflySpawnWeight", 4, 0, 1000);

        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) { }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) { }
}
