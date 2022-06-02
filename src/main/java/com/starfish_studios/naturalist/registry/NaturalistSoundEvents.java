package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
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
    public static final RegistryObject<SoundEvent> SNAIL_CRUSH = SOUND_EVENTS.register("snail_crush", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.crush")));
    public static final RegistryObject<SoundEvent> SNAIL_FORWARD = SOUND_EVENTS.register("snail_forward", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.forward")));
    public static final RegistryObject<SoundEvent> SNAIL_BACK = SOUND_EVENTS.register("snail_back", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.back")));
    public static final RegistryObject<SoundEvent> BEAR_HURT = SOUND_EVENTS.register("bear_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.hurt")));
    public static final RegistryObject<SoundEvent> BEAR_DEATH = SOUND_EVENTS.register("bear_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.death")));
    public static final RegistryObject<SoundEvent> BEAR_AMBIENT = SOUND_EVENTS.register("bear_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient")));
    public static final RegistryObject<SoundEvent> BEAR_AMBIENT_BABY = SOUND_EVENTS.register("bear_ambient_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient_baby")));
    public static final RegistryObject<SoundEvent> BEAR_SLEEP = SOUND_EVENTS.register("bear_sleep", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sleep")));
    public static final RegistryObject<SoundEvent> BEAR_SNIFF = SOUND_EVENTS.register("bear_sniff", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sniff")));
    public static final RegistryObject<SoundEvent> BEAR_SPIT = SOUND_EVENTS.register("bear_spit", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.spit")));
    public static final RegistryObject<SoundEvent> BEAR_EAT = SOUND_EVENTS.register("bear_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.eat")));
    public static final RegistryObject<SoundEvent> BIRD_HURT = SOUND_EVENTS.register("bird_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.hurt")));
    public static final RegistryObject<SoundEvent> BIRD_DEATH = SOUND_EVENTS.register("bird_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.death")));
    public static final RegistryObject<SoundEvent> BIRD_EAT = SOUND_EVENTS.register("bird_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.eat")));
    public static final RegistryObject<SoundEvent> BIRD_FLY = SOUND_EVENTS.register("bird_fly", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.fly")));
    public static final RegistryObject<SoundEvent> BIRD_AMBIENT_BLUEJAY = SOUND_EVENTS.register("bird_ambient_bluejay", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_bluejay")));
    public static final RegistryObject<SoundEvent> BIRD_AMBIENT_CANARY = SOUND_EVENTS.register("bird_ambient_canary", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_canary")));
    public static final RegistryObject<SoundEvent> BIRD_AMBIENT_ROBIN = SOUND_EVENTS.register("bird_ambient_robin", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_robin")));
    public static final RegistryObject<SoundEvent> BIRD_AMBIENT_CARDINAL = SOUND_EVENTS.register("bird_ambient_cardinal", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_cardinal")));
    public static final RegistryObject<SoundEvent> FIREFLY_HURT = SOUND_EVENTS.register("firefly_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hurt")));
    public static final RegistryObject<SoundEvent> FIREFLY_DEATH = SOUND_EVENTS.register("firefly_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.death")));

}
