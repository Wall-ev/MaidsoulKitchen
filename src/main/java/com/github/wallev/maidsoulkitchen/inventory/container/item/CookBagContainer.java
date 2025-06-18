package com.github.wallev.maidsoulkitchen.inventory.container.item;

import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

public class CookBagContainer extends CookBagAbstractContainer {
    public static final MenuType<CookBagContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookBagContainer(windowId, inv, data.readItem()));
    public final Map<BagType, ItemStackHandler> handlers;

    public CookBagContainer(int id, Inventory inventory, ItemStack cookBag) {
        super(TYPE, id, inventory, cookBag);
        this.handlers = ItemCulinaryHub.getContainers(cookBag);
        this.addBagTypeSlots(handlers);
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickTypeIn, Player player) {
        // 禁阻一切对当前手持物品的交互，防止刷物品 bug
        if (slotId == 27 + player.getInventory().selected) {
            return;
        }
        if (clickTypeIn == ClickType.SWAP) {
            return;
        }
        super.clicked(slotId, button, clickTypeIn, player);
        setContainer(slotId - 36, this.handlers);
    }

    protected void setContainer(int slotId, Map<BagType, ItemStackHandler> handlers) {
        for (BagType value : BagType.VALS) {
            if (slotId >= value.startIndex && slotId < value.endIndex) {
                ItemCulinaryHub.setContainer(cookBag, handlers);
                break;
            }
        }
    }

    protected void addBagTypeSlots(Map<BagType, ItemStackHandler> handlers) {

        int yOffset = 22 + 1;
        {
            for (BagType type : BagType.INPUT_VALS) {
                ItemStackHandler container = handlers.getOrDefault(type, new ItemStackHandler(type.size * 9));
                int i = 0;
                for (int i1 = 0; i1 < type.size; i1++, yOffset += 18) {
                    for (int col = 0; col < 9; ++col, i++) {
                        this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset) {
                            @Override
                            public boolean mayPlace(@NotNull ItemStack stack) {
                                return super.mayPlace(stack) && stack.getItem().canFitInsideContainerItems() && !stack.is(MkItems.CULINARY_HUB.get());
                            }
                        });
                    }
                }
            }
        }

        yOffset += 11;
        {
            int i = 0;
            ItemStackHandler container = handlers.getOrDefault(BagType.OUTPUT, new ItemStackHandler(BagType.OUTPUT.size * 9));
            for (int i1 = 0; i1 < BagType.OUTPUT.size; i1++, yOffset += 18) {
                for (int col = 0; col < 9; ++col, i++) {
                    this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset) {
                        @Override
                        public boolean mayPlace(@NotNull ItemStack stack) {
                            return super.mayPlace(stack) && stack.getItem().canFitInsideContainerItems() && !stack.is(MkItems.CULINARY_HUB.get());
                        }
                    });
                }
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack stack1 = ItemStack.EMPTY;
        Slot slot = this.slots.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack stack2 = slot.getItem();
            stack1 = stack2.copy();
            if (index < 36) {
                if (!this.moveItemStackTo(stack2, 36, this.slots.size(), false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.moveItemStackTo(stack2, 0, 36, false)) {
                return ItemStack.EMPTY;
            }
            if (stack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            setContainer(index, this.handlers);
        }
        return stack1;
    }
}
