package com.github.catbert.tlma.task.cook.handler.storage;

import com.github.catbert.tlma.mixin.tomstorage.BlockEntityControllerAccessor;
import com.github.catbert.tlma.mixin.tomstorage.SlotRecordAccessor;
import com.jaquadro.minecraft.storagedrawers.api.capabilities.IItemRepository;
import com.jaquadro.minecraft.storagedrawers.block.tile.BlockEntityController;
import com.jaquadro.minecraft.storagedrawers.util.ItemCollectionRegistry;
import net.minecraft.world.item.ItemStack;

import java.util.Collection;

public class TomController {
    public static void calculate(BlockEntityController controller) {
        IItemRepository itemRepository = controller.getItemRepository();

        ItemCollectionRegistry<SlotRecordAccessor> drawerPrimaryLookup = ((BlockEntityControllerAccessor) controller).getDrawerPrimaryLookup();
        Collection<SlotRecordAccessor> entries = drawerPrimaryLookup.getEntries(ItemStack.EMPTY.getItem());
        for (SlotRecordAccessor entry : entries) {

        }

//        ((BlockEntityControllerAccessor) controller).getDrawerPrimaryLookup()
    }
}
