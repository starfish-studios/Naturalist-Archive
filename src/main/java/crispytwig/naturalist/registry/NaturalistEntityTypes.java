package crispytwig.naturalist.registry;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(modid = Naturalist.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class NaturalistEntityTypes {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, Naturalist.MOD_ID);

    public static final RegistryObject<EntityType<Snail>> SNAIL = ENTITY_TYPES.register("snail", () -> EntityType.Builder.of(Snail::new, MobCategory.CREATURE).sized(0.4F, 0.4F).clientTrackingRange(10).build(new ResourceLocation(Naturalist.MOD_ID, "snail").toString()));
    public static final RegistryObject<EntityType<Crocodile>> CROCODILE = ENTITY_TYPES.register("crocodile", () -> EntityType.Builder.of(Crocodile::new, MobCategory.CREATURE).sized(1.5F, 0.7F).clientTrackingRange(10).build(new ResourceLocation(Naturalist.MOD_ID, "crocodile").toString()));
    public static final RegistryObject<EntityType<Bear>> BEAR = ENTITY_TYPES.register("bear", () -> EntityType.Builder.of(Bear::new, MobCategory.CREATURE).sized(1.4F, 1.7F).clientTrackingRange(10).build(new ResourceLocation(Naturalist.MOD_ID, "bear").toString()));
    public static final RegistryObject<EntityType<Butterfly>> BUTTERFLY = ENTITY_TYPES.register("butterfly", () -> EntityType.Builder.of(Butterfly::new, MobCategory.CREATURE).sized(0.7F, 0.6F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "butterfly").toString()));
    public static final RegistryObject<EntityType<Firefly>> FIREFLY = ENTITY_TYPES.register("firefly", () -> EntityType.Builder.of(Firefly::new, MobCategory.CREATURE).sized(0.7F, 0.6F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "firefly").toString()));
    public static final RegistryObject<EntityType<Snake>> SNAKE = ENTITY_TYPES.register("snake", () -> EntityType.Builder.of(Snake::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "snake").toString()));
    public static final RegistryObject<EntityType<CoralSnake>> CORAL_SNAKE = ENTITY_TYPES.register("coral_snake", () -> EntityType.Builder.of(CoralSnake::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "coral_snake").toString()));
    public static final RegistryObject<EntityType<RattleSnake>> RATTLESNAKE = ENTITY_TYPES.register("rattlesnake", () -> EntityType.Builder.of(RattleSnake::new, MobCategory.CREATURE).sized(0.6F, 0.7F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "rattlesnake").toString()));
    public static final RegistryObject<EntityType<Deer>> DEER = ENTITY_TYPES.register("deer", () -> EntityType.Builder.of(Deer::new, MobCategory.CREATURE).sized(1.3F, 1.6F).clientTrackingRange(10).build(new ResourceLocation(Naturalist.MOD_ID, "deer").toString()));
    public static final RegistryObject<EntityType<Bird>> BLUEJAY = ENTITY_TYPES.register("bluejay", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "bluejay").toString()));
    public static final RegistryObject<EntityType<Bird>> CANARY = ENTITY_TYPES.register("canary", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "canary").toString()));
    public static final RegistryObject<EntityType<Bird>> CARDINAL = ENTITY_TYPES.register("cardinal", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "cardinal").toString()));
    public static final RegistryObject<EntityType<Bird>> ROBIN = ENTITY_TYPES.register("robin", () -> EntityType.Builder.of(Bird::new, MobCategory.CREATURE).sized(0.5F, 0.9F).clientTrackingRange(8).build(new ResourceLocation(Naturalist.MOD_ID, "robin").toString()));

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(NaturalistEntityTypes.SNAIL.get(), Snail.createAttributes().build());
        event.put(NaturalistEntityTypes.CROCODILE.get(), Crocodile.createAttributes().build());
        event.put(NaturalistEntityTypes.BEAR.get(), Bear.createAttributes().build());
        event.put(NaturalistEntityTypes.BUTTERFLY.get(), Butterfly.createAttributes().build());
        event.put(NaturalistEntityTypes.FIREFLY.get(), Firefly.createAttributes().build());
        event.put(NaturalistEntityTypes.SNAKE.get(), Snake.createAttributes().build());
        event.put(NaturalistEntityTypes.CORAL_SNAKE.get(), Snake.createAttributes().build());
        event.put(NaturalistEntityTypes.RATTLESNAKE.get(), Snake.createAttributes().build());
        event.put(NaturalistEntityTypes.DEER.get(), Deer.createAttributes().build());
        event.put(NaturalistEntityTypes.BLUEJAY.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.CANARY.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.CARDINAL.get(), Bird.createAttributes().build());
        event.put(NaturalistEntityTypes.ROBIN.get(), Bird.createAttributes().build());
    }
}
