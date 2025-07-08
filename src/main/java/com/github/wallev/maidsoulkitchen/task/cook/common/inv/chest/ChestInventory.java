package com.github.wallev.maidsoulkitchen.task.cook.common.inv.chest;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.util.WrapperItemHandler;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;

import java.util.ArrayList;
import java.util.List;

public class ChestInventory {
    public static final int TICK_SCAN_LIMIT = 10;
    public static final Codec<ChestInventory> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            BlockPos.CODEC.listOf().fieldOf("chestPoses").forGetter(o -> o.chestPoses),
            ItemInventory.CODEC.fieldOf("itemInventory").forGetter(o -> o.itemInventory)
    ).apply(ins, ChestInventory::new));
    private final List<BlockPos> chestPoses;
    private final List<BlockEntity> chestBes;
    private final List<IItemHandler> chestItemHandlers;
    private final ItemInventory itemInventory;
    private WrapperItemHandler allItemHandlers;
    private int slots = 0;
    private int lastSlot = 0;

    private long lastTime = 0;

    private ChestInventory(List<BlockPos> chestPoses, ItemInventory itemInventory) {
        this.chestPoses = chestPoses;
        this.chestBes = new ArrayList<>();
        this.chestItemHandlers = new ArrayList<>();
        this.itemInventory = itemInventory;
        this.reset();
    }

    public ChestInventory() {
        this(new ArrayList<>(), new ItemInventory());
    }

    public void init(ChestInvsData data) {
        this.initData(data);
    }

    public boolean needReUpdate() {
        return true;
    }

    public boolean tickScan() {
        for (int i = 0; i < TICK_SCAN_LIMIT; i++) {
            if (lastSlot >= slots) {
                return true;
            }
            ItemStack stackInSlot = allItemHandlers.getStackInSlot(lastSlot);
            if (stackInSlot.isEmpty()) {
                lastSlot++;
                continue;
            }
            itemInventory.add(stackInSlot);
            lastSlot++;
        }
        return false;
    }

    public void update() {
        itemInventory.markDirty();
        itemInventory.update();
    }

    protected void reset() {
        chestBes.clear();
        chestItemHandlers.clear();
        allItemHandlers = null;
        slots = 0;
        lastSlot = 0;
    }

    public void clear() {
        this.reset();
        chestPoses.clear();
        itemInventory.clear();
    }

    public boolean done() {
        return lastSlot >= slots;
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

        this.allItemHandlers = new WrapperItemHandler(chestItemHandlers);
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

    public WrapperItemHandler getAllItemHandlers() {
        return allItemHandlers;
    }

    public int getSlots() {
        return slots;
    }
}
