package com.github.wallev.maidsoulkitchen.task.cook.common.cook.be;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.ICookBeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.item.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import com.github.wallev.maidsoulkitchen.util.fakeplayer.WrappedMaidFakePlayer;
import com.github.wallev.verhelper.server.item.VItemStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.templates.FluidTank;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Supplier;

public abstract class CookBeBase<B extends BlockEntity> {
    protected final EntityMaid maid;
    protected final WrappedMaidFakePlayer fakePlayer;
    protected final ServerLevel serverLevel;
    protected B be;

    protected List<ItemStack> activeItems;

    public CookBeBase(EntityMaid maid) {
        this.maid = maid;
        this.serverLevel = (ServerLevel) maid.level;
        this.fakePlayer = WrappedMaidFakePlayer.get(maid);

        this.initActiveItemStacks();
    }


    public boolean hasResult() {
        return !this.getResult().isEmpty();
    }

    public void extractResult(IItemHandler result2Inv) {
        takeItem(getResult(), result2Inv);
    }

    public boolean hasInputs() {
        IInvHandler ingredientInv = this.getIngredientInv();
        int start = this.getIngredientSlotStart();
        int size = start + this.getIngredientSize();
        for (int i = start; i < size; i++) {
            if (!ingredientInv.kl$getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    public void takeInputs(IItemHandler inputs2Inv) {
        IInvHandler ingredientInv = this.getIngredientInv();
        int start = this.getIngredientSlotStart();
        int size = start + this.getIngredientSize();
        for (int i = start; i < size; i++) {
            takeItem(ingredientInv.kl$getStackInSlot(i), inputs2Inv);
        }
    }
//
//    public boolean insertInputs(MaidRecipesManager2<?> rm) {
//        MaidRec rec = rm.getMaidRecs().poll();
//        if (rec == null) {
//            return false;
//        }
//        Map<Item, Queue<ItemStack>> invIngredients = rm.getInvIngredients();
//        return insertInputs(rec, invIngredients);
//    }


    protected boolean insertFluidItems(MaidItem fluidItem, ItemInventory itemInventory) {
        ItemDefinition item = fluidItem.item();
        int amount = fluidItem.count();

        LinkedList<ItemStack> fluidItems = itemInventory.getItemStacks(item);
        for (ItemStack itemStack : fluidItems) {
            if (itemStack.isEmpty()) continue;
            int count0 = itemStack.getCount();

            if (count0 >= amount) {
                this.useItem(itemStack, amount);
                break;
            } else {
                this.useItem(itemStack, count0);
                amount -= count0;
                if (amount <= 0) {
                    break;
                }
            }
        }

        return true;
    }

    protected void useItem(ItemStack itemStack, int times) {
        for (int i = 0; i < times; i++) {
            InteractionResult result = fakePlayer.useOnByItem(be.getBlockPos(), itemStack);
            if (result == InteractionResult.FAIL || result == InteractionResult.PASS) {
                break;
            }
        }
    }

    protected void useItem(ItemStack itemStack, Supplier<Boolean> condition) {
        while (!itemStack.isEmpty()) {
            if (!condition.get()) {
                break;
            }

            InteractionResult result = fakePlayer.useOnByItem(be.getBlockPos(), itemStack);
            if (result == InteractionResult.FAIL || result == InteractionResult.PASS) {
                break;
            }
        }
    }

    protected void useItem(ItemStack itemStack) {
        fakePlayer.useOnByItem(be.getBlockPos(), itemStack);
    }



    public boolean insertFluidItems(MaidItem fluidItem, ItemInventory itemInventory, IItemHandlerModifiable toInv) {
        ItemDefinition item = fluidItem.item();
        int amount = fluidItem.count();

        LinkedList<ItemStack> fluidItems = itemInventory.getItemStacks(item);
        for (ItemStack itemStack : fluidItems) {
            if (itemStack.isEmpty()) continue;
            int count0 = itemStack.getCount();

            if (count0 >= amount) {
                this.useItem(itemStack, amount, toInv);
                break;
            } else {
                this.useItem(itemStack, count0, toInv);
                amount -= count0;
                if (amount <= 0) {
                    break;
                }
            }
        }

        return true;
    }

    public InteractionResult useItem(ItemStack itemStack, int times, IItemHandlerModifiable toInv) {
        for (int i = 0; i < times; i++) {
            InteractionResult result = fakePlayer.useOnByItem(be.getBlockPos(), itemStack, toInv);
            if (result == InteractionResult.FAIL || result == InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult useItem(ItemStack itemStack, Supplier<Boolean> condition, IItemHandlerModifiable toInv) {
        while (!itemStack.isEmpty()) {
            if (!condition.get()) {
                break;
            }

            InteractionResult result = fakePlayer.useOnByItem(be.getBlockPos(), itemStack, toInv);
            if (result == InteractionResult.FAIL || result == InteractionResult.PASS) {
                return result;
            }
        }
        return InteractionResult.SUCCESS;
    }

    public InteractionResult useItem(ItemStack itemStack, IItemHandlerModifiable toInv) {
        return fakePlayer.useOnByItem(be.getBlockPos(), itemStack, toInv);
    }

    public InteractionResult useItemWithSneak(ItemStack itemStack, IItemHandlerModifiable toInv) {
        return fakePlayer.useOnByItemWithSneak(be.getBlockPos(), itemStack, toInv);
    }

    public boolean insertInputs(MaidRec rec, ItemInventory itemInventory) {
        IInvHandler ingredientInv = this.getIngredientInv();

        int index = 0;
        for (MaidItem maidItem : rec.maidItems()) {
            if (!maidItem.isEmpty()) {
                ItemDefinition item = maidItem.item();
                int count = maidItem.count();
                insertAndShrink(ingredientInv, count, itemInventory.getItemStacks(item), index);
            }
            index++;
        }
        return true;
    }

    public void insertAndShrink(IInvHandler beInv, Integer amount, Collection<ItemStack> ingredient, int slotIndex) {
        for (ItemStack itemStack : ingredient) {
            if (itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= amount) {
                ItemStack leftInsertedStack = beInv.kl$insertItem(slotIndex, VItemStack.copyWithCount(itemStack, amount), false);
                itemStack.shrink(amount - leftInsertedStack.getCount());
                break;
            } else {
                ItemStack leftInsertedStack = beInv.kl$insertItem(slotIndex, VItemStack.copyWithCount(itemStack, count), false);
                itemStack.shrink(count - leftInsertedStack.getCount());
                amount -= count;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }


    public boolean hasEnoughIngredient(MaidRec rec, Map<Item, Collection<ItemStack>> invIngredients) {

        for (MaidItem maidItem : rec.maidItems()) {
            ItemDefinition item = maidItem.item();
            int actualCount = maidItem.count();
            for (ItemStack itemStack : invIngredients.getOrDefault(item.item(), new LinkedList<>())) {
                actualCount -= itemStack.getCount();
                if (actualCount <= 0) {
                    break;
                }
            }

            if (actualCount > 0) {
                return false;
            }
        }

        return true;
    }

    public boolean hasMeal() {
        return !this.getMeal().isEmpty();
    }

    public boolean hasContainer() {
        return !this.getNowContainer().isEmpty();
    }

    public boolean takeContainer(IItemHandler item2Inv) {
        return takeItem(this.getNowContainer(), item2Inv);
    }

    public boolean insertContainer(ItemStack container) {
        return insertItem(container, this.getContainerInv(), this.getContainerSlot());
    }

    public boolean hasActiveItem() {
        return !this.activeItemStack().isEmpty();
    }

    public boolean takeActiveItem(IItemHandler item2Inv) {
        return takeItem(this.activeItemStack(), item2Inv);
    }

    public boolean insertActiveItem(ItemStack activeItem) {
        return insertItem(activeItem, this.activeItemInv(), this.activeItemSlot());
    }

    public boolean takeItem(ItemStack takeItem, IItemHandler item2Inv) {
        if (!takeItem.isEmpty()) {
            ItemStack copy = takeItem.copy();
            ItemStack leftStack = ItemHandlerHelper.insertItemStacked(item2Inv, copy, false);
            takeItem.shrink(copy.getCount() - leftStack.getCount());
            return true;
        }
        return false;
    }

    public boolean insertItem(ItemStack insertItem, IInvHandler insertInv, int insertSlot) {
        ItemStack copy = insertItem.copy();
        ItemStack leftStack = insertInv.kl$insertItem(insertSlot, copy, false);
        insertItem.shrink(copy.getCount() - leftStack.getCount());
        return true;
    }


    public abstract boolean isCookBe(BlockEntity be);

    public abstract IInvHandler getInv();

    public IInvHandler getIngredientInv() {
        return this.getInv();
    }

    public int getIngredientSlotStart() {
        return 0;
    }

    public abstract int getIngredientSize();

    public IInvHandler getResultInv() {
        return this.getInv();
    }

    public ItemStack getResult() {
        return this.getResultInv().kl$getStackInSlot(this.getResultSlot());
    }

    public abstract int getResultSlot();


    public ItemStack getMeal() {
        return ItemStack.EMPTY;
    }

    public ItemStack getNeedContainer() {
        return ItemStack.EMPTY;
    }

    public ItemStack getNowContainer() {
        return this.getContainerInv().kl$getStackInSlot(this.getContainerSlot());
    }

    public int getContainerSlot() {
        return 0;
    }

    public IInvHandler getContainerInv() {
        return this.getInv();
    }


    public int activeItemSlot() {
        return 0;
    }

    public ItemStack activeItemStack() {
        return this.activeItemInv().kl$getStackInSlot(this.activeItemSlot());
    }

    public void initActiveItemStacks() {
        if (activeItems == null) {
            activeItems = this.contActiveItemStacks();
        }
    }

    protected List<ItemStack> contActiveItemStacks() {
        return Collections.emptyList();
    }

    public List<ItemStack> getActiveItems() {
        return activeItems;
    }

    public IInvHandler activeItemInv() {
        return this.getInv();
    }


    public abstract boolean recMatch();

    protected final <C extends Container, T extends Recipe<C>> boolean recMatch(C container, Function<C, Optional<T>> recipeFunc) {
        if (container != null) {
            Optional<T> apply = recipeFunc.apply(container);
            return apply.isPresent();
        }
        return false;
    }

    protected final boolean recMatchAccessor() {
        return ((ICookBeAccessor) be).kl$canCook();
    }

    public abstract boolean cookStateMatch();

    protected final boolean cookStateMatchAccessor() {
        return ((ICookBeAccessor) be).kl$matchCookState();
    }

    public boolean canTakeResult() {
        return true;
    }

    public void awardExp() {
        if (be instanceof ICookBeAccessor accessor) {
            accessor.kl$awardExp(maid);
        }
    }

    public boolean hasEnoughCategory() {
        return true;
    }

    public List<ItemStack> getCategoryItems() {
        return Collections.emptyList();
    }

    public FluidTank getFluidTank() {
        return null;
    }

    public FluidStack getFluidStack() {
        return this.getFluidTank().getFluid();
    }

    public boolean hasFluid() {
        return !this.getFluidStack().isEmpty();
    }

    public Fluid getFluid() {
        return this.getFluidStack().getFluid();
    }

    public Fluid getOutputFluid() {
        return this.getFluid();
    }

    public Fluid getInputFluid() {
        return this.getFluid();
    }


    public abstract void markChanged();

    protected final void defaultChanged() {
        this.be.setChanged();
    }

    public B getBe() {
        return be;
    }

    public void setBe(B be) {
        this.be = be;
    }

    public void setBlockEntity(BlockEntity be) {
        this.setBe((B) be);
    }

    public void clear() {
        this.be = null;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public WrappedMaidFakePlayer getFakePlayer() {
        return fakePlayer;
    }
}
