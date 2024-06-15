package com.catbert.tlma.api.task.v1.cook;

import com.catbert.tlma.api.IFhCbeAccessor;
import com.catbert.tlma.api.task.v1.bestate.IBaseCookItemHandlerBe;
import com.catbert.tlma.api.task.v1.bestate.IHeatBe;
import com.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.util.ItemsUtil;
import com.mojang.datafixers.util.Pair;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.List;
import java.util.Optional;

import static com.catbert.tlma.TLMAddon.LOGGER;

public interface IFdPotCook<B extends BlockEntity, R extends Recipe<? extends Container>> extends IBaseCookItemHandlerBe<B, R>, IHeatBe<B>, IHandlerCookBe<B>, IItemHandlerCook {

    int getMealStackSlot();

    int getContainerStackSlot();

    ItemStack getFoodContainer(B blockEntity);

    default boolean maidShouldMoveTo(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        // 有最终物品
        LOGGER.info("outputStack: {} ", outputStack);
        if (!outputStack.isEmpty()) {
            return true;
        }

        ItemStack mealStack = inventory.getStackInSlot(getMealStackSlot());
        ItemStack container = getFoodContainer(blockEntity);
//        boolean hasContainerItem = maidRecipesManager.getMaidInventory().getInventoryItem().containsKey(container.getItem());
        int stackSlot = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(container.getItem()));
        // 有待取出物品和对应的容器
        LOGGER.info("mealStack: {} {} ", mealStack, stackSlot);
        if (!mealStack.isEmpty() && stackSlot > -1) {
            return true;
        }

        boolean heated = isHeated(blockEntity);
        Optional<R> recipe = getMatchingRecipe(blockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(blockEntity, recipe.get());
        List<Pair<List<Integer>, List<List<ItemStack>>>> recipesIngredients = maidRecipesManager.getRecipesIngredients();
        LOGGER.info("recipe: {} {} {} ", recipe, b, recipesIngredients);
        if (!b && !recipesIngredients.isEmpty() && heated && mealStack.isEmpty()) {
            return true;
        }

        // 能做饭现在和有输入（也就是厨锅现在有物品再里面但是不符合配方
        LOGGER.info("hasInput: {} {}", b, hasInput(inventory));
        if (!b && hasInput(inventory)) {
            return true;
        }

        ItemStack containerInputStack = inventory.getStackInSlot(getContainerStackSlot());
        LOGGER.info("containerInputStack: {} {}", hasInput(inventory), containerInputStack);
        //当厨锅没有物品，又有杯具在时，就取出杯具
        if (!hasInput(inventory) && !containerInputStack.isEmpty()) {
            return true;
        }

        return false;
    }

    default void maidCookMake(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        LOGGER.info("MaidCookMakeTask.processCookMake：");
        LOGGER.info("maidRecipesManager: {} ", maidRecipesManager);
        LOGGER.info("getRecipesIngredients: {} ", maidRecipesManager.getRecipesIngredients());

        tryExtractItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);

        tryInsertItem(serverLevel, entityMaid, blockEntity, maidRecipesManager);
    }

    default void tryInsertItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        ItemStack mealStack = inventory.getStackInSlot(getMealStackSlot());
        Pair<List<Integer>, List<List<ItemStack>>> recipeIngredient = maidRecipesManager.getRecipeIngredient();
        if (!mealStack.isEmpty() || recipeIngredient == null) return;

        insertInputStack(inventory, availableInv, blockEntity, recipeIngredient);

        pickupAction(entityMaid);

    }

    default void tryExtractItem(ServerLevel serverLevel, EntityMaid entityMaid, B blockEntity, MaidRecipesManager<R> maidRecipesManager) {
        ItemStackHandler inventory = getItemStackHandler(blockEntity);
        CombinedInvWrapper availableInv = entityMaid.getAvailableInv(true);

        ItemStack mealStack = inventory.getStackInSlot(getMealStackSlot());
        ItemStack containerInputStack = inventory.getStackInSlot(getContainerStackSlot());

        ItemStack outputStack = inventory.getStackInSlot(getOutputSlot());
        ItemStack container = getFoodContainer(blockEntity);

//        ItemStack maidContainerStack = maidRecipesManager.getItemStack(container.getItem());

        int maidContainerStackIndex = ItemsUtil.findStackSlot(availableInv, stack -> stack.is(container.getItem()));

        // 取出杯具（相当于盛饭需要碗，但是此时你手上有被子；所以需要先取出杯子，再把碗放到你手上）
        if (!mealStack.isEmpty() && maidContainerStackIndex > -1) {
            // 取出杯具
            if (!containerInputStack.isEmpty()) {
                inventory.extractItem(getContainerStackSlot(), containerInputStack.getCount(), false);
                ItemHandlerHelper.insertItemStacked(availableInv, containerInputStack.copy(), false);
                blockEntity.setChanged();
            }

            // 放入杯具
            ItemStack stack = availableInv.getStackInSlot(maidContainerStackIndex);
            availableInv.extractItem(maidContainerStackIndex, stack.getCount(), false);
            inventory.insertItem(getContainerStackSlot(), stack.copy(), false);
            blockEntity.setChanged();
        }


        // 取出最终物品
        extractOutputStack(inventory, availableInv, blockEntity);


        boolean heated = isHeated(blockEntity);
        Optional<R> recipe = getMatchingRecipe(blockEntity, new RecipeWrapper(inventory));
        // 现在是否可以做饭（厨锅有没有正在做饭）
        boolean b = recipe.isPresent() && canCook(blockEntity, recipe.get());
        if (!b && hasInput(inventory)) {
            extractInputStack(inventory, availableInv, blockEntity);
        }


        //当厨锅没有物品，又有杯具在时，就取出杯具
        if (!hasInput(inventory) && !containerInputStack.isEmpty()) {
            inventory.extractItem(getContainerStackSlot(), containerInputStack.getCount(), false);
            ItemHandlerHelper.insertItemStacked(availableInv, containerInputStack.copy(), false);
            blockEntity.setChanged();
        }


        pickupAction(entityMaid);
    }

    @Override
    @SuppressWarnings("unchecked")
    default Optional<R> getMatchingRecipe(B be, RecipeWrapper recipeWrapper) {
        return ((IFhCbeAccessor<R>) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    default boolean canCook(B be, R recipe) {
        return ((IFhCbeAccessor<R>) be).canCook$tlma(recipe);
    }
}
