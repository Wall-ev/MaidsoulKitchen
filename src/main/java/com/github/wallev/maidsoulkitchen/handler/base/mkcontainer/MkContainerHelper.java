package com.github.wallev.maidsoulkitchen.handler.base.mkcontainer;

import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import java.util.List;

public class MkContainerHelper {
    /**
     * 从厨具的输出格子中提取出烹饪好的食物
     */
    public static <MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> void extractOutputStack(MCB maidCookBe) {
        int resultSlot = maidCookBe.getResultSlot();
        ItemStack stackInSlot = maidCookBe.getStackInSlot(resultSlot);
        ItemStack copy = stackInSlot.copy();

        if (stackInSlot.isEmpty()) return;
        IItemHandlerModifiable outputInv = maidCookBe.getOutputInv();
        ItemStack leftStack = ItemHandlerHelper.insertItemStacked(outputInv, copy, false);
        maidCookBe.extractItem(resultSlot, stackInSlot.getCount() - leftStack.getCount(), false);
        maidCookBe.setChanged();
    }

    /**
     * 从厨具的输入格子中提取出原料
     */
    public static <MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> void extractInputsStack(MCB maidCookBe) {
        int inputStartSlot = maidCookBe.getInputStartSlot();
        int inputSlotSize = maidCookBe.getInputSlotSize();
        IItemHandlerModifiable ingredientInv = maidCookBe.getIngredientInv();

        for (int i = inputStartSlot; i < inputStartSlot + inputSlotSize; ++i) {
            ItemStack stackInSlot = maidCookBe.getStackInSlot(i);
            ItemStack copy = stackInSlot.copy();
            if (!stackInSlot.isEmpty()) {
                ItemStack leftStack = ItemHandlerHelper.insertItemStacked(ingredientInv, copy, false);
                maidCookBe.extractItem(i, stackInSlot.getCount() - leftStack.getCount(), false);

            }
        }
        maidCookBe.setChanged();
    }

    /**
     * 将原料放入厨具的输入格子中
     *
     * @param ingredientPair 原料
     */
    public static <MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> void insertInputsStack(MCB maidCookBe, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair, int inputStartSlot) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = inputStartSlot, j = 0; i < ingredients.size() + inputStartSlot; i++, j++) {
                insertAndShrink(maidCookBe, amounts.get(j), ingredients, j, i);
            }
            maidCookBe.setChanged();
        }
    }

    /**
     * 将原料放入厨具的输出格子中
     *
     * @param amount          数量
     * @param ingredient      原料
     * @param ingredientIndex 原料索引
     * @param slotIndex       厨具格子索引
     */
    public static <MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> void insertAndShrink(MCB maidCookBe, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            if (itemStack.isEmpty()) continue;
            int count = itemStack.getCount();

            if (count >= amount) {
                ItemStack leftStack = maidCookBe.insertItem(slotIndex, itemStack.copyWithCount(amount), false);
                itemStack.shrink(amount - leftStack.getCount());
                break;
            } else {
                ItemStack leftStack = maidCookBe.insertItem(slotIndex, itemStack.copyWithCount(count), false);
                itemStack.shrink(count - leftStack.getCount());
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
     * @return 是否有原料
     */
    public static <MCB extends AbstractMaidCookBe<B, R>, B extends BlockEntity, R extends Recipe<? extends Container>> boolean hasInputs(MCB maidCookBe, int inputSlotSize, int inputStartSlot) {
        for (int i = inputStartSlot; i <inputStartSlot + inputSlotSize; i++) {
            if (!maidCookBe.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }






    // ----------------------------------- 一系列的ItemHandler厨具交互方法 --------------------------------------------//

    /**
     * 从厨具的输出格子中提取出烹饪好的食物
     *
     * @param cookBeInv 厨具Inv
     * @param ingreInv  原料Inv
     * @param cookBe    厨具
     */
    public static void extractOutputStack(ItemStackHandler cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, int resultSlot) {
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
    public static void extractInputsStack(ItemStackHandler cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, int inputStartSlot, int inputSlotSize) {
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
    public static void insertInputsStack(ItemStackHandler cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair, int inputStartSlot) {
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
    public static void insertAndShrink(ItemStackHandler cookBeInv, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
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
    public static boolean hasInputs(ItemStackHandler cookBeInv, int inputSlotSize, int inputStartSlot) {
        for (int i = inputStartSlot; i < inputSlotSize + inputStartSlot; i++) {
            if (!cookBeInv.getStackInSlot(i).isEmpty()) {
                return true;
            }
        }

        return false;
    }
    // -----------------------------------------------------------------------------------------------//


    // ----------------------------------- 一系列的Container厨具交互方法 --------------------------------------------//

    /**
     * 从厨具的输出格子中提取出烹饪好的食物
     *
     * @param cookBeInv 厨具Inv
     * @param ingreInv  原料Inv
     * @param cookBe    厨具
     */
    public static void extractOutputStack(Container cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, int resultSlot) {
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
    public static void extractInputStack(Container cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, int inputStartSlot, int inputSlotSize) {
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
    public static void insertInputStack(Container cookBeInv, IItemHandlerModifiable ingreInv, BlockEntity cookBe, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair, int inputStartSlot) {
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
    public static void insertAndShrink(Container cookBeInv, Integer amount, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
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
    public static boolean hasInput(Container cookBeInv, int inputStartSlot, int inputSlotSize) {
        for (int i = inputStartSlot; i < inputSlotSize + inputStartSlot; i++) {
            if (!cookBeInv.getItem(i).isEmpty()) {
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
    public static boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
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
    // -----------------------------------------------------------------------------------------------//
}
