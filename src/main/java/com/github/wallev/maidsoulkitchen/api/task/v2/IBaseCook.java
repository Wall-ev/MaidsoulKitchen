package com.github.wallev.maidsoulkitchen.api.task.v2;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.cbaccessor.IFdCbeAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.action.IMaidAction;
import com.github.wallev.maidsoulkitchen.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.mojang.datafixers.util.Pair;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

//public interface IBaseCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends ICookBaseBe<B, R>, IMaidAction {
public interface IBaseCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IMaidAction {

    /**
     * 获取最终物品的格子
     */
    int getOutputSlot();

    /**
     * 获取基本原料的起始格子
     */
    default int getInputStartSlot() {
        return 0;
    }

    /**
     * 获取基本原料的总格子数
     * 注意：不包括额外原料啥的格子
     * 比如：和啤酒啦的啤酒杯、葡园酒香的葡萄酒瓶、饮酒作乐的...
     */
    int getInputSize();

    /**
     * 女仆移动至厨具的条件:
     * 厨具内最可以去除最终物品
     */
    default <T extends IBeInv<B>> boolean outputCanMoveTo(T inventoryHandler, EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
        return !getStackInBeSlot(be, getOutputSlot()).isEmpty();
    }

    default <T extends IBeInv<B>> boolean outputCanMoveTo(EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
        return !getStackInBeSlot(be, getOutputSlot()).isEmpty();
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内的原料不符很配方和女仆身上有对应配方的原料
     */
    default <T extends IBeInv<B>> boolean beCookCanMoveTo(T inventoryHandler, EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
        return !beInnerCanCook(be, inventoryHandler) && !maidRecManager.getRecipesIngredients().isEmpty();
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内的原料不符很配方和女仆身上有对应配方的原料
     */
    default boolean beCookCanMoveTo(boolean innerBeCanCook, EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
        return !innerBeCanCook && !maidRecManager.getRecipesIngredients().isEmpty();
    }

//    default boolean beCookCanMoveTo(EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
//        return !beInnerCanCook(be, inventoryHandler) && !maidRecManager.getRecipesIngredients().isEmpty();
//    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内有原料但不符合配方
     */
    default <T extends IBeInv<B>> boolean beInputCanMoveTo(T inventoryHandler, EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
        return hasInput(be) && !beInnerCanCook(be, inventoryHandler);
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内有原料但不符合配方
     */
    default boolean beInputCanMoveTo(boolean innerBeCanCook, EntityMaid maid, B be, MaidRecipesManager<R> maidRecManager){
        return hasInput(be) && !innerBeCanCook;
    }

    /**
     * 女仆移动至厨具的条件:
     * 厨具内的原料是否符合配方，一盘通过mixin获得...
     */
    @SuppressWarnings("unchecked")
    default <T extends IBeInv<B>> boolean beInnerCanCook(B be, T inventoryHandler) {
        RecipeWrapper recipeWrapper = new RecipeWrapper((IItemHandlerModifiable) inventoryHandler);
        Optional<R> matchingRecipe = ((IFdCbeAccessor<R>) be).getMatchingRecipe$tlma(recipeWrapper);
        return matchingRecipe.isPresent() && ((IFdCbeAccessor<R>) be).canCook$tlma(matchingRecipe.get());
    }

//    @SuppressWarnings("unchecked")
//    default <T extends IBeInv<B>> boolean beInnerItemHandlerCanCook(B be) {
//        RecipeWrapper recipeWrapper = new RecipeWrapper((IItemHandlerModifiable) inventoryHandler);
//        Optional<R> matchingRecipe = ((ICbeAccessor<R>) be).getMatchingRecipe$tlma(recipeWrapper);
//        return matchingRecipe.isPresent() && ((ICbeAccessor<R>) be).canCook$tlma(matchingRecipe.get());
//    }
//
//    @SuppressWarnings("unchecked")
//    default <T extends IBeInv<B>> boolean beInnerContainerCanCook(B be) {
//        RecipeWrapper recipeWrapper = new RecipeWrapper((IItemHandlerModifiable) inventoryHandler);
//        Optional<R> matchingRecipe = ((ICbeAccessor<R>) be).getMatchingRecipe$tlma(recipeWrapper);
//        return matchingRecipe.isPresent() && ((ICbeAccessor<R>) be).canCook$tlma(matchingRecipe.get());
//    }


    /**
     * 女仆操作厨具:
     * 取出最终物品
     */
    default <T extends IBeInv<B>> void extractOutputMake(T inventoryHandler, EntityMaid maid, CombinedInvWrapper availableInv, B be, MaidRecipesManager<R> maidRecManager) {
        if (outputCanMoveTo(inventoryHandler, maid, be, maidRecManager)) {
            extractOutputStack(availableInv, be);
        }
    }

    /**
     * 女仆操作厨具:
     * 厨具内份原料不符合配方时，取出原料
     * 身上有符合配方的原料时，放入原料
     */
    default <T extends IBeInv<B>> void extractAndInsertInputMake(T inventoryHandler, EntityMaid maid, CombinedInvWrapper availableInv, B be, MaidRecipesManager<R> maidRecManager) {
        if (!beInputCanMoveTo(inventoryHandler, maid, be, maidRecManager)) return;
        extractInputsStack(availableInv, be);

        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecManager.getRecipeIngredient();
        if (recipeIngredient.getFirst().isEmpty()) return;
        insertInputsStack(availableInv, be, recipeIngredient);
    }


    /**
     * 取出最终物品
     */
    default void extractOutputStack(CombinedInvWrapper availableInv, B be) {
        ItemStack stackInSlot = getStackInBeSlot(be, this.getOutputSlot());
//        ItemStack stackInSlot = inventory.getStackInSlot(this.getOutputSlot());

        if (stackInSlot.isEmpty()) return;
        extractAndInsertAction(be, availableInv, stackInSlot.copy(), this.getOutputSlot());
//        extractStack2BeAction(be, this.getOutputSlot(), stackInSlot.getCount());
//        inventory.extractItem(this.getOutputSlot(), stackInSlot.getCount(), false);
//        ItemHandlerHelper.insertItemStacked(availableInv, stackInSlot.copy(), false);
        be.setChanged();
    }


    /**
     * 取出厨具内的基本原料
     */
    default void extractInputsStack(CombinedInvWrapper availableInv, B be) {
        for (int i = this.getInputStartSlot(); i < this.getInputSize() + this.getInputStartSlot(); ++i) {
            ItemStack stackInSlot = getStackInBeSlot(be, i);
            if (!stackInSlot.isEmpty()) {
                extractAndInsertAction(be, availableInv, stackInSlot.copy(), i);
//                extractStack2BeAction(be, i, stackInSlot.getCount());
//                inventory.extractItem(i, stackInSlot.getCount(), false);
//                ItemHandlerHelper.insertItemStacked(availableInv, stackInSlot.copy(), false);
            }
        }
        be.setChanged();
    }

    /**
     * 放入基本原料
     */
    default void insertInputsStack(CombinedInvWrapper availableInv, B be, Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {
        List<Integer> amounts = ingredientPair.getFirst();
        List<List<ItemStack>> ingredients = ingredientPair.getSecond();

        if (hasEnoughIngredient(amounts, ingredients)) {
            for (int i = getInputStartSlot(), j = 0; i < ingredients.size() + getInputStartSlot(); i++, j++) {
                insertAndShrink(be, amounts, ingredients, j, i);
            }
            be.setChanged();
        }

        updateIngredient(ingredientPair);
    }

    default void updateIngredients(List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients) {

    }

    default void updateIngredient(Pair<List<Integer>, List<List<ItemStack>>> ingredientPair) {

    }

    default boolean hasEnoughIngredient(List<Integer> amounts, List<List<ItemStack>> ingredients) {
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

    default void insertAndShrink(B be, List<Integer> amounts, List<List<ItemStack>> ingredient, int ingredientIndex, int slotIndex) {
        int i = 0;
        for (ItemStack itemStack : ingredient.get(ingredientIndex)) {
            int shinkNum = amounts.get(i++);
            int count = itemStack.getCount();

            if (count >= shinkNum) {
                insertStack2BeAction(be, itemStack.copyWithCount(shinkNum), slotIndex);
//                inventory.insertItem(slotIndex, itemStack.copyWithCount(shinkNum), false);
                itemStack.shrink(shinkNum);
                break;
            } else {
                insertStack2BeAction(be, itemStack.copyWithCount(count), slotIndex);
//                inventory.insertItem(slotIndex, itemStack.copyWithCount(count), false);
                itemStack.shrink(count);
                shinkNum -= count;
                if (shinkNum <= 0) {
                    break;
                }
            }
        }
    }

    default boolean hasInput(B be) {
        for (int i = getInputStartSlot(); i < getInputSize() + getInputStartSlot(); i++) {
            if (!getStackInBeSlot(be, i).isEmpty()) {
                return true;
            }
        }

        return false;
    }

    default void insertStack2MaidAction(CombinedInvWrapper availableInv, ItemStack insertStack){
        ItemHandlerHelper.insertItemStacked(availableInv, insertStack, false);
    }

    /**
     * 获取厨具内对应格子的物品
     */
    ItemStack getStackInBeSlot(B be, int slot);

    /**
     * 用于取出厨具内的物品并放入女仆的背包
     */
    default void extractAndInsertAction(B be, CombinedInvWrapper availableInv, ItemStack insertStack, int extractSlot){
        extractStack2BeAction(be, extractSlot, insertStack.getCount());
        insertStack2MaidAction(availableInv, insertStack);
    }

    /**
     * 移出厨具内的物品
     */
    void extractStack2BeAction(B be, int extractSlot, int count);

    /**
     * 将物品放入厨具内
     */
    void insertStack2BeAction(B be, ItemStack insertStack, int insertSlot);
}
