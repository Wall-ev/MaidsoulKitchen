package com.github.wallev.maidsoulkitchen.task.cook.compat;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.List;
import java.util.Map;

public class InventoryCompat {

    public static boolean isSopStorageBe(BlockEntity blockEntity) {
        return Mods.SOPHISTICATED_STORAGE.isLoaded && SophistorageCompat.isStorageBe(blockEntity);
    }

    public static boolean sopStorageItemData(BlockEntity blockEntity, Map<ItemStack, Pair<IItemHandler, Integer>> stackContentHandler, Map<Item, Integer> available, Map<Item, List<ItemStack>> ingredientAmount) {
        return isSopStorageBe(blockEntity) && SophistorageCompat.storageItemData(blockEntity, stackContentHandler, available, ingredientAmount);
    }

    public static boolean insertSopBe(ItemStack itemStack, BlockEntity blockEntity, boolean requireHasItem) {
        return isSopStorageBe(blockEntity) && SophistorageCompat.insert(itemStack, blockEntity, requireHasItem);
    }
}
