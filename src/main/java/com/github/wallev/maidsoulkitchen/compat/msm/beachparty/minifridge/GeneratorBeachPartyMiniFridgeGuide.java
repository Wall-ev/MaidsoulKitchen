package com.github.wallev.maidsoulkitchen.compat.msm.beachparty.minifridge;

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
import net.satisfy.beachparty.core.block.entity.MiniFridgeBlockEntity;
import net.satisfy.beachparty.core.recipe.MiniFridgeRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;
import net.satisfy.beachparty.core.registry.RecipeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_DO_BEACH_PARTY_MINI_FRIDGE)
public class GeneratorBeachPartyMiniFridgeGuide implements ILdCookingPotGuideGenerator<MiniFridgeRecipe, MiniFridgeBlockEntity> {
//public class GeneratorBeachPartyMiniFridgeGuide implements ILdCookingPotGuideGenerator<MiniFridgeRecipe, Container, MiniFridgeBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof MiniFridgeBlockEntity;
    }

    @Override
    public int getRecipeTime(MiniFridgeRecipe recipe) {
        return recipe.getCraftingTime();
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.MINI_FRIDGE.get().asItem();
    }

    @Override
    public boolean isHeated(MiniFridgeBlockEntity be) {
        return true;
    }

    @Override
    public RecipeType<MiniFridgeRecipe> getRecipeType() {
        return RecipeRegistry.MINI_FRIDGE_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("beachparty", "mini_fridge_freezing");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }
}
