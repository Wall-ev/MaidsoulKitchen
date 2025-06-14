package com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec;

import com.github.wallev.maidsoulkitchen.foundation.utility.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

import java.util.List;
import java.util.Map;

public abstract class ToolRecSerializerManager<R extends Recipe<? extends Container>> extends RecSerializerManager<R> {
    protected ToolRecSerializerManager(RecipeType<R> recipeType) {
        super(recipeType);
    }

    @Override
    protected MKRecipe<R> createMKRecipe(R r) {
        List<RecIngredient> ingredients = recipeInfoProvider.getIngredients(this, r);
        ItemStack output = recipeInfoProvider.getOutput(this, r);
        ItemStack container = recipeInfoProvider.getContainer(this, r);
        RecIngredient tool = ((ToolRecipeInfoProvider<R>) recipeInfoProvider).getTool(this, r);
        return new MKRecipe<>(r, tool, ingredients, output, container);
    }

    @Override
    protected abstract ToolRecipeInfoProvider<R> createRecipeInfoProvider();

    @Override
    protected MaidRec recProcess(MKRecipe<R> r, Map<ItemDefinition, Long> available, List<Item> invIngredient, boolean[] single, Map<Item, ItemAmount> itemTimes) {
        ItemStack tool = processTool(r, available, invIngredient, single, itemTimes);
        if (tool.isEmpty()) {
            return MaidRec.EMPTY;
        }

        boolean processRecIngres = processRecIngres(r, available, invIngredient, single, itemTimes);
        if (!processRecIngres) {
            return MaidRec.EMPTY;
        }

        return createCookRec(r, tool, available, single, invIngredient, itemTimes);
    }

    protected ItemStack processTool(MKRecipe<R> r, Map<ItemDefinition, Long> available, List<Item> invIngredient, boolean[] single, Map<Item, ItemAmount> itemTimes) {
        RecIngredient tool = r.tool();
        if (tool.isEmpty()) {
            return ItemStack.EMPTY;
        }

        for (ItemDefinition item : available.keySet()) {
            if (tool.test(item.stack()) > 0) {
                return item.item().getDefaultInstance();
            }
        }
        return ItemStack.EMPTY;
    }

    public static abstract class ToolRecipeInfoProvider<R extends Recipe<? extends Container>> extends RecipeInfoProvider<R> {
        public abstract RecIngredient getTool(RecSerializerManager<R> rsm, R rec);
    }
}
