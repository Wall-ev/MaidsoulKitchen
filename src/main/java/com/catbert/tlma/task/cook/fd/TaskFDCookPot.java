package com.catbert.tlma.task.cook.fd;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.mixin.fd.CookingPotBlockEntityAccessor;
import com.catbert.tlma.task.cook.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;

import java.util.Optional;

@LittleMaidExtension
public class TaskFDCookPot extends TaskFdPot<CookingPotBlockEntity> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "fd_cooking_pot");

    @Override
    public boolean modLoaded() {
        return Mods.FD.isLoaded;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CookingPotBlockEntity;
    }

    @Override
    public int getOutputSlot() {
        return CookingPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputEndSlot() {
        return 5;
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
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
    public Optional<CookingPotRecipe> getMatchingRecipe(CookingPotBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((CookingPotBlockEntityAccessor) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    public boolean canCook(CookingPotBlockEntity be, CookingPotRecipe recipe) {
        return ((CookingPotBlockEntityAccessor) be).canCook$tlma(recipe);
    }

    @Override
    public boolean isHeated(CookingPotBlockEntity be) {
        return be.isHeated();
    }
}
