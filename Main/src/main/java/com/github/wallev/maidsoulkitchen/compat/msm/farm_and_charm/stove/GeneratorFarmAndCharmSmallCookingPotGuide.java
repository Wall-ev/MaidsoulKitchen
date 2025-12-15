package com.github.wallev.maidsoulkitchen.compat.msm.farm_and_charm.stove;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.StoveRecipe;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.registry.RecipeTypeRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.List;


//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_FARM_AND_CHARM_STOVE)
public class GeneratorFarmAndCharmSmallCookingPotGuide implements ILdCookingPotGuideGenerator<StoveRecipe, StoveBlockEntity> {
//public class GeneratorFarmAndCharmSmallCookingPotGuide implements ILdCookingPotGuideGenerator<StoveRecipe, Container, StoveBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof StoveBlockEntity;
    }

    @Override
    public boolean isHeated(StoveBlockEntity be) {
        return true;
//        return ((StoveBlockEntityAccessor) be).callIsBurning();
    }

    @Override
    public RecipeType<StoveRecipe> getRecipeType() {
        return RecipeTypeRegistry.STOVE_RECIPE_TYPE.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.ofTypeMod("farm_and_charm", "stove");
    }

    @Override
    public List<Ingredient> getContainers(StoveRecipe recipe) {
        return List.of(Ingredient.of(ItemTags.COALS));
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ObjectRegistry.STOVE.get().asItem();
    }

    @Override
    public int getRecipeTime(StoveRecipe recipe) {
        return StoveBlockEntity.TOTAL_COOKING_TIME;
    }
}
