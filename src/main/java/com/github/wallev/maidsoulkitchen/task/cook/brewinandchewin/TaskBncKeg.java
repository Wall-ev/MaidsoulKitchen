package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin;

import com.brewinandchewin.common.block.entity.KegBlockEntity;
import com.brewinandchewin.common.crafting.KegRecipe;
import com.brewinandchewin.core.registry.BCBlocks;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.RegisterData;
import com.github.wallev.maidsoulkitchen.mixin.brewinandchewin.KegBlockEntityAccessor;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.TaskFdCiCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.*;


public class TaskBncKeg extends TaskFdCiCook<KegBlockEntity, KegRecipe> {
    @Override
    public ItemStackHandler getItemStackHandler(KegBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public Optional<KegRecipe> getMatchingRecipe(KegBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((KegBlockEntityAccessor) be).tlmk$getMatchingRecipe(recipeWrapper);
    }

    @Override
    public boolean canCook(KegBlockEntity be, KegRecipe recipe) {
        return ((KegBlockEntityAccessor) be).tlmk$canCook(recipe);
    }

    @Override
    public int getOutputSlot() {
        return KegBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 5;
    }

    @Override
    public ItemStackHandler getBeInv(KegBlockEntity kegBlockEntity) {
        return kegBlockEntity.getInventory();
    }

    @Override
    public int getMealStackSlot() {
        return KegBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return KegBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(KegBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public boolean isHeated(KegBlockEntity be) {
        return true;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof KegBlockEntity;
    }

    @Override
    public RecipeType<KegRecipe> getRecipeType() {
        return KegRecipe.TYPE;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BNC_KEY.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BCBlocks.KEG.get().asItem().getDefaultInstance();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.BNC_KEY;
    }

}
