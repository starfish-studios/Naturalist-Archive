package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.Naturalist;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class NaturalistSoundEvents {
    public static final Supplier<SoundEvent> SNAKE_HISS = NaturalistRegistryHelper.registerSoundEvent("snake_hiss", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hiss")));
    public static final Supplier<SoundEvent> SNAKE_HURT = NaturalistRegistryHelper.registerSoundEvent("snake_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hurt")));
    public static final Supplier<SoundEvent> SNAKE_RATTLE = NaturalistRegistryHelper.registerSoundEvent("snake_rattle", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.rattle")));
    public static final Supplier<SoundEvent> SNAIL_CRUSH = NaturalistRegistryHelper.registerSoundEvent("snail_crush", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.crush")));
    public static final Supplier<SoundEvent> SNAIL_FORWARD = NaturalistRegistryHelper.registerSoundEvent("snail_forward", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.forward")));
    public static final Supplier<SoundEvent> SNAIL_BACK = NaturalistRegistryHelper.registerSoundEvent("snail_back", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.back")));
    public static final Supplier<SoundEvent> BEAR_HURT = NaturalistRegistryHelper.registerSoundEvent("bear_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.hurt")));
    public static final Supplier<SoundEvent> BEAR_DEATH = NaturalistRegistryHelper.registerSoundEvent("bear_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.death")));
    public static final Supplier<SoundEvent> BEAR_AMBIENT = NaturalistRegistryHelper.registerSoundEvent("bear_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient")));
    public static final Supplier<SoundEvent> BEAR_AMBIENT_BABY = NaturalistRegistryHelper.registerSoundEvent("bear_ambient_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient_baby")));
    public static final Supplier<SoundEvent> BEAR_SLEEP = NaturalistRegistryHelper.registerSoundEvent("bear_sleep", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sleep")));
    public static final Supplier<SoundEvent> BEAR_SNIFF = NaturalistRegistryHelper.registerSoundEvent("bear_sniff", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sniff")));
    public static final Supplier<SoundEvent> BEAR_SPIT = NaturalistRegistryHelper.registerSoundEvent("bear_spit", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.spit")));
    public static final Supplier<SoundEvent> BEAR_EAT = NaturalistRegistryHelper.registerSoundEvent("bear_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.eat")));
    public static final Supplier<SoundEvent> BIRD_HURT = NaturalistRegistryHelper.registerSoundEvent("bird_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.hurt")));
    public static final Supplier<SoundEvent> BIRD_DEATH = NaturalistRegistryHelper.registerSoundEvent("bird_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.death")));
    public static final Supplier<SoundEvent> BIRD_EAT = NaturalistRegistryHelper.registerSoundEvent("bird_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.eat")));
    public static final Supplier<SoundEvent> BIRD_FLY = NaturalistRegistryHelper.registerSoundEvent("bird_fly", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.fly")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_BLUEJAY = NaturalistRegistryHelper.registerSoundEvent("bird_ambient_bluejay", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_bluejay")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CANARY = NaturalistRegistryHelper.registerSoundEvent("bird_ambient_canary", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_canary")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_ROBIN = NaturalistRegistryHelper.registerSoundEvent("bird_ambient_robin", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_robin")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CARDINAL = NaturalistRegistryHelper.registerSoundEvent("bird_ambient_cardinal", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_cardinal")));
    public static final Supplier<SoundEvent> FIREFLY_HURT = NaturalistRegistryHelper.registerSoundEvent("firefly_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hurt")));
    public static final Supplier<SoundEvent> FIREFLY_DEATH = NaturalistRegistryHelper.registerSoundEvent("firefly_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.death")));
    public static final Supplier<SoundEvent> FIREFLY_HIDE = NaturalistRegistryHelper.registerSoundEvent("firefly_hide", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hide")));
    
    public static void init() {}
}
