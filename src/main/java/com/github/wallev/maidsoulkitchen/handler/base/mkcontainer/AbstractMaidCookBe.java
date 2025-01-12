package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

/**
 * 用于建立女仆烹饪厨具，便于管理
 * 应该在createBrain的时候就
 */
public abstract class AbstractMaidCookBe<B extends BlockEntity, R extends Recipe<? extends Container>> {
    protected final EntityMaid maid;
    protected final MaidRecipesManager<R> recipesManager;
    protected B cookBe;
    // @final
    protected int inputSlotSize;
    // @final
    protected int inputStartSlot;
    // @final
    protected int resultSlot;

    public AbstractMaidCookBe(EntityMaid maid, MaidRecipesManager<R> recipesManager, B cookBe) {
        this.maid = maid;
        this.recipesManager = recipesManager;
        this.cookBe = cookBe;
        this.initialSlots();
    }

    /**
     * 初始化厨具的格子信息（比如原料输入的格子，起始格子，输出的格子）
     */
    protected abstract void initialSlots();

    /**
     * 从厨具取出某个格子的物品
     *
     * @param slot     格子
     * @param amount   取出的数量
     * @param simulate 是否模拟取出
     * @return 取出后剩余的物品
     */
    public abstract ItemStack extractItem(int slot, int amount, boolean simulate);

    /**
     * 将物品插入到厨具的某个格子中
     *
     * @param slot     格子
     * @param stack    要插入的物品
     * @param simulate 是否模拟插入
     * @return 插入后剩余的物品
     */
    public abstract ItemStack insertItem(int slot, ItemStack stack, boolean simulate);

    /**
     * 获取厨具的某个格子里的ItemStack
     *
     * @param slot 格子
     * @return 格子里的ItemStack
     */
    public abstract ItemStack getStackInSlot(int slot);

    /**
     * 对于方块实体，确保包含方块实体的区块稍后保存到磁盘
     * <br>游戏不会认为它没有更改并跳过它。
     */
    public void setChanged() {
        this.cookBe.setChanged();
    }


    /**
     * 厨具内部的所有原料输入的格子是否有物资在里头
     */
    public boolean hasInputs() {
        for (int i = inputStartSlot; i < inputStartSlot + inputSlotSize; i++) {
            if (!this.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    /**
     * 厨具内部的输出（完全烹饪好的食物）的格子是否有物资在里头
     */
    public boolean hasResult() {
        return !this.getStackInSlot(resultSlot).isEmpty();
    }

    /**
     * 厨具内部的物资是否能够烹饪（即物资是否符合某个配方的原材料）
     * <br>不包括外部条件和内部条件（比如要加水，加燃料等；厨具的下面的方块提供温度）
     */
    public abstract void innerCanCook();


    // ----------------------------------- 一系列的Container厨具交互方法 --------------------------------------------//

    /**
     * 从厨具的输出格子中提取出烹饪好的食物
     *
     * @param cookBeInv 厨具Inv
     * @param ingreInv  原料Inv
     * @param cookBe    厨具
     */
    protected void extractOutputStack(Container cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe) {
        ItemStack stackInSlot = cookBeInv.getItem(resultSlot);
        ItemStack copy = stackInSlot.copy();

        if (stackInSlot.isEmpty()) return;
        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(ingreInv, copy, false);
        cookBeInv.removeItem(resultSlot, stackInSlot.getCount() - leftStack.getCount());
        cookBe.setChanged();
    }

    /**
     * 从厨具的输入格子中提取出原料
     *
     * @param cookBeInv 厨具Inv
     * @param ingreInv  原料Inv
     * @param cookBe    厨具
     */
    protected void extractInputStack(Container cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe) {
        for (int i = inputStartSlot; i < inputSlotSize + inputStartSlot; ++i) {
            ItemStack stackInSlot = cookBeInv.getItem(i);
            ItemStack copy = stackInSlot.copy();
            if (!stackInSlot.isEmpty()) {
                ItemStack leftStack = ItemHandlerHelper.insertItemStacked(ingreInv, copy, false);
                cookBeInv.removeItem(i, stackInSlot.getCount() - leftStack.getCount());
            }
        }
        cookBe.setChanged();
    }


    /**
     * 将原料放入厨具的输入格子中
     *
     * @param cookBeInv      厨具Inv
     * @param ingreInv       原料Inv
     * @param cookBe         厨具
     * @param ingredientPair 原料
     */
    protected void insertInputStack(Container cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = inputStartSlot, j = 0; i < ingredients.size() + inputStartSlot; i++, j++) {
                insertAndShrink(cookBeInv, amounts.get(j), ingredients, j, i);
            }
            cookBe.setChanged();
        }
    }

    /**
     * 将原料放入厨具的输出格子中
     *
     * @param cookBeInv       厨具Inv
     * @param amount          数量
     * @param ingredient      原料
     * @param ingredientIndex 原料索引
     * @param slotIndex       厨具格子索引
     */
    protected void insertAndShrink(Container cookBeInv, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            if (itemStack.isEmpty()) continue;
            int count = itemStack.getCount();
            if (count >= amount) {
                int slotStackCount = cookBeInv.getItem(slotIndex).getCount();
                cookBeInv.setItem(slotIndex, itemStack.copyWithCount(amount + slotStackCount));
                itemStack.shrink(amount);
                break;
            } else {
                int slotStackCount = cookBeInv.getItem(slotIndex).getCount();
                cookBeInv.setItem(slotIndex, itemStack.copyWithCount(count + slotStackCount));
                itemStack.shrink(count);
                amount -= count;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }

    /**
     * 检查厨具是否有原料
     *
     * @param cookBeInv 厨具Inv
     * @return 是否有原料
     */
    protected boolean hasInput(Container cookBeInv) {
        for (int i = inputStartSlot; i < inputSlotSize + inputStartSlot; i++) {
            if (!cookBeInv.getItem(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }
    // -----------------------------------------------------------------------------------------------//


    // ----------------------------------- 一系列的ItemHandler厨具交互方法 --------------------------------------------//

    /**
     * 从厨具的输出格子中提取出烹饪好的食物
     *
     * @param cookBeInv 厨具Inv
     * @param ingreInv  原料Inv
     * @param cookBe    厨具
     */
    protected void extractOutputStack(ItemStackHandler cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe) {
        ItemStack stackInSlot = cookBeInv.getStackInSlot(resultSlot);
        ItemStack copy = stackInSlot.copy();

        if (stackInSlot.isEmpty()) return;
        ItemStack insertedStack = ItemHandlerHelper.insertItemStacked(ingreInv, copy, false);
        cookBeInv.extractItem(resultSlot, stackInSlot.getCount() - insertedStack.getCount(), false);
        cookBe.setChanged();
    }

    /**
     * 从厨具的输入格子中提取出原料
     *
     * @param cookBeInv 厨具Inv
     * @param ingreInv  原料Inv
     * @param cookBe    厨具
     */
    protected void extractInputStack(ItemStackHandler cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe) {
        for (int i = inputStartSlot; i < inputSlotSize + inputStartSlot; ++i) {
            ItemStack stackInSlot = cookBeInv.getStackInSlot(i);
            ItemStack copy = stackInSlot.copy();
            if (!stackInSlot.isEmpty()) {
                ItemStack leftStack = ItemHandlerHelper.insertItemStacked(ingreInv, copy, false);
                cookBeInv.extractItem(i, stackInSlot.getCount() - leftStack.getCount(), false);

            }
        }
        cookBe.setChanged();
    }

    /**
     * 将原料放入厨具的输入格子中
     *
     * @param cookBeInv      厨具Inv
     * @param ingreInv       原料Inv
     * @param cookBe         厨具
     * @param ingredientPair 原料
     */
    protected void insertInputsStack(ItemStackHandler cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = inputStartSlot, j = 0; i < ingredients.size() + inputStartSlot; i++, j++) {
                insertAndShrink(cookBeInv, amounts.get(j), ingredients, j, i);
            }
            cookBe.setChanged();
        }
    }

    /**
     * 将原料放入厨具的输出格子中
     *
     * @param cookBeInv       厨具Inv
     * @param amount          数量
     * @param ingredient      原料
     * @param ingredientIndex 原料索引
     * @param slotIndex       厨具格子索引
     */
    protected void insertAndShrink(ItemStackHandler cookBeInv, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            if (itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= amount) {
                ItemStack leftInsertedStack = cookBeInv.insertItem(slotIndex, itemStack.copyWithCount(amount), false);
                itemStack.shrink(amount - leftInsertedStack.getCount());
                break;
            } else {
                ItemStack leftInsertedStack = cookBeInv.insertItem(slotIndex, itemStack.copyWithCount(count), false);
                itemStack.shrink(count - leftInsertedStack.getCount());
                amount -= count;
                if (amount <= 0) {
                    break;
                }
            }
        }
    }

    /**
     * 检查厨具是否有原料
     *
     * @param cookBeInv 厨具Inv
     * @return 是否有原料
     */
    protected boolean hasInput(ItemStackHandler cookBeInv) {
        for (int i = inputStartSlot; i < inputSlotSize + inputStartSlot; i++) {
            if (!cookBeInv.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }
    // -----------------------------------------------------------------------------------------------//


    // ----------------------------------- Common --------------------------------------------//

    /**
     * 检查原料Inv里的原料是否足够
     *
     * @param amounts     原料数量
     * @param ingredients 原料
     * @return 是否足够
     */
    protected boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
        boolean canInsert = true;

        int i = 0;
        for (List<ItemStack> ingredient : ingredients) {
            int actualCount = amounts.get(i++);
            for (ItemStack itemStack : ingredient) {
                actualCount -= itemStack.getCount();
                if (actualCount <= 0) {
                    break;
                }
            }

            if (actualCount > 0) {
                canInsert = false;
                break;
            }
        }

        return canInsert;
    }

    /**
     * 标志changed
     */
    protected void makeChanged() {
        this.cookBe.setChanged();
        this.recipesManager.setChanged();
    }
    // -----------------------------------------------------------------------------------------------//

    /**
     * 获取maid
     */
    public EntityMaid getMaid() {
        return maid;
    }

    /**
     * 获取配方管理器
     */
    public MaidRecipesManager<R> getRecipesManager() {
        return recipesManager;
    }

    /**
     * 烹饪厨具
     */
    public B getCookBe() {
        return cookBe;
    }

    /**
     * 获取输入槽大小
     */
    public int getInputSlotSize() {
        return inputSlotSize;
    }

    /**
     * 获取输入槽起始位置
     */
    public int getInputStartSlot() {
        return inputStartSlot;
    }

    /**
     * 获取结果槽
     */
    public int getResultSlot() {
        return resultSlot;
    }

    /**
     * 设置烹饪的厨具
     * <br>应该在每次执行逻辑的时候设置一次，以更新为该厨具
     */
    public void setCookBe(B cookBe) {
        this.cookBe = cookBe;
    }
}
