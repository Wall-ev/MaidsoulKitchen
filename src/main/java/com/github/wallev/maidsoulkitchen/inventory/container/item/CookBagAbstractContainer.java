package com.github.wallev.maidsoulkitchen.inventory.container.item;

import com.github.wallev.maidsoulkitchen.init.InitItems;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;

public abstract class CookBagAbstractContainer extends AbstractContainerMenu {
    public final ItemStack cookBag;

    public CookBagAbstractContainer(@Nullable MenuType<?> pMenuType, int id, Inventory inventory, ItemStack cookBag) {
        super(pMenuType, id);
        this.cookBag = cookBag;
        this.addPlayerSlots(inventory);
    }

    protected void addPlayerSlots(Inventory inventory) {
        int playerSlotsY = 173 + 1;
        for (int row = 0; row < 3; ++row, playerSlotsY += 18) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(new Slot(inventory, col + row * 9 + 9, 8 + col * 18, playerSlotsY));
            }
        }
        playerSlotsY += 4;

        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(inventory, col, 8 + col * 18, playerSlotsY));
        }
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.getMainHandItem().is(InitItems.CULINARY_HUB.get());
    }
}
