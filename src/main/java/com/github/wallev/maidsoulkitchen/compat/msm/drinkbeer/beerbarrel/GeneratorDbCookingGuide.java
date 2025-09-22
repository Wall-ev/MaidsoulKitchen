package com.github.wallev.maidsoulkitchen.compat.msm.drinkbeer.beerbarrel;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.MaidPathFindingBFS;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.AutoCraftGuideGeneratorRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContainerStorage;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import com.github.wallev.maidsoulkitchen.mixin.compat.drinkbeer.BeerBarrelBlockAccessor;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.GuideTest;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.Lists;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.recipes.IBrewingInventory;
import lekavar.lma.drinkbeer.registries.ItemRegistry;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.List;

//@GuideTest
@AutoCraftGuideGeneratorRegister(TaskInfo.MSM_DB_DRINKBEER_BEERBARREL)
public class GeneratorDbCookingGuide implements ICookingRecipeGuideGenerator<BrewingRecipe> {
//public class GeneratorDbCookingGuide implements ICookingRecipeGuideGenerator<BrewingRecipe, IBrewingInventory> {

    @Override
    public boolean isValidBlockInWorld(ServerLevel level, EntityMaid maid, BlockPos pos, MaidPathFindingBFS pathFinding) {
        return level.getBlockEntity(pos) instanceof BeerBarrelBlockEntity be && ((BeerBarrelBlockAccessor) be).tlmk$statusCode() == 0;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return RecipeRegistry.RECIPE_TYPE_BREWING.get();
    }

    @Override
    public @NotNull ResourceLocation getType() {
        return VResourceLocation.createTypeMod("drinkbeer", "brewing");
    }

    @Override
    public <T extends Container> T convert2InputsInv(List<ItemStack> allInputs) {
        BeerBarrelBlockEntity.BrewingInventory brewingInventory = new BeerBarrelBlockEntity.BrewingInventory(null);
        return (T) brewingInventory;
    }

    @Override
    public Item getBlockItemForTranslate() {
        return ItemRegistry.BEER_BARREL.get();
    }

    @Override
    public boolean isBlockValid(Level level, BlockPos pos) {
        return level.getBlockEntity(pos) instanceof BeerBarrelBlockEntity;
    }

    @Override
    public int getRecipeTime(BrewingRecipe recipe) {
        return recipe.getBrewingTime();
    }

    @Override
    public List<Ingredient> getInputs(BrewingRecipe recipe) {
        List<Ingredient> ingredients = Lists.newArrayList(ICookingRecipeGuideGenerator.super.getInputs(recipe));
        ItemStack beerCup = recipe.getBeerCup();
        ingredients.add(Ingredient.of(beerCup));
        return ingredients;
    }

    @Override
    public void outputsStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> outputs) {
        ICookingRecipeGuideGenerator.super.outputsStep(pos, craftGuide, outputs);
    }

    @Override
    public ResourceLocation getInputStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public ResourceLocation getOutputStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public ResourceLocation getRemainStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public ResourceLocation getOutputContainerStorageType() {
        return ContainerStorage.TYPE;
    }

    @Override
    public void remainStep(BlockPos pos, CraftGuideOperator2 craftGuide, List<ItemStack> remains) {
        this.remainTakeStep(pos, craftGuide, remains);
    }
}
