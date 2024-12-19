package com.github.wallev.maidsoulkitchen.task.cook.v1.bakery;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.bakery.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.bakery.recipe.CookingPotRecipe;
import net.satisfy.bakery.registry.RecipeTypeRegistry;
import net.satisfy.bakery.registry.ObjectRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class TaskDbkCookingPot extends TaskLdContainerCook<SmallCookingPotBlockEntity, CookingPotRecipe> {
    @Override
    public boolean isHeated(SmallCookingPotBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputStartSlot() {
        return 1;
    }

    @Override
    public int getInputSize() {
        return 7;
    }

    @Override
    public Container getContainer(SmallCookingPotBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof SmallCookingPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DBK_COOKING_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.SMALL_COOKING_POT.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraEndRecipe(CookingPotRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
       return extraRecipe(recipe.getContainer().getItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DBK_COOKING_POT;
    }

    @Override
    public NonNullList<Ingredient> getIngredients(Recipe<?> recipe) {
//        CookingPotRecipe cookingPotRecipe = (CookingPotRecipe) recipe;
//        NonNullList<Ingredient> ingredients = cookingPotRecipe.getIngredients();
//        NonNullList<Ingredient> list = NonNullList.create();
//        list.add(Ingredient.merge(ingredients));
//        list.add(Ingredient.of(cookingPotRecipe.getContainer()));
        return super.getIngredients(recipe);
    }

    @Override
    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        CookingPotRecipe cookingPotRecipe = (CookingPotRecipe) recipe;
        List<Ingredient> ingres = this.getIngredients(recipe);
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingres);
        list.add(Ingredient.of(cookingPotRecipe.getContainer()));
        return ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(list, modeRandom, overSize));
    }
}
