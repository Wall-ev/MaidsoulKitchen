package com.github.catbert.tlma.task.cook.handler.v2.storage;

import com.github.catbert.tlma.item.ItemWios;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.handler.BaubleItemHandler;
import com.jaquadro.minecraft.storagedrawers.api.capabilities.IItemRepository;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController;
import com.jaquadro.minecraft.storagedrawers.util.WorldUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class StorageDrawersHandler {

    public static <T extends Recipe<? extends Container>> boolean trans(EntityMaid maid, List<T> recs) {
        BlockPos blockPos = null;
        BaubleItemHandler maidBauble = maid.getMaidBauble();
        for (int i = 0; i < maidBauble.getSlots(); i++) {
            ItemStack stackInSlot = maidBauble.getStackInSlot(i);
            if (stackInSlot.getItem() instanceof ItemWios) {
                blockPos = ItemWios.getBindingPos(stackInSlot);
                break;
            }
        }

        if (blockPos != null) {
            BlockEntityController blockEntity1 = WorldUtils.getBlockEntity(maid.level(), blockPos, BlockEntityController.class);
            return blockEntity1 != null && transFromStorageDrawers(maid, blockEntity1, recs);
        } else {
            return false;
        }
    }

    public static <T extends Recipe<? extends Container>> boolean transFromStorageDrawers(EntityMaid maid, BlockEntityController blockEntity1, List<T> recs) {
        IItemRepository itemRepository = blockEntity1.getItemRepository();
        NonNullList<IItemRepository.ItemRecord> allItems = itemRepository.getAllItems();
        List<ItemStack> list = allItems.stream()
                .map(itemRecord -> itemRecord.itemPrototype).toList();

        boolean trans = false;
        for (T rec : recs) {
            int maxAmount = 9999;
            boolean canTake = true;
            List<ItemStack> takeStacks = new ArrayList<>();
            for (Ingredient ingredient : rec.getIngredients()) {

                Optional<ItemStack> first = list.stream().filter(ingredient).findFirst();

                if (first.isPresent()) {
                    maxAmount = Math.min(maxAmount, itemRepository.getStoredItemCount(first.get(), stack -> stack.is(first.get().getItem())));
                    takeStacks.add(first.get());
                } else {
                    canTake = false;
                    break;
                }
            }

            if (canTake) {

                boolean canInsert2Maid = true;
                CombinedInvWrapper availableInv = maid.getAvailableInv(true);

                for (ItemStack takeStack : takeStacks) {
                    ItemStack itemStack = itemRepository.extractItem(takeStack, maxAmount, true);
                    if (!ItemHandlerHelper.insertItemStacked(availableInv, itemStack, true).isEmpty()) {
                        canInsert2Maid = false;
                        break;
                    }
                }

                if (canInsert2Maid) {
                    for (ItemStack takeStack : takeStacks) {
                        ItemStack itemStack = itemRepository.extractItem(takeStack, maxAmount, false);
                        ItemHandlerHelper.insertItemStacked(availableInv, itemStack, false);
                    }
                    trans = true;
                }
            }
        }

        blockEntity1.updateCache();

        return trans;
    }
}
