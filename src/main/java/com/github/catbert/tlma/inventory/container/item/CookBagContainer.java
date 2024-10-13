package com.github.catbert.tlma.inventory.container.item;

import com.github.catbert.tlma.item.ItemCookBag;
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

import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class CookBagContainer extends CookBagAbstractContainer {
    public static final MenuType<CookBagContainer> TYPE = IForgeMenuType.create((windowId, inv, data) -> new CookBagContainer(windowId, inv, data.readItem()));
    //    public final ItemStackHandler container;
    public final Map<BagType, ItemStackHandler> handlers;

    public CookBagContainer(int id, Inventory inventory, ItemStack cookBag) {
        super(TYPE, id, inventory, cookBag);
        this.handlers = ItemCookBag.getContainers(cookBag);
        this.addBagTypeSlots(handlers);
//        this.container = ItemCookBag.getContainer(cookBag);
//        addBagTypeSlots(container);
    }

    @Override
    public void clicked(int slotId, int button, ClickType clickTypeIn, Player player) {
        if (clickTypeIn == ClickType.SWAP) {
            return;
        }
        super.clicked(slotId, button, clickTypeIn, player);
        setContainer(slotId - 36, this.handlers);
//        ItemCookBag.setContainer(cookBag, container);
    }

    protected void setContainer(int slotId, Map<BagType, ItemStackHandler> handlers) {
        for (BagType value : BagType.values()) {
            if (slotId >= value.startIndex && slotId < value.endIndex) {
                ItemCookBag.setContainer(cookBag, handlers);
                break;
            }
        }
    }

    protected void addBagTypeSlots(Map<BagType, ItemStackHandler> handlers) {

        int yOffset = 22 + 1, i = 0;
        ItemStackHandler container = handlers.getOrDefault(BagType.INGREDIENT, new ItemStackHandler(BagType.INGREDIENT.size * 9));
        for (int i1 = 0; i1 < BagType.INGREDIENT.size; i1++, yOffset += 18) {
            for (int col = 0; col < 9; ++col, i++) {
                this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset) {
                    @Override
                    public boolean mayPlace(@NotNull ItemStack stack) {
                        return super.mayPlace(stack) && stack.getItem().canFitInsideContainerItems();
                    }
                });
            }
        }
        yOffset += 11;

        List<BagType> list = Arrays.stream(BagType.values()).skip(1).toList();

        for (BagType value : list) {
            int j = 0;
            ItemStackHandler container1 = handlers.getOrDefault(value, new ItemStackHandler(value.size * 9));
            for (int row = 0; row < value.size; ++row, yOffset += 18) {
                for (int col = 0; col < 9; ++col, j++) {
                    this.addSlot(new SlotItemHandler(container1, j, 8 + col * 18, yOffset) {
                        @Override
                        public boolean mayPlace(@NotNull ItemStack stack) {
                            return super.mayPlace(stack) && stack.getItem().canFitInsideContainerItems();
                        }
                    });
                }
            }
        }
    }

//    protected void addBagTypeSlots(ItemStackHandler container) {
//
//        int yOffset = 22 + 1, i = 0;
//        for (int i1 = 0; i1 < BagType.INGREDIENT.size; i1++, yOffset += 18) {
//            for (int col = 0; col < 9; ++col, i++) {
//                this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset) {
//                    @Override
//                    public boolean mayPlace(@NotNull ItemStack stack) {
//                        return super.mayPlace(stack) && stack.getItem().canFitInsideContainerItems();
//                    }
//                });
//            }
//        }
//        yOffset += 11;
//
//        List<BagType> list = Arrays.stream(BagType.values()).skip(1).toList();
//
//        for (BagType value : list) {
//            int size = value.size;
//            for (int row = 0; row < size; ++row, yOffset += 18) {
//                for (int col = 0; col < 9; ++col, i++) {
//                    this.addSlot(new SlotItemHandler(container, i, 8 + col * 18, yOffset) {
//                        @Override
//                        public boolean mayPlace(@NotNull ItemStack stack) {
//                            return super.mayPlace(stack) && stack.getItem().canFitInsideContainerItems();
//                        }
//                    });
//                }
//            }
//        }
//    }
}
