package com.starfish_studios.naturalist.registry.forge;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.io.File;

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
    public static final ForgeConfigSpec.IntValue RHINO_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue LION_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue ELEPHANT_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue ZEBRA_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue GIRAFFE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue HIPPO_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue VULTURE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BOAR_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue DRAGONFLY_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue CATFISH_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue ALLIGATOR_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue BASS_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue LIZARD_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue TORTOISE_SPAWN_WEIGHT;
    public static final ForgeConfigSpec.IntValue DUCK_SPAWN_WEIGHT;

    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("A higher spawn weight will make the mob spawn more often.",
                "To prevent a certain mob from spawning, set its weight to 0.");
        COMMON_BUILDER.push("Mob Spawn Weights");
        
        // VANILLA SPAWN WEIGHTS

        FOREST_RABBIT_SPAWN_WEIGHT = COMMON_BUILDER.comment("Rabbit Forest Spawn Weight").defineInRange("forestRabbitSpawnWeight", 6, 0, 1000);
        FOREST_FOX_SPAWN_WEIGHT = COMMON_BUILDER.comment("Fox Forest Spawn Weight").defineInRange("forestFoxSpawnWeight", 6, 0, 1000);


        // MISC SPAWN WEIGHTS

        SNAIL_SPAWN_WEIGHT = COMMON_BUILDER.comment("Snail Spawn Weight").defineInRange("snailSpawnWeight", 5, 0, 1000);
        SNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Snake Spawn Weight").defineInRange("snakeSpawnWeight", 4, 0, 1000);
        CORAL_SNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Coral Snake Spawn Weight").defineInRange("coralSnakeSpawnWeight", 4, 0, 1000);
        RATTLESNAKE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Rattlesnake Spawn Weight").defineInRange("rattlesnakeSpawnWeight", 4, 0, 1000);
        FIREFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Firefly Spawn Weight").defineInRange("fireflySpawnWeight", 8, 0, 1000);
        BLUEJAY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bluejay Spawn Weight").defineInRange("bluejaySpawnWeight", 8, 0, 1000);
        CANARY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Canary Spawn Weight").defineInRange("canarySpawnWeight", 8, 0, 1000);
        CARDINAL_SPAWN_WEIGHT = COMMON_BUILDER.comment("Cardinal Spawn Weight").defineInRange("cardinalSpawnWeight", 8, 0, 1000);
        ROBIN_SPAWN_WEIGHT = COMMON_BUILDER.comment("Robin Spawn Weight").defineInRange("robinSpawnWeight", 8, 0, 1000);
        BUTTERFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Butterfly Spawn Weight").defineInRange("butterflySpawnWeight", 6, 0, 1000);
        DUCK_SPAWN_WEIGHT = COMMON_BUILDER.comment("Duck Spawn Weight").defineInRange("duckSpawnWeight", 15, 0, 1000);


        // FOREST SPAWN WEIGHTS

        BEAR_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bear Spawn Weight").defineInRange("bearSpawnWeight", 8, 0, 1000);
        DEER_SPAWN_WEIGHT = COMMON_BUILDER.comment("Deer Spawn Weight").defineInRange("deerSpawnWeight", 8, 0, 1000);


        // DRYLANDS SPAWN WEIGHTS

        RHINO_SPAWN_WEIGHT = COMMON_BUILDER.comment("Rhino Spawn Weight").defineInRange("rhinoSpawnWeight", 15, 0, 1000);
        LION_SPAWN_WEIGHT = COMMON_BUILDER.comment("Lion Spawn Weight").defineInRange("lionSpawnWeight", 8, 0, 1000);
        ELEPHANT_SPAWN_WEIGHT = COMMON_BUILDER.comment("Elephant Spawn Weight").defineInRange("elephantSpawnWeight", 10, 0, 1000);
        ZEBRA_SPAWN_WEIGHT = COMMON_BUILDER.comment("Zebra Spawn Weight").defineInRange("zebraSpawnWeight", 10, 0, 1000);
        GIRAFFE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Giraffe Spawn Weight").defineInRange("giraffeSpawnWeight", 10, 0, 1000);
        HIPPO_SPAWN_WEIGHT = COMMON_BUILDER.comment("Hippo Spawn Weight").defineInRange("hippoSpawnWeight", 15, 0, 1000);
        VULTURE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Vulture Spawn Weight").defineInRange("vultureSpawnWeight", 15, 0, 1000);
        BOAR_SPAWN_WEIGHT = COMMON_BUILDER.comment("Boar Spawn Weight").defineInRange("boarSpawnWeight", 8, 0, 1000);


        // SWAMPLANDS SPAWN WEIGHTS
        
        DRAGONFLY_SPAWN_WEIGHT = COMMON_BUILDER.comment("Dragonfly Spawn Weight").defineInRange("dragonflySpawnWeight", 8, 0, 1000);
        CATFISH_SPAWN_WEIGHT = COMMON_BUILDER.comment("Catfish Spawn Weight").defineInRange("catfishSpawnWeight", 8, 0, 1000);
        ALLIGATOR_SPAWN_WEIGHT = COMMON_BUILDER.comment("Alligator Spawn Weight").defineInRange("alligatorSpawnWeight", 15, 0, 1000);
        BASS_SPAWN_WEIGHT = COMMON_BUILDER.comment("Bass Spawn Weight").defineInRange("bassSpawnWeight", 8, 0, 1000);
        LIZARD_SPAWN_WEIGHT = COMMON_BUILDER.comment("Lizard Spawn Weight").defineInRange("lizardSpawnWeight", 15, 0, 1000);
        TORTOISE_SPAWN_WEIGHT = COMMON_BUILDER.comment("Tortoise Spawn Weight").defineInRange("tortoiseSpawnWeight", 15, 0, 1000);
        COMMON_BUILDER.pop();

        COMMON_CONFIG = COMMON_BUILDER.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfigEvent.Loading configEvent) { }

    @SubscribeEvent
    public static void onReload(final ModConfigEvent.Reloading configEvent) { }

    public static void loadConfig(ForgeConfigSpec config, String path) {
        final CommentedFileConfig file = CommentedFileConfig.builder(new File(path)).sync().preserveInsertionOrder().autosave().writingMode(WritingMode.REPLACE).build();
        file.load();
        config.setConfig(file);
    }
}