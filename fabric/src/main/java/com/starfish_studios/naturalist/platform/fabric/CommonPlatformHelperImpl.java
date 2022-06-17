package com.starfish_studios.naturalist.platform.fabric;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.item.fabric.NoFluidMobBucketItem;
import com.starfish_studios.naturalist.mixin.fabric.PotionBrewingInvoker;
import com.starfish_studios.naturalist.mixin.fabric.SpawnPlacementsInvoker;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.itemgroup.FabricItemGroupBuilder;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.fabricmc.fabric.api.registry.CompostingChanceRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
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
import net.minecraft.util.registry.Registry;
import net.minecraft.world.Heightmap;
import net.minecraft.world.entity.*;
import java.util.function.Supplier;

public class CommonPlatformHelperImpl {
    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        var registry = Registry.register(Registry.BLOCK, new Identifier(Naturalist.MOD_ID, name), block.get());
        return () -> registry;
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        var registry = Registry.register(Registry.ITEM, new Identifier(Naturalist.MOD_ID, name), item.get());
        return () -> registry;
    }

    public static <T extends MobEntity> Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        var registry = Registry.register(Registry.ITEM, new Identifier(Naturalist.MOD_ID, name), new SpawnEggItem(entityType.get(), backgroundColor, highlightColor, new Item.Settings().group(Naturalist.TAB)));
        return () -> registry;
    }

    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        var registry = Registry.register(Registry.ITEM, new Identifier(Naturalist.MOD_ID, name), new NoFluidMobBucketItem(entitySupplier, fluidSupplier.get(), soundSupplier.get(), new Item.Settings().group(Naturalist.TAB).maxCount(1)));
        return () -> registry;
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        var registry = Registry.register(Registry.SOUND_EVENT, new Identifier(Naturalist.MOD_ID, name), soundEvent.get());
        return () -> registry;
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, SpawnGroup category, float width, float height, int clientTrackingRange) {
        var registry = Registry.register(Registry.ENTITY_TYPE, new Identifier(Naturalist.MOD_ID, name), FabricEntityTypeBuilder.create(category, factory).dimensions(EntityDimensions.fixed(width, height)).trackRangeChunks(clientTrackingRange).build());
        return () -> registry;
    }

    public static ItemGroup registerCreativeModeTab(Identifier name, Supplier<ItemStack> icon) {
        return FabricItemGroupBuilder.build(name, icon);
    }

    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        var registry = Registry.register(Registry.POTION, new Identifier(Naturalist.MOD_ID, name), potion.get());
        return () -> registry;
    }

    public static void registerBrewingRecipe(Potion input, Item ingredient, Potion output) {
        PotionBrewingInvoker.invokeAddMix(input, ingredient, output);
    }

    public static <T extends MobEntity> void registerSpawnPlacement(EntityType<T> entityType, SpawnRestriction.Location decoratorType, Heightmap.Type heightMapType, SpawnRestriction.SpawnPredicate<T> decoratorPredicate) {
        SpawnPlacementsInvoker.invokeRegister(entityType, decoratorType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemConvertible item) {
        CompostingChanceRegistry.INSTANCE.add(item, chance);
    }
}
