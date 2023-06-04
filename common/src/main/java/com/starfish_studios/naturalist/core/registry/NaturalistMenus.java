package com.starfish_studios.naturalist.core.registry;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NaturalistMenus {
    //public static final Supplier<MenuType<ElephantInventoryMenu>> ELEPHANT_INVENTORY = CommonPlatformHelper.registerMenuType("elephant_inventory", () -> CommonPlatformHelper.createMenuType(ElephantInventoryMenu::new));

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory, FriendlyByteBuf byteBuf);
    }
}