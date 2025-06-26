package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.ItemStackUtil;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import io.github.tt432.kitchenkarrot.recipes.recipe.BrewingBarrelRecipe;
import io.github.tt432.kitchenkarrot.registries.ModItems;
import io.github.tt432.kitchenkarrot.registries.RecipeTypes;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.List;

@TaskClassAnalyzer(TaskInfo.KK_BREW_BARREL)
public class KkBrewingBarrelRecSerializerManager extends RecSerializerManager<BrewingBarrelRecipe> {
    private static final KkBrewingBarrelRecSerializerManager INSTANCE = new KkBrewingBarrelRecSerializerManager();

    protected KkBrewingBarrelRecSerializerManager() {
        super(RecipeTypes.BREWING_BARREL.get());
    }

    public static KkBrewingBarrelRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected RecipeInfoProvider<BrewingBarrelRecipe> createRecipeInfoProvider() {
        return new BrewingBarrelRecipeInfoProvider();
    }

    @Override
    protected void initFuels() {
        List<ItemStack> fuels = new ArrayList<>();
        fuels.add(ItemStackUtil.getItemStack(Items.WATER_BUCKET));
        fuels.add(new ItemStack(ModItems.WATER.get(), 4));
        this.fuels = fuels;
    }

    public static class BrewingBarrelRecipeInfoProvider extends RecipeInfoProvider<BrewingBarrelRecipe> {
        @Override
        public List<RecIngredient> getIngredients(RecSerializerManager<BrewingBarrelRecipe> rsm, BrewingBarrelRecipe rec) {
            return RecIngredient.from(rec.getIngredient());
        }
    }
}
