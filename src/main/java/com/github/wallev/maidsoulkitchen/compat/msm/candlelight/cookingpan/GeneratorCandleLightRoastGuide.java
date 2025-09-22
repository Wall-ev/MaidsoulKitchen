package com.github.wallev.maidsoulkitchen.compat.msm.candlelight.cookingpan;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.candlelight.core.block.entity.CookingPanBlockEntity;
import net.satisfy.candlelight.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.RoasterRecipe;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_CANDLE_LINGHT_ROAST)
public class GeneratorCandleLightRoastGuide implements ILdCookingPotGuideGenerator<RoasterRecipe, CookingPanBlockEntity> {
//public class GeneratorCandleLightRoastGuide implements ILdCookingPotGuideGenerator<RoasterRecipe, Container, CookingPanBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof CookingPanBlockEntity;
    }

    @Override
    public boolean isHeated(CookingPanBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public RecipeType<RoasterRecipe> getRecipeType() {
        return RecipeTypeRegistry.ROASTER_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("candlelight", "roaster");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        return (T) recipeWrapperContainer(allInputs);
    }

    @Override
    public List<Ingredient> getContainers(RoasterRecipe recipe) {
        return toIngredients(recipe.getContainer());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.COOKING_PAN.get().asItem();
    }

    @Override
    public int getRecipeTime(RoasterRecipe recipe) {
        return 300;
    }
}
