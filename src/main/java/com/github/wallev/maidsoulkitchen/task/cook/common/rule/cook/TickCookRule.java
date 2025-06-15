package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public abstract class TickCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    protected EntityMaid maid;
    protected WrappedMaidFakePlayer player;
    protected B be;
    protected BlockPos pos;

    private boolean end = false;
    protected int tick = 0;

    @Override
    public boolean tickCan(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        return be != null && !this.end;
    }

    @Override
    public void tickStop(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        this.clear(cookBeBase, rm);
    }

    public ItemStack swapItem(InteractionHand hand, ItemStack itemStack, EntityMaid maid, IItemHandler inv) {
        ItemStack itemInHand = maid.getItemInHand(hand);
        if (itemInHand.is(itemStack.getItem())) {
            return itemInHand;
        }

        ItemStack swapItemCopy = itemStack.copyAndClear();
//        ItemStack handItemCopy = maid.getItemInHand(hand).copyAndClear();

        maid.setItemInHand(hand, swapItemCopy);
        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inv, itemInHand, false);
        if (!leftStack.isEmpty()) {
            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
        }
        return swapItemCopy;
    }

    public ItemStack swapTool(ItemStack toolItemStack, ItemInventory itemInventory, EntityMaid maid, InteractionHand hand, IItemHandler inv) {
        ItemStack itemInHand = maid.getItemInHand(hand);
        if (itemInHand.is(toolItemStack.getItem())) {
            return itemInHand;
        }

        LinkedList<ItemStack> toolStacks = itemInventory.getItemStacks(toolItemStack);
        LinkedList<ItemStack> toolStacks0 = itemInventory.getItemStacks(ItemDefinition.of(toolItemStack));
        if (toolStacks.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack swapItemCopy = toolStacks.poll().copyAndClear();
        maid.setItemInHand(hand, swapItemCopy);
        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inv, itemInHand, false);
        if (!leftStack.isEmpty()) {
            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
        }
        toolStacks.addFirst(swapItemCopy);
        toolStacks0.set(0, swapItemCopy);
        return swapItemCopy;
    }

//    public ItemStack swapItem(InteractionHand hand, ItemStack itemStack, EntityMaid maid, IItemHandler inv) {
//        ItemStack swapItemCopy = itemStack.copyAndClear();
//
//        ItemStack handItem = maid.getItemInHand(hand);
//        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inv, handItem, false);
//        maid.setItemInHand(hand, swapItemCopy);
//        if (!leftStack.isEmpty()) {
//            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
//        }
//        return swapItemCopy;
//    }

    public ItemStack getItem(ItemDefinition definition, ItemInventory itemInventory) {
        LinkedList<ItemStack> itemStacks = itemInventory.getItemStacks(definition);
        return itemStacks.isEmpty() ? ItemStack.EMPTY : itemStacks.peek();
    }

    public ItemStack getItem(Item item, ItemInventory itemInventory) {
        LinkedList<ItemStack> itemStacks = itemInventory.getItemStacks(item);
        return itemStacks.isEmpty() ? ItemStack.EMPTY : itemStacks.peek();
    }

    public ItemStack contItemStack(Item item, int count, ItemInventory itemInventory) {
        return this.contItemStack(count, itemInventory.getItemStacks(item));
    }

    public ItemStack contItemStack(MaidItem maidItem, ItemInventory itemInventory) {
        return this.contItemStack(maidItem.item(), maidItem.count(), itemInventory);
    }

    public ItemStack contItemStack(ItemDefinition definition, int containerAmount, ItemInventory itemInventory) {
        LinkedList<ItemStack> stackList = itemInventory.getItemStacks(definition);
        return contItemStack(containerAmount, stackList);
    }

    public ItemStack contItemStack(int containerAmount, LinkedList<ItemStack> stackList) {
        if (stackList == null || stackList.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack contItemStack = stackList.poll();
        containerAmount -= contItemStack.getCount();
        while (containerAmount > 0) {
            ItemStack peek = stackList.peek();
            if (peek == null) {
                return contItemStack;
            }

            int count = peek.getCount();
            if (count >= containerAmount) {
                contItemStack.grow(containerAmount);
                peek.shrink(containerAmount);
                break;
            } else {
                contItemStack.grow(count);
                stackList.removeFirst();
                containerAmount -= count;
                if (containerAmount <= 0) {
                    break;
                }
            }
        }
        return contItemStack;
    }

    protected void init(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        this.maid = cookBeBase.getMaid();
        this.player = WrappedMaidFakePlayer.get(maid);
        this.be = cookBeBase.getBe();
        this.pos = be.getBlockPos();
    }

    protected void clear(CookBeBase<B> cookBeBase, MaidRecipesManager2<R> rm) {
        cookBeBase.markChanged();
        this.maid = null;
        this.player = null;
        this.be = null;
        this.pos = null;
        this.end = false;
        this.tick = 0;
    }

    protected void stop() {
        this.end = true;
    }
}
