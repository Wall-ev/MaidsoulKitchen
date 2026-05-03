package com.github.wallev.maidsoulkitchen.compat.msm.beachparty.palmbar;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_BEARCH_PARTY_PALM_BAR)
public class GeneratorBeachPalmBarGuide implements ILdCookingPotGuideGenerator<PalmBarRecipe, PalmBarBlockEntity> {
//public class GeneratorBeachPalmBarGuide implements ILdCookingPotGuideGenerator<PalmBarRecipe, Container, PalmBarBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof PalmBarBlockEntity;
    }

    @Override
    public boolean isHeated(PalmBarBlockEntity be) {
        return true;
    }

    @Override
    public int getRecipeTime(PalmBarRecipe recipe) {
        return 50;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.PALM_BAR.get().asItem();
    }

    @Override
    public RecipeType<PalmBarRecipe> getRecipeType() {
        return RecipeRegistry.PALM_BAR_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("beachparty", "palm_bar_mixing");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }
}
