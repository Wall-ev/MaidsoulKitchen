package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
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

    public ItemStack getItem(ItemStack itemStack, Map<Item, LinkedList<ItemStack>> invIngredients) {
        Queue<ItemStack> itemStacks = invIngredients.get(itemStack.getItem());
        return itemStacks.isEmpty() ? ItemStack.EMPTY : itemStacks.peek();
    }

    public ItemStack contItemStack(ItemStack itemStack, Map<Item, LinkedList<ItemStack>> invIngredients) {
        return this.contItemStack(itemStack.getItem(), itemStack.getCount(), invIngredients);
    }

    public ItemStack contItemStack(MaidItem maidItem, Map<Item, LinkedList<ItemStack>> invIngredients) {
        return this.contItemStack(maidItem.item(), maidItem.count(), invIngredients);
    }

    public ItemStack contItemStack(Item item, int containerAmount, Map<Item, LinkedList<ItemStack>> invIngredients) {
        LinkedList<ItemStack> itemStacks1 = invIngredients.get(item);
        if (itemStacks1 == null || itemStacks1.isEmpty()) {
            return ItemStack.EMPTY;
        }

        ItemStack contItemStack = itemStacks1.poll();
        containerAmount -= contItemStack.getCount();
        while (containerAmount > 0) {
            ItemStack poll = itemStacks1.poll();
            if (poll == null) {
                return contItemStack;
            }

            int count = poll.getCount();
            if (count >= containerAmount) {
                contItemStack.grow(containerAmount);
                poll.shrink(containerAmount);
                break;
            } else {
                contItemStack.grow(count);
                poll.shrink(count);
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
