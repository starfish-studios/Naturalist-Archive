package crispytwig.naturalist.event;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.registry.NaturalistEntityTypes;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.event.world.BiomeLoadingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID)
public class CommonEventHandler {

    @SubscribeEvent
    public static void onBiomeLoad(BiomeLoadingEvent event) {
        switch (event.getCategory()) {
            case FOREST -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BEAR.get(), 4, 1, 2));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.DEER.get(), 4, 2, 4));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAKE.get(), 4, 1, 1));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
                event.getSpawns().addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), 5, 3, 6));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BUTTERFLY.get(), 4, 1, 3));
            }
            case TAIGA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BEAR.get(), 4, 1, 2));
            }
            case PLAINS -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAKE.get(), 4, 1, 1));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
                event.getSpawns().addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), 5, 3, 6));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BUTTERFLY.get(), 4, 1, 3));
            }
            case SWAMP -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAKE.get(), 4, 1, 1));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
                event.getSpawns().addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), 5, 3, 6));
            }
            case MESA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RATTLESNAKE.get(), 4, 1, 1));
            }
            case SAVANNA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RATTLESNAKE.get(), 4, 1, 1));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
            }
            case DESERT -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RATTLESNAKE.get(), 4, 1, 1));
            }
            case JUNGLE -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CORAL_SNAKE.get(), 4, 1, 1));
            }
            case BEACH -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CORAL_SNAKE.get(), 4, 1, 1));
            }
            case RIVER -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CORAL_SNAKE.get(), 4, 1, 1));
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
            }
            case EXTREME_HILLS -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
            }
            case MUSHROOM -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
                event.getSpawns().addSpawn(MobCategory.AMBIENT, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), 5, 3, 6));
            }
            case UNDERGROUND -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
            }
            case MOUNTAIN -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3));
            }
        }
    }
}
