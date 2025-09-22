package com.github.wallev.maidsoulkitchen.datagen.recipe.water;

import com.github.wallev.maidsoulkitchen.compat.msm.tea_aroma.util.TeaBrewingFoamHelper;
import com.github.wallev.maidsoulkitchen.datagen.recipe.ModRecipeProvider;
import com.github.wallev.maidsoulkitchen.recipe.water.ConsumeWaterRecipe;
import com.sammy.minersdelight.setup.MDItems;
import com.teammoeg.caupona.CPItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.world.item.Items;
import net.satisfy.meadow.core.registry.ObjectRegistry;
import org.jetbrains.annotations.NotNull;

import java.util.function.Consumer;

public class GetterWaterRecipeProvider extends ModRecipeProvider {
    public GetterWaterRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    public void buildRecipes(@NotNull Consumer<FinishedRecipe> consumer) {
        ConsumeWaterRecipeBuilder.builder()
                .setType(ConsumeWaterRecipe.Condition.SINGLE)
                .setInput(TeaBrewingFoamHelper.getEmptyBoilingKettle())
                .setResult(TeaBrewingFoamHelper.getNeedBoilingWaterKettle())
                .save(consumer);
        ConsumeWaterRecipeBuilder.builder()
                .setType(ConsumeWaterRecipe.Condition.SINGLE)
                .setInput(Items.BOWL)
                .setResult(CPItems.water_bowl.get())
                .save(consumer);

        ConsumeWaterRecipeBuilder.builder()
                .setType(ConsumeWaterRecipe.Condition.MULTIPLE)
                .setInput(ObjectRegistry.WOODEN_BUCKET.get())
                .setResult(ObjectRegistry.WOODEN_WATER_BUCKET.get())
                .save(consumer);
        ConsumeWaterRecipeBuilder.builder()
                .setType(ConsumeWaterRecipe.Condition.MULTIPLE)
                .setInput(MDItems.COPPER_CUP.get())
                .setResult(MDItems.WATER_CUP.get())
                .save(consumer);
    }
}
