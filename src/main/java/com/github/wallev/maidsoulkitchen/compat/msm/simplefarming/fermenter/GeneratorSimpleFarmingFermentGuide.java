package com.github.wallev.maidsoulkitchen.compat.msm.simplefarming.fermenter;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import dev.enemeez.simplefarming.common.SimpleFarming;
import dev.enemeez.simplefarming.common.block.entity.FermenterBlockEntity;
import dev.enemeez.simplefarming.common.item.crafting.FermenterRecipe;
import dev.enemeez.simplefarming.common.registries.ModItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_SIMPLE_FARMING_FERMENTER)
public class GeneratorSimpleFarmingFermentGuide implements ILdCookingPotGuideGenerator<FermenterRecipe, FermenterBlockEntity> {
    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof FermenterBlockEntity;
    }

    @Override
    public boolean isHeated(FermenterBlockEntity be) {
        return true;
    }

    @Override
    public RecipeType<FermenterRecipe> getRecipeType() {
        return FermenterRecipe.Type.INSTANCE;
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod(SimpleFarming.MOD_ID, FermenterRecipe.Type.ID);
    }

    @Override
    public List<Ingredient> getContainers(FermenterRecipe recipe) {
        return toIngredients(Items.GLASS_BOTTLE.getDefaultInstance());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.FERMENTER.get().asItem();
    }

    @Override
    public List<Ingredient> getAllInputs(FermenterRecipe recipe) {
        return ILdCookingPotGuideGenerator.super.getAllInputs(recipe);
    }

    @Override
    public List<Ingredient> getInputs(FermenterRecipe recipe) {
        List<Ingredient> inputs = ILdCookingPotGuideGenerator.super.getInputs(recipe);
        return List.of(inputs.get(0));
    }

    @Override
    public int getRecipeTime(FermenterRecipe recipe) {
        return 2000;
    }
}
