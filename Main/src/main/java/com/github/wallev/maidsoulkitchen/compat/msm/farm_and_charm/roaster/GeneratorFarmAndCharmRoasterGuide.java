package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.roaster;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.farm_and_charm.core.block.entity.RoasterBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.RoasterRecipe;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FARM_AND_CHARM_ROASTER)
public class GeneratorFarmAndCharmRoasterGuide implements ILdCookingPotGuideGenerator<RoasterRecipe, RoasterBlockEntity> {
//public class GeneratorFarmAndCharmRoasterGuide implements ILdCookingPotGuideGenerator<RoasterRecipe, Container, RoasterBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof RoasterBlockEntity;
    }

    @Override
    public boolean isHeated(RoasterBlockEntity be) {
        return be.isBeingBurned();
    }

    @Override
    public RecipeType<RoasterRecipe> getRecipeType() {
        return RecipeTypeRegistry.ROASTER_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.ofTypeMod("farm_and_charm", "roaster");
    }

    @Override
    public List<Ingredient> getContainers(RoasterRecipe recipe) {
        return toIngredients(recipe.getContainer());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.ROASTER.get().asItem();
    }

    @Override
    public int getRecipeTime(RoasterRecipe recipe) {
        return RoasterBlockEntity.getMaxRoastingTime();
    }
}
