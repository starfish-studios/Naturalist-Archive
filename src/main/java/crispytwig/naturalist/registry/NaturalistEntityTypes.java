package crispytwig.naturalist.registry;

import crispytwig.naturalist.Naturalist;
import crispytwig.naturalist.entity.Crocodile;
import crispytwig.naturalist.entity.Snail;
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
    public static final RegistryObject<EntityType<Crocodile>> CROCODILE = ENTITY_TYPES.register("crocodile", () -> EntityType.Builder.of(Crocodile::new, MobCategory.CREATURE).sized(0.4F, 0.4F).clientTrackingRange(10).build(new ResourceLocation(Naturalist.MOD_ID, "crocodile").toString()));

    @SubscribeEvent
    public static void createAttributes(EntityAttributeCreationEvent event) {
        event.put(NaturalistEntityTypes.SNAIL.get(), Snail.createAttributes().build());
        event.put(NaturalistEntityTypes.CROCODILE.get(), Crocodile.createAttributes().build());
    }
}
