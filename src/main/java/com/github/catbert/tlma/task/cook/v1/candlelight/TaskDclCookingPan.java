package com.github.catbert.tlma.task.cook.v1.candlelight;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.init.registry.tlm.RegisterData;
import com.github.catbert.tlma.task.cook.v1.common.TaskLdContainerCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.candlelight.block.entity.CookingPanBlockEntity;
import net.satisfy.candlelight.recipe.CookingPanRecipe;
import net.satisfy.candlelight.registry.ObjectRegistry;
import net.satisfy.candlelight.registry.RecipeTypeRegistry;

import java.util.List;
import java.util.Map;


public class TaskDclCookingPan extends TaskLdContainerCook<CookingPanBlockEntity, CookingPanRecipe> {
    public static final TaskDclCookingPan INSTANCE = new TaskDclCookingPan();
    public static final ResourceLocation UID = new ResourceLocation(TLMAddon.MOD_ID, "dcl_cooking_pan");

    private TaskDclCookingPan() {
    }

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

    public static TaskDclCookingPan getInstance() {
        return INSTANCE;
    }

}
