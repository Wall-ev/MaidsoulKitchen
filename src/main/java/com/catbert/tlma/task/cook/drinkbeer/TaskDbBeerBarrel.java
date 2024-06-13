package com.catbert.tlma.task.cook.drinkbeer;

import com.catbert.tlma.TLMAddon;
import com.catbert.tlma.foundation.utility.Mods;
import com.catbert.tlma.task.cook.common.TaskFdCiCook;
import lekavar.lma.drinkbeer.blockentities.BeerBarrelBlockEntity;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;
import lekavar.lma.drinkbeer.registries.BlockRegistry;
import lekavar.lma.drinkbeer.registries.RecipeRegistry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.Optional;

public class TaskDbBeerBarrel extends TaskFdCiCook<BeerBarrelBlockEntity, BrewingRecipe> {
    public static final ResourceLocation NAME = new ResourceLocation(TLMAddon.MOD_ID, "drinkbeer_beerbarrel");

    @Override
    public boolean canLoaded() {
        return Mods.DB.isLoaded;
    }

    @Override
    public ItemStackHandler getItemStackHandler(BeerBarrelBlockEntity be) {
        return null;
    }

    @Override
    public Optional<BrewingRecipe> getMatchingRecipe(BeerBarrelBlockEntity be, RecipeWrapper recipeWrapper) {
        return Optional.empty();
    }

    @Override
    public boolean canCook(BeerBarrelBlockEntity be, BrewingRecipe recipe) {
        return false;
    }

    @Override
    public boolean isHeated(BeerBarrelBlockEntity be) {
        return false;
    }

    @Override
    public int getOutputSlot() {
        return 0;
    }

    @Override
    public int getInputSize() {
        return 0;
    }

    @Override
    public int getMealStackSlot() {
        return 0;
    }

    @Override
    public int getContainerStackSlot() {
        return 0;
    }

    @Override
    public ItemStack getFoodContainer(BeerBarrelBlockEntity blockEntity) {
        return null;
    }

    @Override
    public boolean isCookBE(BlockEntity blockEntity) {
        return blockEntity instanceof BeerBarrelBlockEntity;
    }

    @Override
    public RecipeType<BrewingRecipe> getRecipeType() {
        return RecipeRegistry.RECIPE_TYPE_BREWING.get();
    }

    @Override
    public ResourceLocation getUid() {
        return NAME;
    }

    @Override
    public ItemStack getIcon() {
        return BlockRegistry.BEER_BARREL.get().asItem().getDefaultInstance();
    }
}
