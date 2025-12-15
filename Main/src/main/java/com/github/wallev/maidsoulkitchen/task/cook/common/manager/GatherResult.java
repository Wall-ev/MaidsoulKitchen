package com.github.wallev.maidsoulkitchen.task.cook.common.manager;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.EmptyHandler;

public class GatherResult {

    public static GatherResult FAIL = new GatherResult(EmptyHandler.INSTANCE, Result.NOT_ENOUGH, 0, 0);

    private final IItemHandler itemHandler;
    private final Result result;
    private final int amount;
    private final int slot;

    public GatherResult(IItemHandler itemHandler, Result result, int amount, int slot) {
        this.itemHandler = itemHandler;
        this.result = result;
        this.amount = amount;
        this.slot = slot;
    }

    public GatherResult(IItemHandler itemHandler, int slot) {
        this(itemHandler, Result.DEFAULT, 0, slot);
    }

    public IItemHandler getItemHandler() {
        return itemHandler;
    }

    public Result getResult() {
        return result;
    }

    public ItemStack queryItemStack() {
        return queryItemStack(this.amount);
    }

    public ItemStack queryItemStack(int amount) {
        return itemHandler.extractItem(slot, amount, false);
    }

    public ItemStack backItemStack(ItemStack backItem) {
        return ItemHandlerHelper.insertItemStacked(itemHandler, backItem, false);
    }

    public boolean isFail() {
        return result == Result.NOT_ENOUGH;
    }

    public enum Result {
        DEFAULT,
        ENOUGH,
        NOT_ENOUGH,
        ;
    }

}
