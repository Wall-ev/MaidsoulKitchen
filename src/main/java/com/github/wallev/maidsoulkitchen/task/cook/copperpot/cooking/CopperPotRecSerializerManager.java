package com.github.wallev.maidsoulkitchen.task.cook.copperpot.cooking;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

@TaskClassAnalyzer(TaskInfo.COPPER_POT)
public class CopperPotRecSerializerManager extends RecSerializerManager<CookingPotRecipe> {
    private static final CopperPotRecSerializerManager INSTANCE = new CopperPotRecSerializerManager();

    protected CopperPotRecSerializerManager() {
        super(ModRecipeTypes.COOKING.get());
    }

    public static CopperPotRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected void initRecs(Level level) {
        this.recipes = createDefaultRecs(level).stream()
                .filter(r -> r.inItems().size() < 4)
                .toList();
    }

    @Override
    protected RecipeInfoProvider<CookingPotRecipe> createRecipeInfoProvider() {
        return new CookingPotRecipeInfo();
    }

    public static class CookingPotRecipeInfo extends RecipeInfoProvider<CookingPotRecipe> {

        @Override
        public ItemStack getOutput(RecSerializerManager<CookingPotRecipe> rsm, CookingPotRecipe rec) {
            return super.getOutput(rsm, rec);
        }

        @Override
        public ItemStack getContainer(RecSerializerManager<CookingPotRecipe> rsm, CookingPotRecipe rec) {
            return super.getContainer(rsm, rec);
        }
    }
}
