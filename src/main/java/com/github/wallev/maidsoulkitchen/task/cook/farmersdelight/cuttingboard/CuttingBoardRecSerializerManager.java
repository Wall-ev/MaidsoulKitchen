package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cuttingboard;

import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ingredient.RecIngredient;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.ToolRecSerializerManager;
import vectorwing.farmersdelight.common.crafting.CuttingBoardRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class CuttingBoardRecSerializerManager extends ToolRecSerializerManager<CuttingBoardRecipe> {
    private static final CuttingBoardRecSerializerManager INSTANCE = new CuttingBoardRecSerializerManager();

    protected CuttingBoardRecSerializerManager() {
        super(ModRecipeTypes.CUTTING.get());
    }

    public static CuttingBoardRecSerializerManager getInstance() {
        return INSTANCE;
    }

    @Override
    protected ToolRecipeInfoProvider<CuttingBoardRecipe> createRecipeInfoProvider() {
        return new CuttingBoardRecipeInfoProvider();
    }

    public static class CuttingBoardRecipeInfoProvider extends ToolRecipeInfoProvider<CuttingBoardRecipe> {
        @Override
        public RecIngredient getTool(RecSerializerManager<CuttingBoardRecipe> rsm, CuttingBoardRecipe rec) {
            return RecIngredient.of(rec.getTool());
        }
    }
}
