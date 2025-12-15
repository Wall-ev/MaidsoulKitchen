package com.github.wallev.maidsoulkitchen.datagen.recipe.itemuse;

import com.github.wallev.maidsoulkitchen.datagen.recipe.ModRecipeProvider;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.recipe.itemuse.ItemUseRecipe;
import com.renyigesai.bakeries.init.BakeriesItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class ItemUseRecipeProvider extends ModRecipeProvider {
    public ItemUseRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ItemUseRecipeBuilder.builder()
//                .addModRecipe(Mods.BAKERIES)
                .setType(ItemUseRecipe.Condition.SINGLE)
                .setInputs(
                        Items.EGG.getDefaultInstance(),
                        BakeriesItems.BREAD_KNIFE.get().getDefaultInstance()
                )
                .setResults(
                        BakeriesItems.WHOLE_EGG.get().getDefaultInstance(),
                        BakeriesItems.BREAD_KNIFE.get().getDefaultInstance()
                )
                .save(consumer);
        ItemUseRecipeBuilder.builder()
//                .addModRecipe(Mods.BAKERIES)
                .setType(ItemUseRecipe.Condition.SINGLE)
                .setInputs(
                        BakeriesItems.WHOLE_EGG.get().getDefaultInstance(),
                        BakeriesItems.BREAD_KNIFE.get().getDefaultInstance()
                )
                .setResults(
                        BakeriesItems.RAW_EGG_YOLK.get().getDefaultInstance(),
                        BakeriesItems.RAW_PROTEIN.get().getDefaultInstance(),
                        BakeriesItems.BREAD_KNIFE.get().getDefaultInstance()
                )
                .save(consumer);
    }
}
