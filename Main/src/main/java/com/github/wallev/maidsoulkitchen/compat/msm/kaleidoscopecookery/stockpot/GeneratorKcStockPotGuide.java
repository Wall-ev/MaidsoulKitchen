package com.github.wallev.maidsoulkitchen.compat.msm.kaleidoscopecookery.stockpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.cookingpot.ILdCookingPotGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.ysbbbbbb.kaleidoscopecookery.api.recipe.soupbase.ISoupBase;
import com.github.ysbbbbbb.kaleidoscopecookery.blockentity.kitchen.StockpotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.recipe.StockpotRecipe;
import com.github.ysbbbbbb.kaleidoscopecookery.crafting.soupbase.SoupBaseManager;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModRecipes;
import com.google.common.collect.Lists;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_KC_STOCK_POT)
public class GeneratorKcStockPotGuide implements ILdCookingPotGuideGenerator<StockpotRecipe, StockpotBlockEntity> {

    private static boolean isHit(StockpotBlockEntity be) {
        Level level = be.getLevel();
        assert level != null;
        BlockPos blockPos = be.getBlockPos();
        return level.getBlockState(blockPos.below()).getOptionalValue(BlockStateProperties.LIT).orElse(false);
    }

    private static boolean hasLitItem(StockpotBlockEntity be) {
        return be.hasLid();
    }

    @Override
    public boolean isValidBlockEntity(BlockEntity be) {
        return be instanceof StockpotBlockEntity;
    }

    @Override
    public boolean isHeated(StockpotBlockEntity be) {
        return hasLitItem(be) && isHit(be);
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return ILdCookingPotGuideGenerator.super.isBlockValid(level, pos);
    }

    @Override
    public List<Ingredient> getInputs(StockpotRecipe recipe) {
        List<Ingredient> ingredients = Lists.newArrayList();

        // 汤基
        ResourceLocation resourceLocation = recipe.soupBase();
        ISoupBase soupBase = SoupBaseManager.getSoupBase(resourceLocation);
        ItemStack displayStack = soupBase.getDisplayStack();
        ingredients.add(Ingredient.of(displayStack));

        NonNullList<Ingredient> ingredients1 = recipe.getIngredients();
        for (Ingredient ingredient : ingredients1) {
            if (!ingredient.isEmpty()) {
                ingredients.add(ingredient);
            }
        }
        return ingredients;
    }

    @Override
    public List<Ingredient> getContainers(StockpotRecipe recipe) {
        return toIngredients(Items.BOWL.getDefaultInstance());
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ModItems.STOCKPOT.get();
    }

    @Override
    public void generateSteps(BlockPos pos, Level level, StockpotRecipe recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        ItemStack container = ItemStack.EMPTY;
        List<ItemStack> inputsWithOil = Lists.newArrayList(realItems);
        if (needContainer) {
            container = containers.get(0);
        }

        // 开盖
        craftGuide.addEmptyUse(ModItems.STOCKPOT_LID.get().getDefaultInstance());
        // 放汤基和食材
        CraftGuideOperator2.forEachItem(inputsWithOil, craftGuide::addItemUse);
        // 上盖
        craftGuide.addItemUse(ModItems.STOCKPOT_LID.get().getDefaultInstance());
        // 等待
        craftGuide.addIdle(recipe.time() + 10);
        // 开盖
        craftGuide.addEmptyUse(ModItems.STOCKPOT_LID.get().getDefaultInstance());

        ItemStack container0 = container.copyWithCount(1);
        ItemStack result = outputs.get(0).copyWithCount(1);
        // 盛饭
        CraftGuideOperator2.forEachSingleItem(outputs.get(0), itemStack -> craftGuide.addItemUse(container0, result));
        // 上盖
        craftGuide.addItemUse(ModItems.STOCKPOT_LID.get().getDefaultInstance());
    }

    @Override
    public int getRecipeTime(StockpotRecipe recipe) {
        return recipe.time();
    }

    @Override
    public RecipeType<StockpotRecipe> getRecipeType() {
        return ModRecipes.STOCKPOT_RECIPE;
    }
}
