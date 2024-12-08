package com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskLdContainerCook;
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
import net.satisfy.candlelight.block.entity.CookingPanBlockEntity;
import net.satisfy.candlelight.recipe.CookingPanRecipe;
import net.satisfy.candlelight.registry.ObjectRegistry;
import net.satisfy.candlelight.registry.RecipeTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TaskDclCookingPan extends TaskLdContainerCook<CookingPanBlockEntity, CookingPanRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "dcl_cooking_pan");

    @Override
    public boolean isHeated(CookingPanBlockEntity be) {
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
    public Container getContainer(CookingPanBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CookingPanBlockEntity;
    }

    @Override
    public RecipeType<CookingPanRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_PAN_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.COOKING_PAN.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraEndRecipe(CookingPanRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
       return extraRecipe(recipe.getContainer().getItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DCL_COOKING_PAN;
    }

    @Override
    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        CookingPanRecipe cookingPotRecipe = (CookingPanRecipe) recipe;
        List<Ingredient> ingres = this.getIngredients(recipe);
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingres);
        list.add(Ingredient.of(cookingPotRecipe.getContainer()));
        return ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(list, modeRandom, overSize));
    }
}
