package com.starfish_studios.naturalist.registry;

import com.starfish_studios.naturalist.platform.CommonPlatformHelper;
import com.starfish_studios.naturalist.world.inventory.ElephantInventoryMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public class NaturalistMenus {
    //public static final Supplier<MenuType<ElephantInventoryMenu>> ELEPHANT_INVENTORY = CommonPlatformHelper.registerMenuType("elephant_inventory", () -> CommonPlatformHelper.createMenuType(ElephantInventoryMenu::new));

    @FunctionalInterface
    public interface MenuFactory<T extends AbstractContainerMenu> {
        T create(int syncId, Inventory inventory, FriendlyByteBuf byteBuf);
    }
}