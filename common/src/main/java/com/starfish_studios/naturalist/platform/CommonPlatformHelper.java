package com.starfish_studios.naturalist.platform;

import dev.architectury.injectables.annotations.ExpectPlatform;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.SpawnRestriction;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.item.SpawnEggItem;
import net.minecraft.potion.Potion;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.world.Heightmap;
import net.minecraft.world.entity.*;
import java.util.function.Supplier;

public class CommonPlatformHelper {
    @ExpectPlatform
    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends MobEntity> Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, SpawnGroup category, float width, float height, int clientTrackingRange) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static ItemGroup registerCreativeModeTab(Identifier name, Supplier<ItemStack> icon) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerBrewingRecipe(Potion input, Item ingredient, Potion output) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static <T extends MobEntity> void registerSpawnPlacement(EntityType<T> entityType, SpawnRestriction.Location decoratorType, Heightmap.Type heightMapType, SpawnRestriction.SpawnPredicate<T> decoratorPredicate) {
        throw new AssertionError();
    }

    @ExpectPlatform
    public static void registerCompostable(float chance, ItemConvertible item) {
        throw new AssertionError();
    }
}
