package com.github.wallev.farmsoulkitchen.task.cook.v1.md;

import com.github.wallev.farmsoulkitchen.FarmsoulKitchen;
import com.github.wallev.farmsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.farmsoulkitchen.init.registry.tlm.RegisterData;
import com.github.wallev.farmsoulkitchen.task.cook.v1.common.TaskFdPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.setup.MDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;


public class TaskMDCopperPot extends TaskFdPot<CopperPotBlockEntity, CookingPotRecipe> {
    public static final ResourceLocation UID = new ResourceLocation(FarmsoulKitchen.MOD_ID, "md_copper_pot");

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof CopperPotBlockEntity;
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return UID;
    }

    @Override
    public ItemStack getIcon() {
        return MDBlocks.COPPER_POT.asStack();
    }

    @Override
    public List<CookingPotRecipe> getRecipes(Level level) {
        return super.getRecipes(level).stream().filter(cookingPotRecipe -> {
            return cookingPotRecipe.getIngredients().size() <= 4;
        }).toList();
    }

    @Override
    public int getOutputSlot() {
        return CopperPotBlockEntity.OUTPUT_SLOT;
    }

    @Override
    public int getInputSize() {
        return 4;
    }

    @Override
    public ItemStackHandler getBeInv(CopperPotBlockEntity copperPotBlockEntity) {
        return copperPotBlockEntity.getInventory();
    }

    @Override
    public int getMealStackSlot() {
        return CopperPotBlockEntity.MEAL_DISPLAY_SLOT;
    }

    @Override
    public int getContainerStackSlot() {
        return CopperPotBlockEntity.CONTAINER_SLOT;
    }

    @Override
    public ItemStack getFoodContainer(CopperPotBlockEntity blockEntity) {
        return blockEntity.getContainer();
    }

    @Override
    public ItemStackHandler getItemStackHandler(CopperPotBlockEntity be) {
        return be.getInventory();
    }

    @Override
    public boolean isHeated(CopperPotBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return RegisterData.MD_COPPER_POT;
    }

}
