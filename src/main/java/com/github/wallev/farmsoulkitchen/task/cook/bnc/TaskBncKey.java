package com.github.wallev.farmsoulkitchen.task.cook.bnc;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.mixin.bnc.KegBlockEntityAccessor;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskFdCiCook;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCBlocks;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

import java.util.*;


public class TaskBncKey extends TaskFdCiCook<KegBlockEntity, KegRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "bnc_key");

    @Override
    public ItemStackHandler getItemStackHandler(KegBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public Optional<KegRecipe> getMatchingRecipe(KegBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((KegBlockEntityAccessor) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    public boolean canCook(KegBlockEntity be, KegRecipe recipe) {
        return ((KegBlockEntityAccessor) be).canCook$tlma(recipe, be.getLevel());
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
        return KegBlockEntity.DRINK_DISPLAY_SLOT;
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
        return BCRecipeTypes.FERMENTING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
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
