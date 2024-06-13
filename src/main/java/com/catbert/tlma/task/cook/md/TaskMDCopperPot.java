package com.catbert.tlma.task.cook.md;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.mixin.md.CopperPotBlockEntityAccessor;
import com.catbert.tlma.task.cook.common.TaskFdPot;
import com.catbert.tlma.task.cook.handler.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.api.LittleMaidExtension;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.setup.MDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;
import java.util.Optional;

@LittleMaidExtension
public class TaskMDCopperPot extends TaskFdPot<CopperPotBlockEntity, CookingPotRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "md_copper_pot");

    @Override
    public boolean canLoaded() {
        return Mods.MD.isLoaded;
    }

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
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return MDBlocks.COPPER_POT.asStack();
    }

    @Override
    public MaidRecipesManager<CookingPotRecipe> getRecipesManager(EntityMaid maid) {
        return new MaidRecipesManager<>(maid, getRecipeType(), false) {
            @Override
            protected List<CookingPotRecipe> filterRecipes(List<CookingPotRecipe> recipes) {
                return recipes.stream().filter(cookingPotRecipe -> {
                    return cookingPotRecipe.getIngredients().size() <= 4;
                }).toList();
            }
        };
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
    public Optional<CookingPotRecipe> getMatchingRecipe(CopperPotBlockEntity be, RecipeWrapper recipeWrapper) {
        return ((CopperPotBlockEntityAccessor) be).getMatchingRecipe$tlma(recipeWrapper);
    }

    @Override
    public boolean canCook(CopperPotBlockEntity be, CookingPotRecipe recipe) {
        return ((CopperPotBlockEntityAccessor) be).canCook$tlma(recipe);
    }

    @Override
    public boolean isHeated(CopperPotBlockEntity be) {
        return be.isHeated();
    }
}
