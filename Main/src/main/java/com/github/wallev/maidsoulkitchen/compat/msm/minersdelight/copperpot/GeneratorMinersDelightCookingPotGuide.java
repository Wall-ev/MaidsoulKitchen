package com.github.wallev.maidsoulkitchen.compat.msm.minersdelight.copperpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.IFdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.logic.CupConversionReloadListener;
import com.sammy.minersdelight.setup.MDBlocks;
import com.sammy.minersdelight.setup.MDItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_MD_COOKING_POT)
public class GeneratorMinersDelightCookingPotGuide implements IFdCookingPotGuideGenerator<CookingPotRecipe, CopperPotBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof CopperPotBlockEntity;
    }

    @Override
    public boolean isHeated(CopperPotBlockEntity be) {
        return be.isHeated();
    }

    @Override
    public RecipeType<CookingPotRecipe> getRecipeType() {
        return ModRecipeTypes.COOKING.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("miners_delight:cooking");
    }

    @Override
    public int getRecipeTime(CookingPotRecipe recipe) {
        return recipe.getCookTime();
    }

    @Override
    public boolean isValidRecipe(CookingPotRecipe recipe) {
        return recipe.getIngredients().size() <= 4;
    }

    @Override
    public List<Ingredient> getContainers(CookingPotRecipe recipe) {
        ItemStack output = recipe.getResultItem(RegistryAccess.EMPTY);
        boolean cupServed = CupConversionReloadListener.BOWL_TO_CUP.containsKey(output.getItem());
        if (cupServed) {
            ItemStack stack = MDItems.COPPER_CUP.asStack();
            stack.setCount(output.getCount() * 2);
            return toIngredients(stack);
        } else {
            ItemStack outputContainer = recipe.getOutputContainer();
            boolean empty = outputContainer.isEmpty();
            if (empty) {
                return List.of();
            } else {
                outputContainer.setCount(output.getCount());
                return toIngredients(outputContainer);
            }
        }
    }

    @Override
    public List<ItemStack> getOutputs(CookingPotRecipe recipe, RegistryAccess registryAccess) {
        ItemStack output = recipe.getResultItem(RegistryAccess.EMPTY);
        Item cupOutput = CupConversionReloadListener.BOWL_TO_CUP.get(output.getItem());
        if (cupOutput != null) {
            return List.of(new ItemStack(cupOutput, output.getCount() * 2));
        } else {
            return List.of(output);
        }
    }

    @Override
    public Item getBlockItemForTranslate() {
        return MDBlocks.COPPER_POT.asItem();
    }

    @Override
    public ResourceLocation getRecipeId(CookingPotRecipe recipe) {
        ItemStack output = recipe.getResultItem(RegistryAccess.EMPTY);
        boolean cupServed = CupConversionReloadListener.BOWL_TO_CUP.containsKey(output.getItem());
        ResourceLocation id = recipe.getId();
        if (cupServed) {
            return new ResourceLocation("miners_delight", id.getPath());
        } else {
            return id;
        }
    }
}
