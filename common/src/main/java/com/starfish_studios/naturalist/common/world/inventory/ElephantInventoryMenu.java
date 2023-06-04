package com.starfish_studios.naturalist.common.world.inventory;

import com.starfish_studios.naturalist.common.entity.Elephant;
import net.minecraft.world.Container;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

public class ElephantInventoryMenu extends AbstractContainerMenu {
    private final Container elephantContainer;
    private final Elephant elephant;

    public ElephantInventoryMenu(int i, Inventory inventory, Container container, final Elephant elephant) {
        super(null, i);
        this.elephantContainer = container;
        this.elephant = elephant;
        container.startOpen(inventory.player);
        this.addSlot(new Slot(container, 0, 8, 18) {
            public boolean mayPlace(ItemStack stack) {
                return stack.is(Items.SADDLE) && !this.hasItem() && elephant.isSaddleable();
            }

            public boolean isActive() {
                return elephant.isSaddleable();
            }
        });
        this.addSlot(new Slot(container, 1, 8, 36) {
            public boolean mayPlace(ItemStack stack) {
                return elephant.isArmor(stack);
            }

            public boolean isActive() {
                return elephant.canWearArmor();
            }

            public int getMaxStackSize() {
                return 1;
            }
        });
        int l;
        int m;
        if (this.hasChest(elephant)) {
            for (l = 0; l < 3; ++l) {
                for (m = 0; m < elephant.getInventoryColumns(); ++m) {
                    this.addSlot(new Slot(container, 2 + m + l * elephant.getInventoryColumns(), 80 + m * 18, 18 + l * 18));
                }
            }
        }

        for (l = 0; l < 3; ++l) {
            for (m = 0; m < 9; ++m) {
                this.addSlot(new Slot(inventory, m + l * 9 + 9, 8 + m * 18, 102 + l * 18 + -18));
            }
        }

        for (l = 0; l < 9; ++l) {
            this.addSlot(new Slot(inventory, l, 8 + l * 18, 142));
        }

    }

    public boolean stillValid(Player player) {
        return !this.elephant.hasInventoryChanged(this.elephantContainer) && this.elephantContainer.stillValid(player) && this.elephant.isAlive() && this.elephant.distanceTo(player) < 8.0F;
    }

    private boolean hasChest(AbstractHorse horse) {
        return horse instanceof AbstractChestedHorse && ((AbstractChestedHorse) horse).hasChest();
    }

    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            int i = this.elephantContainer.getContainerSize();
            if (index < i) {
                if (!this.moveItemStackTo(itemStack2, i, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemStack2) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemStack2)) {
                if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (i <= 2 || !this.moveItemStackTo(itemStack2, 2, i, false)) {
                int k = i + 27;
                int m = k + 9;
                if (index >= k && index < m) {
                    if (!this.moveItemStackTo(itemStack2, i, k, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= i && index < k) {
                    if (!this.moveItemStackTo(itemStack2, k, m, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack2, k, k, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        this.elephantContainer.stopOpen(player);
    }
}