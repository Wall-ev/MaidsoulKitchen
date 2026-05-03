package com.github.wallev.maidsoulkitchen.datagen;

import com.github.wallev.maidsoulkitchen.datagen.recipe.ModRecipeProvider;
import com.github.wallev.maidsoulkitchen.datagen.recipe.itemuse.ItemUseRecipeProvider;
import com.github.wallev.maidsoulkitchen.datagen.recipe.water.GetterWaterRecipeProvider;
import com.google.common.collect.Lists;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;

import java.util.List;
import java.util.function.Consumer;

public class ModRecipeGenerator extends RecipeProvider {
    private final List<ModRecipeProvider> providers = Lists.newArrayList();


    public ModRecipeGenerator(PackOutput output) {
        super(output);
        providers.add(new GetterWaterRecipeProvider(output));
        providers.add(new ItemUseRecipeProvider(output));
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> consumer) {
        providers.forEach(provider -> provider.buildRecipes(consumer));
    }
}
