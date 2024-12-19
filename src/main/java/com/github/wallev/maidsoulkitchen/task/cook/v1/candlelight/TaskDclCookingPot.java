package com.github.wallev.maidsoulkitchen.task.cook.v1.candlelight;

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
import net.satisfy.candlelight.entity.LargeCookingPotBlockEntity;
import net.satisfy.candlelight.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.recipe.CookingPotRecipe;
import net.satisfy.farm_and_charm.registry.RecipeTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TaskDclCookingPot extends TaskLdContainerCook<LargeCookingPotBlockEntity, CookingPotRecipe> {
    @Override
    public boolean isHeated(LargeCookingPotBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public int getOutputSlot() {
        return 7;
    }

    @Override
    public int getInputSize() {
        return 7;
    }

    @Override
    public Container getContainer(LargeCookingPotBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof LargeCookingPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DCL_COOKING_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.COOKING_POT.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraEndRecipe(CookingPotRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
       return extraRecipe(recipe.getContainer().getItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DCL_COOKING_POT;
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
