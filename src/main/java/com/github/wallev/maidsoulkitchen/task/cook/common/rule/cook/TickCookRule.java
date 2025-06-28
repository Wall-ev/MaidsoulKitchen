package com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
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

public abstract class TickCookRule<B extends BlockEntity, R extends Recipe<? extends Container>> extends AbstractCookRule<B, R> {
    protected EntityMaid maid;
    protected WrappedMaidFakePlayer player;
    protected B be;
    protected BlockPos pos;

    protected ItemStack kitchenToolInHand = ItemStack.EMPTY;
    protected final ItemStack kitchenTool;
    protected final Item kitchenToolItem;

    private boolean end = false;
    protected int tick = 0;

    protected TickCookRule(ItemStack kitchenTool) {
        this.kitchenTool = kitchenTool;
        this.kitchenToolItem = kitchenTool.getItem();
    }

    protected TickCookRule(Item kitchenTool) {
        this(kitchenTool.getDefaultInstance());
    }

    protected TickCookRule() {
        this(ItemStack.EMPTY);
    }

    @Override
    public boolean tickCan(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        return be != null && !this.end;
    }

    @Override
    public void tickStop(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        this.clear(cookBeBase, cm);
    }

    @Override
    public final TickCookRule<B, R> getOrCreate() {
        return this.create();
    }

    protected abstract TickCookRule<B, R> create();

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

    public void swapTool(ItemStack tool, ItemInventory itemInventory, EntityMaid maid, IItemHandler inv) {
        ItemStack copyAndClear = tool.copyAndClear();
        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inv, copyAndClear, false);
        if (leftStack.isEmpty()) {
            itemInventory.getStacks().merge(ItemDefinition.of(tool), 1L, (a, b) -> a - b);
            itemInventory.add(copyAndClear);
        } else {
            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
        }
    }

    public void backpackTool(CookBeBase<B> cookBeBase, MaidCookManager<R> cm) {
        if (!kitchenToolInHand.is(kitchenTool.getItem())) {
            return;
        }

        this.swapTool(kitchenToolInHand, cm.getItemInventory(), cm.getMaid(), cm.getInputInv());
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

        while (toolStacks0.peek().isEmpty()) {
            toolStacks0.poll();
        }
        while (toolStacks.peek().isEmpty()) {
            toolStacks.poll();
        }

        ItemStack swapItemCopy = toolStacks.peek().copyAndClear();
        maid.setItemInHand(hand, swapItemCopy);
        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(inv, itemInHand, false);
        if (!leftStack.isEmpty()) {
            maid.level.addFreshEntity(new ItemEntity(maid.level, maid.getX(), maid.getY(), maid.getZ(), leftStack));
        }
        toolStacks.set(0, swapItemCopy);
        toolStacks0.set(0, swapItemCopy);
        this.kitchenToolInHand = swapItemCopy;
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

        // 先不要移出第一个
        // 状况可能如下：第一个ItemStack为33个，但只需要1个。
        ItemStack peek = stackList.peek();
        ItemStack result = peek;
        while (containerAmount > 0) {
            if (peek == null) {
                return result;
            }

            int count = peek.getCount();

            // 33 >= 1，那就分离出来一个
            if (count >= containerAmount) {
                result = peek.split(containerAmount);
                break;
            } else {
                stackList.poll();
                result.grow(count);
                containerAmount -= count;

                // 需要33个，刚刚peek的这个刚好也是33个，那也就是全部拿出来后刚好符合33个，那就不用继续再拿了
                if (containerAmount <= 0) {
                    break;
                }
                // 需要34个，但是第一个只有33个，那就把第一个全部拿出来，再peek下一个
                peek = stackList.peek();
            }
        }
        return result;
    }

    protected void init(CookBeBase<B> cookBeBase, MaidCookManager<R> rm) {
        this.maid = cookBeBase.getMaid();
        this.player = WrappedMaidFakePlayer.get(maid);
        this.be = cookBeBase.getBe();
        this.pos = be.getBlockPos();
    }

    protected void clear(CookBeBase<B> cookBeBase, MaidCookManager<R> rm) {
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
