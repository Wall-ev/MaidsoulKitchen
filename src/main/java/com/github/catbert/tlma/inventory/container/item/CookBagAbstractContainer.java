package com.github.catbert.tlma.inventory.container.item;

import com.github.catbert.tlma.init.InitItems;
import com.github.catbert.tlma.item.bauble.ItemCookBag;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public abstract class CookBagAbstractContainer extends AbstractContainerMenu {
    public final ItemStack cookBag;
    public final ItemStackHandler container;

    public CookBagAbstractContainer(@Nullable MenuType<?> pMenuType, int id, Inventory inventory, ItemStack cookBag) {
        super(pMenuType, id);
        this.cookBag = cookBag;
        this.container = ItemCookBag.getContainer(cookBag);
        this.addPlayerSlots(inventory);
    }

    protected void addBagTypeSlots(ItemStackHandler container) {

        int yOffset = 22 + 1, i = 0;
        for (int i1 = 0; i1 < BagType.INGREDIENT.size; i1++, yOffset += 18) {
            for (int col = 0; col < 9; ++col, i++) {
                this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset));
            }
        }
        yOffset += 11;

        List<BagType> list = Arrays.stream(BagType.values()).skip(1).toList();

        for (BagType value : list) {
            int size = value.size;
            for (int row = 0; row < size; ++row, yOffset += 18) {
                for (int col = 0; col < 9; ++col, i++) {
                    this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset));
                }
            }
        }
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
    public void clicked(int slotId, int button, ClickType clickTypeIn, Player player) {
        if (clickTypeIn == ClickType.SWAP) {
            return;
        }
        super.clicked(slotId, button, clickTypeIn, player);
        ItemCookBag.setContainer(cookBag, container);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index < 27) {
                if (!this.moveItemStackTo(stack2, 27, 36, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, 27, false)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return stack1;
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return pPlayer.getMainHandItem().is(InitItems.COOK_BAG.get());
    }
}
