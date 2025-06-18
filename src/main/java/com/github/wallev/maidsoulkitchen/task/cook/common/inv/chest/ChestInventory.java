package com.github.wallev.maidsoulkitchen.task.cook.common.inv.chest;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.util.ItemHandlerWrapper;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ChestInventory {
    public static final int TICK_SCAN_LIMIT = 10;
    private final List<BlockPos> chestPoses = new ArrayList<>();
    private final List<BlockEntity> chestBes = new ArrayList<>();
    private final List<IItemHandler> chestItemHandlers = new ArrayList<>();
    private final ItemInventory itemInventory = new ItemInventory();
    private ItemHandlerWrapper allItemHandlers;
    private int slots = 0;
    private int lastSlot = 0;

    public void init(ChestInvsData data) {
        this.initData(data);
        this.tickScan0();
    }

    public boolean tickScan0() {
        for (int i = 0; i < slots; i++) {
            ItemStack stackInSlot = allItemHandlers.getStackInSlot(i);
            if (stackInSlot.isEmpty()) {
                continue;
            }
            itemInventory.add(stackInSlot);
        }
        return false;
    }

    public boolean tickScan() {
        for (; lastSlot < TICK_SCAN_LIMIT; lastSlot++) {
            if (lastSlot >= slots) {
                return true;
            }
            ItemStack stackInSlot = allItemHandlers.getStackInSlot(lastSlot);
            if (stackInSlot.isEmpty()) {
                continue;
            }
            itemInventory.add(stackInSlot);
        }
        return false;
    }

    public void update() {
        itemInventory.markDirty();
        itemInventory.update();
    }

    public void clear() {
        chestPoses.clear();
        chestBes.clear();
        chestItemHandlers.clear();
        itemInventory.clear();
        allItemHandlers = null;
        slots = 0;
        lastSlot = 0;
    }

    protected void clearAndInitData(ChestInvsData data) {
        this.clear();
        this.initData(data);
    }

    protected void initData(ChestInvsData data) {
        this.chestPoses.addAll(data.chestPoses());
        this.chestBes.addAll(data.chestBes());
        this.chestItemHandlers.addAll(data.chestItemHandlers());
        this.slots = data.invSlots();

        this.allItemHandlers = new ItemHandlerWrapper(chestItemHandlers);
    }

    public List<BlockPos> getChestPoses() {
        return chestPoses;
    }

    public List<BlockEntity> getChestBes() {
        return chestBes;
    }

    public List<IItemHandler> getChestItemHandlers() {
        return chestItemHandlers;
    }

    public ItemInventory getItemInventory() {
        return itemInventory;
    }

    public ItemHandlerWrapper getAllItemHandlers() {
        return allItemHandlers;
    }

    public int getSlots() {
        return slots;
    }
}
