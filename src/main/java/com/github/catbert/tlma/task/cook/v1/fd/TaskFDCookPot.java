package com.github.catbert.tlma.task.cook.v1.fd;

import com.github.catbert.tlma.TLMAddon;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

@LittleMaidExtension
public class TaskFDCookPot extends TaskFdPot<CookingPotBlockEntity, CookingPotRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "fd_cooking_pot");

    @Override
    public boolean canLoaded() {
        return Mods.FD.isLoaded;
    }

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
    public boolean isHeated(CookingPotBlockEntity be) {
        return be.isHeated();
    }
}
