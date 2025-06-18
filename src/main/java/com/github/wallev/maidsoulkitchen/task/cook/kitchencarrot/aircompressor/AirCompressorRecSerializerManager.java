package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.aircompressor;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import io.github.tt432.kitchenkarrot.recipes.recipe.AirCompressorRecipe;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class AirCompressorRecSerializerManager extends RecSerializerManager<AirCompressorRecipe> {
    private static final AirCompressorRecSerializerManager INSTANCE = new AirCompressorRecSerializerManager();

    protected AirCompressorRecSerializerManager() {
        super(RecipeTypes.AIR_COMPRESSOR.get());
    }

    public static AirCompressorRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initFuels() {
        this.fuels = List.of(ItemStackUtil.getItemStack(Items.REDSTONE));
    }

    @Override
    protected RecipeInfoProvider<AirCompressorRecipe> createRecipeInfoProvider() {
        return new AirCompressorRecipeInfoProvider();
    }

    public static class AirCompressorRecipeInfoProvider extends RecipeInfoProvider<AirCompressorRecipe> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<AirCompressorRecipe> rsm, AirCompressorRecipe rec) {
            List<Ingredient> ingredients = new ArrayList<>(rec.getIngredient());
            Ingredient container = rec.getContainer();
            if (container != null) {
                ingredients.add(container);
            }
            return RecIngredient.from(ingredients);
        }
    }
}
