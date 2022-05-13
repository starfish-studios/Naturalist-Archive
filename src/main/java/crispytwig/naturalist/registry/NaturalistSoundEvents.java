package crispytwig.naturalist.registry;

import crispytwig.naturalist.Naturalist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class NaturalistSoundEvents {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Naturalist.MOD_ID);

    public static final RegistryObject<SoundEvent> SNAKE_HISS = SOUND_EVENTS.register("snake_hiss", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hiss")));
    public static final RegistryObject<SoundEvent> SNAKE_HURT = SOUND_EVENTS.register("snake_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hurt")));
    public static final RegistryObject<SoundEvent> SNAKE_RATTLE = SOUND_EVENTS.register("snake_rattle", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.rattle")));

}
