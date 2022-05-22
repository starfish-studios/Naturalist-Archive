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
        final MobSpawnSettings.SpawnerData snake = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAKE.get(), 4, 1, 1);
        final MobSpawnSettings.SpawnerData bear = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BEAR.get(), 4, 1, 2);
        final MobSpawnSettings.SpawnerData deer = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.DEER.get(), 4, 2, 4);
        final MobSpawnSettings.SpawnerData snail = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.SNAIL.get(), 4, 1, 3);
        final MobSpawnSettings.SpawnerData firefly = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.FIREFLY.get(), 5, 3, 6);
        final MobSpawnSettings.SpawnerData butterfly = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.BUTTERFLY.get(), 4, 1, 3);
        final MobSpawnSettings.SpawnerData rattlesnake = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.RATTLESNAKE.get(), 4, 1, 1);
        final MobSpawnSettings.SpawnerData coralsnake = new MobSpawnSettings.SpawnerData(NaturalistEntityTypes.CORAL_SNAKE.get(), 4, 1, 1);
        switch (event.getCategory()) {
            case FOREST -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, bear);
                event.getSpawns().addSpawn(MobCategory.CREATURE, deer);
                event.getSpawns().addSpawn(MobCategory.CREATURE, snake);
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
                event.getSpawns().addSpawn(MobCategory.AMBIENT, firefly);
                event.getSpawns().addSpawn(MobCategory.CREATURE, butterfly);
            }
            case TAIGA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, bear);
            }
            case PLAINS -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snake);
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
                event.getSpawns().addSpawn(MobCategory.AMBIENT, firefly);
                event.getSpawns().addSpawn(MobCategory.CREATURE, butterfly);
            }
            case SWAMP -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snake);
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
                event.getSpawns().addSpawn(MobCategory.AMBIENT, firefly);
            }
            case MESA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, rattlesnake);
            }
            case SAVANNA -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, rattlesnake);
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
            }
            case DESERT -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, rattlesnake);
            }
            case JUNGLE -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, coralsnake);
            }
            case BEACH -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, coralsnake);
            }
            case RIVER -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, coralsnake);
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
            }
            case EXTREME_HILLS -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
            }
            case MUSHROOM -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
                event.getSpawns().addSpawn(MobCategory.AMBIENT, firefly);
            }
            case UNDERGROUND -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
            }
            case MOUNTAIN -> {
                event.getSpawns().addSpawn(MobCategory.CREATURE, snail);
            }
        }
    }
}
