package com.github.catbert.tlma.task.cook.v1.kitcarrot;

import com.github.catbert.tlma.entity.data.inner.task.CookData;
import com.github.catbert.tlma.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;

public class TaskAirCompressor extends TaskFdPot<AirCompressorBlockEntity, AirCompressorRecipe> {
    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return null;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof AirCompressorBlockEntity;
    }

    @Override
    public RecipeType<AirCompressorRecipe> getRecipeType() {
        return null;
    }

    @Override
    public int getMealStackSlot() {
        return 0;
    }

    @Override
    public int getContainerStackSlot() {
        return 0;
    }

    @Override
    public ItemStack getFoodContainer(AirCompressorBlockEntity blockEntity) {
        return null;
    }

    @Override
    public ItemStackHandler getItemStackHandler(AirCompressorBlockEntity be) {
        return null;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 0;
    }

    @Override
    public boolean isHeated(AirCompressorBlockEntity be) {
        return false;
    }

    @Override
    public ResourceLocation getUid() {
        return null;
    }

    @Override
    public ItemStack getIcon() {
        return null;
    }
}
