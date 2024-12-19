package com.github.wallev.maidsoulkitchen.task.cook.v1.farmacharm;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskLdContainerCook;
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
import net.satisfy.farm_and_charm.block.entity.RoasterBlockEntity;
import net.satisfy.farm_and_charm.recipe.RoasterRecipe;
import net.satisfy.farm_and_charm.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.registry.RecipeTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TaskDfcRoast extends TaskLdContainerCook<RoasterBlockEntity, RoasterRecipe> {
    @Override
    public boolean isHeated(RoasterBlockEntity be) {
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
    public Container getContainer(RoasterBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof RoasterBlockEntity;
    }

    @Override
    public RecipeType<RoasterRecipe> getRecipeType() {
        return RecipeTypeRegistry.ROASTER_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DFC_ROAST.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.ROASTER.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraEndRecipe(RoasterRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
       return extraRecipe(recipe.getContainer().getItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DFC_ROAST;
    }

    @Override
    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        RoasterRecipe cookingPotRecipe = (RoasterRecipe) recipe;
        List<Ingredient> ingres = this.getIngredients(recipe);
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingres);
        list.add(Ingredient.of(cookingPotRecipe.getContainer()));
        return ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(list, modeRandom, overSize));
    }
}
