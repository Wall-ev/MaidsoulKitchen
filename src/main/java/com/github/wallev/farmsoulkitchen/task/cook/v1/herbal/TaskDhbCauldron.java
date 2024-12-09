package com.github.wallev.farmsoulkitchen.task.cook.v1.herbal;

import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.inventory.tooltip.AmountTooltip;
import com.github.wallev.farmsoulkitchen.task.TaskInfo;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfy.herbalbrews.blocks.entity.CauldronBlockEntity;
import satisfy.herbalbrews.recipe.CauldronRecipe;
import satisfy.herbalbrews.registry.ObjectRegistry;
import satisfy.herbalbrews.registry.RecipeTypeRegistry;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public class TaskDhbCauldron extends TaskLdContainerCook<CauldronBlockEntity, CauldronRecipe> {
    @Override
    public boolean isHeated(CauldronBlockEntity be) {
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
    public Container getContainer(CauldronBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CauldronBlockEntity;
    }

    @Override
    public RecipeType<CauldronRecipe> getRecipeType() {
        return RecipeTypeRegistry.CAULDRON_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DHB_CAULDRON.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.CAULDRON.get().asItem().getDefaultInstance();
    }

    @Override
    protected boolean tExtraStartRecipe(CauldronRecipe recipe, Map<Item, Integer> available, boolean[] single, boolean[] canMake, Map<Item, Integer> itemTimes, List<Item> invIngredient) {
       return extraRecipe(Items.GLASS_BOTTLE.asItem(), recipe, available, single, canMake, itemTimes, invIngredient);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.DHB_CAULDRON;
    }

    @Override
    public Optional<TooltipComponent> getRecClientAmountTooltip(Recipe<?> recipe, boolean modeRandom, boolean overSize) {
        List<Ingredient> ingres = this.getIngredients(recipe);
        NonNullList<Ingredient> list = NonNullList.create();
        list.addAll(ingres);
        list.add(Ingredient.of(Items.GLASS_BOTTLE.asItem()));
        return ingres.isEmpty() ? Optional.empty() : Optional.of(new AmountTooltip(list, modeRandom, overSize));
    }
}
