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

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(NaturalistEntityTypes.SNAIL.get(), Snail.createAttributes().build());
        event.put(NaturalistEntityTypes.CROCODILE.get(), Crocodile.createAttributes().build());
        event.put(NaturalistEntityTypes.BEAR.get(), Bear.createAttributes().build());
        event.put(NaturalistEntityTypes.BUTTERFLY.get(), Butterfly.createAttributes().build());
        event.put(NaturalistEntityTypes.FIREFLY.get(), Firefly.createAttributes().build());
    }
}
