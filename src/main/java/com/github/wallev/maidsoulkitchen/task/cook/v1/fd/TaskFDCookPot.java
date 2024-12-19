package com.github.wallev.maidsoulkitchen.task.cook.v1.fd;

import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;


public class TaskFDCookPot extends TaskFdPot<CookingPotBlockEntity, CookingPotRecipe> {
    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CookingPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public int getOutputSlot() {
        return CookingPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 6;
    }

    @Override
    public ItemStackHandler getBeInv(CookingPotBlockEntity cookingPotBlockEntity) {
        return cookingPotBlockEntity.getInventory();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.FD_COOK_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.COOKING_POT.get().getDefaultInstance();
    }

    @Override
    public int getMealStackSlot() {
        return CookingPotBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return CookingPotBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(CookingPotBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public ItemStackHandler getItemStackHandler(CookingPotBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public boolean isHeated(CookingPotBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.FD_COOK_POT;
    }

}
