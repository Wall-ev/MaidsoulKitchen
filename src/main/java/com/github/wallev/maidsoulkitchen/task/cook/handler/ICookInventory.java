package com.github.wallev.maidsoulkitchen.task.cook.handler;

import com.github.wallev.maidsoulkitchen.inventory.container.item.BagType;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import java.util.List;
import java.util.Map;

public interface ICookInventory {

    void refreshInv();

    void proseLastInvStack(int index, ItemStack invStack);

    void clearCacheStackInfo();

    List<Integer> getBlackSlots();

    void add(ItemStack stack);

    public Map<Item, List<ItemStack>> getInventoryStack();

    public Map<Item, Integer> getInventoryItem();

    List<ItemStack> getLastInvStack();
    default IItemHandlerModifiable getAvailableInv(EntityMaid maid, BagType bagType) {
        return maid.getAvailableInv(true);
    }

    default void syncInv() {

    }

}
