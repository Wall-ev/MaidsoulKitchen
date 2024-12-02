package com.github.wallev.farmsoulkitchen.task.cook.v1.candlelight;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.candlelight.block.entity.CookingPotBlockEntity;
import net.satisfy.candlelight.recipe.CookingPotRecipe;
import net.satisfy.candlelight.registry.RecipeTypeRegistry;
import net.satisfy.candlelight.registry.ObjectRegistry;

import java.util.List;
import java.util.Map;


public class TaskDclCookingPot extends TaskLdContainerCook<CookingPotBlockEntity, CookingPotRecipe> {
    public static final TaskDclCookingPot INSTANCE = new TaskDclCookingPot();
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "dcl_cooking_pot");

    private TaskDclCookingPot() {
    }

    @Override
    public boolean isHeated(CookingPotBlockEntity be) {
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
    public Container getContainer(CookingPotBlockEntity be) {
        return be;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CookingPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return RecipeTypeRegistry.COOKING_POT_RECIPE_TYPE.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
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
    public static TaskDclCookingPot getInstance() {
        return INSTANCE;
    }

}
