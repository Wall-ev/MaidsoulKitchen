package com.github.wallev.maidsoulkitchen.task.cook.compat;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.ITrackedContentsItemHandler;
import net.p3pp3rf1y.sophisticatedcore.inventory.InventoryHandler;
import net.p3pp3rf1y.sophisticatedcore.util.WorldHelper;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageBlockEntity;
import net.p3pp3rf1y.sophisticatedstorage.block.StorageWrapper;

import java.util.List;
import java.util.Map;

public class SophistorageCompat {

    protected static void insertItem(ItemStack itemStack, StorageBlockEntity storageBlockEntity, boolean requireHasItem) {
        StorageWrapper storageWrapper = storageBlockEntity.getStorageWrapper();
        if (!requireHasItem) {
            InventoryHandler inventoryHandler = storageWrapper.getInventoryHandler();

            ItemStack leftStack = inventoryHandler.insertItem(itemStack.copy(), false);
            itemStack.shrink(itemStack.getCount() - leftStack.getCount());

            storageBlockEntity.setChanged();
            WorldHelper.notifyBlockUpdate(storageBlockEntity);
        } else {
            ITrackedContentsItemHandler inventoryHandler = storageWrapper.getInventoryForInputOutput();

            ItemStack leftStack = inventoryHandler.insertItem(itemStack.copy(), false);
            itemStack.shrink(itemStack.getCount() - leftStack.getCount());

            storageBlockEntity.setChanged();
            WorldHelper.notifyBlockUpdate(storageBlockEntity);
        }
    }

    protected static void mapItemData(StorageBlockEntity storageBlockEntity, Map<ItemStack, Pair<IItemHandler, Integer>> stackContentHandler, Map<Item, Integer> available, Map<Item, List<ItemStack>> ingredientAmount) {
        StorageWrapper storageWrapper = storageBlockEntity.getStorageWrapper();
        ITrackedContentsItemHandler inventoryForInputOutput = storageWrapper.getInventoryForInputOutput();

        for (int i = 0; i < inventoryForInputOutput.getSlots(); i++) {
            ItemStack stackInSlot = inventoryForInputOutput.getStackInSlot(i);
            if (stackInSlot.isEmpty()) continue;

            Item item = stackInSlot.getItem();

            available.merge(item, stackInSlot.getCount(), Integer::sum);

            stackContentHandler.put(stackInSlot, Pair.of(inventoryForInputOutput, i));

            List<ItemStack> itemStacks = ingredientAmount.get(item);
            if (itemStacks == null) {
                ingredientAmount.put(item, Lists.newArrayList(stackInSlot));
            } else {
                itemStacks.add(stackInSlot);
            }
        }
    }


    protected static boolean storageItemData(BlockEntity blockEntity, Map<ItemStack, Pair<IItemHandler, Integer>> stackContentHandler, Map<Item, Integer> available, Map<Item, List<ItemStack>> ingredientAmount) {
        if (Mods.SOPHISTICATED_STORAGE.isLoaded && blockEntity instanceof StorageBlockEntity storageBlockEntity) {
            mapItemData(storageBlockEntity, stackContentHandler, available, ingredientAmount);
            return true;
        } else {
            return false;
        }
    }

    protected static boolean insert(ItemStack itemStack, BlockEntity blockEntity, boolean requireHasItem) {
        if (blockEntity instanceof StorageBlockEntity storageBlockEntity) {
            insertItem(itemStack, storageBlockEntity, requireHasItem);
            return true;
        } else {
            return false;
        }
    }

    protected static boolean isStorageBe(BlockEntity blockEntity) {
        return blockEntity instanceof StorageBlockEntity storageBlockEntity;
    }
}
