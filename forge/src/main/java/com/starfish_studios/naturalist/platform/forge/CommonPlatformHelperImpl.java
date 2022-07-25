package com.starfish_studios.naturalist.platform.forge;

import com.starfish_studios.naturalist.Naturalist;
import com.starfish_studios.naturalist.item.forge.NoFluidMobBucketItem;
import com.starfish_studios.naturalist.util.forge.NaturalistBrewingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

public class CommonPlatformHelperImpl {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Naturalist.MOD_ID);
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, Naturalist.MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Naturalist.MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Naturalist.MOD_ID);
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, Naturalist.MOD_ID);

    public static <T extends Block> Supplier<T> registerBlock(String name, Supplier<T> block) {
        return BLOCKS.register(name, block);
    }

    public static <T extends Item> Supplier<T> registerItem(String name, Supplier<T> item) {
        return ITEMS.register(name, item);
    }

    public static <T extends Mob> Supplier<SpawnEggItem> registerSpawnEggItem(String name, Supplier<EntityType<T>> entityType, int backgroundColor, int highlightColor) {
        return ITEMS.register(name, () -> new ForgeSpawnEggItem(entityType, backgroundColor, highlightColor, new Item.Properties().tab(Naturalist.TAB)));
    }

    public static Supplier<Item> registerMobBucketItem(String name, Supplier<? extends EntityType<?>> entitySupplier, Supplier<? extends Fluid> fluidSupplier, Supplier<? extends SoundEvent> soundSupplier) {
        return ITEMS.register(name, () -> new NoFluidMobBucketItem(entitySupplier, fluidSupplier, soundSupplier, new Item.Properties().tab(Naturalist.TAB).stacksTo(1)));
    }

    public static <T extends SoundEvent> Supplier<T> registerSoundEvent(String name, Supplier<T> soundEvent) {
        return SOUND_EVENTS.register(name, soundEvent);
    }

    public static <T extends Entity> Supplier<EntityType<T>> registerEntityType(String name, EntityType.EntityFactory<T> factory, MobCategory category, float width, float height, int clientTrackingRange) {
        return ENTITY_TYPES.register(name, () -> EntityType.Builder.of(factory, category).sized(width, height).clientTrackingRange(clientTrackingRange).build(name));
    }

    public static CreativeModeTab registerCreativeModeTab(ResourceLocation name, Supplier<ItemStack> icon) {
        return new CreativeModeTab(name.toLanguageKey()) {
            @Override
            public ItemStack makeIcon() {
                return icon.get();
            }
        };
    }

    public static <T extends Potion> Supplier<T> registerPotion(String name, Supplier<T> potion) {
        return POTIONS.register(name, potion);
    }

    public static void registerBrewingRecipe(Potion input, Item ingredient, Potion output) {
        BrewingRecipeRegistry.addRecipe(new NaturalistBrewingRecipe(input, ingredient, output));
    }

    public static <T extends Mob> void registerSpawnPlacement(EntityType<T> entityType, SpawnPlacements.Type decoratorType, Heightmap.Types heightMapType, SpawnPlacements.SpawnPredicate<T> decoratorPredicate) {
        SpawnPlacements.register(entityType, decoratorType, heightMapType, decoratorPredicate);
    }

    public static void registerCompostable(float chance, ItemLike item) {
        ComposterBlock.COMPOSTABLES.put(item.asItem(), chance);
    }
}
