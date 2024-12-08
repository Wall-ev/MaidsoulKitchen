package com.github.wallev.farmsoulkitchen.task.cook.v1.vinery;

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
import net.satisfy.vinery.block.entity.FermentationBarrelBlockEntity;
import net.satisfy.vinery.recipe.FermentationBarrelRecipe;
import net.satisfy.vinery.registry.ObjectRegistry;
import net.satisfy.vinery.registry.RecipeTypesRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TaskFermentationBarrel extends TaskLdContainerCook<FermentationBarrelBlockEntity, FermentationBarrelRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "dv_fermentation_barrel");

    @Override
    public boolean isHeated(FermentationBarrelBlockEntity be) {
        return true;
    }

    @Override
    public int getOutputSlot() {
        return 5;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public Container getContainer(FermentationBarrelBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof FermentationBarrelBlockEntity;
    }

    @Override
    public RecipeType<FermentationBarrelRecipe> getRecipeType() {
        return RecipeTypesRegistry.FERMENTATION_BARREL_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.FERMENTATION_BARREL.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraStartRecipe(FermentationBarrelRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
        // 别问我为什么这里是硬编码,葡园酒香这里就是硬编码...
       return extraRecipe(ObjectRegistry.WINE_BOTTLE.get(), recipe, available, single, canMake, itemTimes, invIngredient);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FERMENTATION_BARREL;
    }

    @Override
    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        List<Ingredient> ingres = this.getIngredients(recipe);
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingres);
        list.add(Ingredient.of(ObjectRegistry.WINE_BOTTLE.get()));
        return ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(list, modeRandom, overSize));
    }
}
