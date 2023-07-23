package com.starfish_studios.naturalist.core.registry;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.core.platform.CommonPlatformHelper;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;

import java.util.function.Supplier;

public class NaturalistSoundEvents {

    // MISC SOUNDS

    public static final Supplier<SoundEvent> SNAKE_HISS = CommonPlatformHelper.registerSoundEvent("snake_hiss", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hiss")));
    public static final Supplier<SoundEvent> SNAKE_HURT = CommonPlatformHelper.registerSoundEvent("snake_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.hurt")));
    public static final Supplier<SoundEvent> SNAKE_RATTLE = CommonPlatformHelper.registerSoundEvent("snake_rattle", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snake.rattle")));
    public static final Supplier<SoundEvent> SNAIL_CRUSH = CommonPlatformHelper.registerSoundEvent("snail_crush", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.crush")));
    public static final Supplier<SoundEvent> SNAIL_FORWARD = CommonPlatformHelper.registerSoundEvent("snail_forward", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.forward")));
    public static final Supplier<SoundEvent> SNAIL_BACK = CommonPlatformHelper.registerSoundEvent("snail_back", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.snail.back")));
    public static final Supplier<SoundEvent> BUCKET_FILL_SNAIL = CommonPlatformHelper.registerSoundEvent("bucket_fill_snail", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "item.bucket.fill_snail")));
    public static final Supplier<SoundEvent> BUCKET_EMPTY_SNAIL = CommonPlatformHelper.registerSoundEvent("bucket_empty_snail", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "item.bucket.empty_snail")));
    public static final Supplier<SoundEvent> BIRD_HURT = CommonPlatformHelper.registerSoundEvent("bird_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.hurt")));
    public static final Supplier<SoundEvent> BIRD_DEATH = CommonPlatformHelper.registerSoundEvent("bird_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.death")));
    public static final Supplier<SoundEvent> BIRD_EAT = CommonPlatformHelper.registerSoundEvent("bird_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.eat")));
    public static final Supplier<SoundEvent> BIRD_FLY = CommonPlatformHelper.registerSoundEvent("bird_fly", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.fly")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_BLUEJAY = CommonPlatformHelper.registerSoundEvent("bird_ambient_bluejay", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_bluejay")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CANARY = CommonPlatformHelper.registerSoundEvent("bird_ambient_canary", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_canary")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_ROBIN = CommonPlatformHelper.registerSoundEvent("bird_ambient_robin", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_robin")));
    public static final Supplier<SoundEvent> BIRD_AMBIENT_CARDINAL = CommonPlatformHelper.registerSoundEvent("bird_ambient_cardinal", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bird.ambient_cardinal")));
    public static final Supplier<SoundEvent> FIREFLY_HURT = CommonPlatformHelper.registerSoundEvent("firefly_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hurt")));
    public static final Supplier<SoundEvent> FIREFLY_DEATH = CommonPlatformHelper.registerSoundEvent("firefly_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.death")));
    public static final Supplier<SoundEvent> FIREFLY_HIDE = CommonPlatformHelper.registerSoundEvent("firefly_hide", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.firefly.hide")));



    // FOREST SOUNDS

    public static final Supplier<SoundEvent> BEAR_HURT = CommonPlatformHelper.registerSoundEvent("bear_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.hurt")));
    public static final Supplier<SoundEvent> BEAR_DEATH = CommonPlatformHelper.registerSoundEvent("bear_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.death")));
    public static final Supplier<SoundEvent> BEAR_AMBIENT = CommonPlatformHelper.registerSoundEvent("bear_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient")));
    public static final Supplier<SoundEvent> BEAR_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("bear_ambient_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.ambient_baby")));
    public static final Supplier<SoundEvent> BEAR_HURT_BABY = CommonPlatformHelper.registerSoundEvent("bear_hurt_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.hurt_baby")));
    public static final Supplier<SoundEvent> BEAR_SLEEP = CommonPlatformHelper.registerSoundEvent("bear_sleep", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sleep")));
    public static final Supplier<SoundEvent> BEAR_SNIFF = CommonPlatformHelper.registerSoundEvent("bear_sniff", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.sniff")));
    public static final Supplier<SoundEvent> BEAR_SPIT = CommonPlatformHelper.registerSoundEvent("bear_spit", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.spit")));
    public static final Supplier<SoundEvent> BEAR_EAT = CommonPlatformHelper.registerSoundEvent("bear_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bear.eat")));
    public static final Supplier<SoundEvent> DEER_AMBIENT = CommonPlatformHelper.registerSoundEvent("deer_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.ambient")));
    public static final Supplier<SoundEvent> DEER_HURT = CommonPlatformHelper.registerSoundEvent("deer_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.hurt")));
    public static final Supplier<SoundEvent> DEER_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("deer_ambient_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.ambient_baby")));
    public static final Supplier<SoundEvent> DEER_HURT_BABY = CommonPlatformHelper.registerSoundEvent("deer_hurt_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.deer.hurt_baby")));



    // SAVANNA SOUNDS

    public static final Supplier<SoundEvent> RHINO_SCRAPE = CommonPlatformHelper.registerSoundEvent("rhino_scrape", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rhino.scrape")));
    public static final Supplier<SoundEvent> RHINO_AMBIENT = CommonPlatformHelper.registerSoundEvent("rhino_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rhino.ambient")));
    public static final Supplier<SoundEvent> RHINO_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("rhino_ambient_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rhino.ambient_baby")));
    public static final Supplier<SoundEvent> LION_HURT = CommonPlatformHelper.registerSoundEvent("lion_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.hurt")));
    public static final Supplier<SoundEvent> LION_AMBIENT = CommonPlatformHelper.registerSoundEvent("lion_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.ambient")));
    public static final Supplier<SoundEvent> LION_ROAR = CommonPlatformHelper.registerSoundEvent("lion_roar", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.lion.roar")));
    public static final Supplier<SoundEvent> ELEPHANT_HURT = CommonPlatformHelper.registerSoundEvent("elephant_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.elephant.hurt")));
    public static final Supplier<SoundEvent> ELEPHANT_AMBIENT = CommonPlatformHelper.registerSoundEvent("elephant_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.elephant.ambient")));
    public static final Supplier<SoundEvent> ZEBRA_AMBIENT = CommonPlatformHelper.registerSoundEvent("zebra_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.ambient")));
    public static final Supplier<SoundEvent> ZEBRA_HURT = CommonPlatformHelper.registerSoundEvent("zebra_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.hurt")));
    public static final Supplier<SoundEvent> ZEBRA_DEATH = CommonPlatformHelper.registerSoundEvent("zebra_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.death")));
    public static final Supplier<SoundEvent> ZEBRA_EAT = CommonPlatformHelper.registerSoundEvent("zebra_eat", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.eat")));
    public static final Supplier<SoundEvent> ZEBRA_BREATHE = CommonPlatformHelper.registerSoundEvent("zebra_breathe", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.breathe")));
    public static final Supplier<SoundEvent> ZEBRA_ANGRY = CommonPlatformHelper.registerSoundEvent("zebra_angry", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.angry")));
    public static final Supplier<SoundEvent> ZEBRA_JUMP = CommonPlatformHelper.registerSoundEvent("zebra_jump", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.zebra.jump")));
    public static final Supplier<SoundEvent> VULTURE_AMBIENT = CommonPlatformHelper.registerSoundEvent("vulture_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.vulture.ambient")));
    public static final Supplier<SoundEvent> VULTURE_HURT = CommonPlatformHelper.registerSoundEvent("vulture_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.vulture.hurt")));
    public static final Supplier<SoundEvent> VULTURE_DEATH = CommonPlatformHelper.registerSoundEvent("vulture_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.vulture.death")));
    public static final Supplier<SoundEvent> GIRAFFE_AMBIENT = CommonPlatformHelper.registerSoundEvent("giraffe_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.giraffe.ambient")));
    public static final Supplier<SoundEvent> HIPPO_AMBIENT = CommonPlatformHelper.registerSoundEvent("hippo_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.hippo.ambient")));
    public static final Supplier<SoundEvent> HIPPO_HURT = CommonPlatformHelper.registerSoundEvent("hippo_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.hippo.hurt")));
    public static final Supplier<SoundEvent> BOAR_AMBIENT = CommonPlatformHelper.registerSoundEvent("boar_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.ambient")));
    public static final Supplier<SoundEvent> BOAR_HURT = CommonPlatformHelper.registerSoundEvent("boar_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.hurt")));
    public static final Supplier<SoundEvent> BOAR_DEATH = CommonPlatformHelper.registerSoundEvent("boar_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.boar.death")));
    public static final Supplier<SoundEvent> HYENA_AMBIENT = CommonPlatformHelper.registerSoundEvent("hyena_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.hyena.ambient")));
    public static final Supplier<SoundEvent> HYENA_HURT = CommonPlatformHelper.registerSoundEvent("hyena_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.hyena.hurt")));

    public static final Supplier<SoundEvent> OSTRICH_AMBIENT = CommonPlatformHelper.registerSoundEvent("ostrich_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.ostrich.ambient")));
    public static final Supplier<SoundEvent> OSTRICH_HURT = CommonPlatformHelper.registerSoundEvent("ostrich_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.ostrich.hurt")));
    public static final Supplier<SoundEvent> OSTRICH_DEATH = CommonPlatformHelper.registerSoundEvent("ostrich_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.ostrich.death")));
    // Ostrich Eggs
    public static final Supplier<SoundEvent> OSTRICH_EGG_BREAK = CommonPlatformHelper.registerSoundEvent("ostrich_egg_break", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.ostrich.egg_break")));
    public static final Supplier<SoundEvent> OSTRICH_EGG_CRACK = CommonPlatformHelper.registerSoundEvent("ostrich_egg_crack", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.ostrich.egg_crack")));
    public static final Supplier<SoundEvent> OSTRICH_EGG_HATCH = CommonPlatformHelper.registerSoundEvent("ostrich_egg_hatch", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.ostrich.egg_hatch")));



    // SWAMP SOUNDS

    // Gator Eggs
    public static final Supplier<SoundEvent> GATOR_EGG_BREAK = CommonPlatformHelper.registerSoundEvent("alligator_egg_break", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.egg_break")));
    public static final Supplier<SoundEvent> GATOR_EGG_CRACK = CommonPlatformHelper.registerSoundEvent("alligator_egg_crack", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.egg_crack")));
    public static final Supplier<SoundEvent> GATOR_EGG_HATCH = CommonPlatformHelper.registerSoundEvent("alligator_egg_hatch", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.egg_hatch")));

    // Tortoise Eggs
    public static final Supplier<SoundEvent> TORTOISE_EGG_BREAK = CommonPlatformHelper.registerSoundEvent("tortoise_egg_break", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.tortoise.egg_break")));
    public static final Supplier<SoundEvent> TORTOISE_EGG_CRACK = CommonPlatformHelper.registerSoundEvent("tortoise_egg_crack", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.tortoise.egg_crack")));
    public static final Supplier<SoundEvent> TORTOISE_EGG_HATCH = CommonPlatformHelper.registerSoundEvent("tortoise_egg_hatch", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.tortoise.egg_hatch")));

    public static final Supplier<SoundEvent> GATOR_AMBIENT = CommonPlatformHelper.registerSoundEvent("alligator_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.ambient")));
    public static final Supplier<SoundEvent> GATOR_AMBIENT_BABY = CommonPlatformHelper.registerSoundEvent("alligator_ambient_baby", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.ambient_baby")));
    public static final Supplier<SoundEvent> GATOR_HURT = CommonPlatformHelper.registerSoundEvent("alligator_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.hurt")));
    public static final Supplier<SoundEvent> GATOR_DEATH = CommonPlatformHelper.registerSoundEvent("alligator_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.alligator.death")));
    public static final Supplier<SoundEvent> CATFISH_FLOP = CommonPlatformHelper.registerSoundEvent("catfish_flop", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.catfish.flop")));
    public static final Supplier<SoundEvent> BASS_FLOP = CommonPlatformHelper.registerSoundEvent("bass_flop", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.bass.flop")));
    public static final Supplier<SoundEvent> DRAGONFLY_LOOP = CommonPlatformHelper.registerSoundEvent("dragonfly_loop", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.dragonfly.loop")));
    // DUCK SOUNDS
    public static final Supplier<SoundEvent> DUCK_AMBIENT = CommonPlatformHelper.registerSoundEvent("duck_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.ambient")));
    public static final Supplier<SoundEvent> DUCK_HURT = CommonPlatformHelper.registerSoundEvent("duck_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.hurt")));
    public static final Supplier<SoundEvent> DUCK_DEATH = CommonPlatformHelper.registerSoundEvent("duck_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.death")));
    public static final Supplier<SoundEvent> DUCK_STEP = CommonPlatformHelper.registerSoundEvent("duck_step", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.duck.step")));
    // RUBBER DUCKY SOUNDS
    public static final Supplier<SoundEvent> RUBBER_DUCKY_AMBIENT = CommonPlatformHelper.registerSoundEvent("rubber_ducky_ambient", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rubber_ducky.ambient")));
    public static final Supplier<SoundEvent> RUBBER_DUCKY_HURT = CommonPlatformHelper.registerSoundEvent("rubber_ducky_hurt", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rubber_ducky.hurt")));
    public static final Supplier<SoundEvent> RUBBER_DUCKY_DEATH = CommonPlatformHelper.registerSoundEvent("rubber_ducky_death", () -> new SoundEvent(new ResourceLocation(Naturalist.MOD_ID, "entity.rubber_ducky.death")));


    public static void init() {}
}
