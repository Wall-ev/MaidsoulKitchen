package com.github.wallev.maidsoulkitchen.task.cook.minersdelight.cooking;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.sammy.minersdelight.logic.CupConversionReloadListener;
import com.sammy.minersdelight.setup.MDItems;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

@TaskClassAnalyzer(TaskInfo.MD_COOK_POT)
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
                .filter(r -> r.inItems().size() <= 4)
                .toList();
    }

    @Override
    protected RecipeInfoProvider<CookingPotRecipe> createRecipeInfoProvider() {
        return new CookingPotRecipeInfo();
    }

    public static class CookingPotRecipeInfo extends RecipeInfoProvider<CookingPotRecipe> {

        @Override
        public ItemStack getOutput(RecSerializerManager<CookingPotRecipe> rsm, CookingPotRecipe rec) {
            ItemStack output = rec.getResultItem(RegistryAccess.EMPTY);
            Item cupOutput = CupConversionReloadListener.BOWL_TO_CUP.get(output.getItem());
            return cupOutput != null ? new ItemStack(cupOutput) : output;
        }

        @Override
        public ItemStack getContainer(RecSerializerManager<CookingPotRecipe> rsm, CookingPotRecipe rec) {
            ItemStack output = rec.getResultItem(RegistryAccess.EMPTY);
            boolean cupServed = CupConversionReloadListener.BOWL_TO_CUP.containsKey(output.getItem());
            return cupServed ? MDItems.COPPER_CUP.asStack() : rec.getOutputContainer();
        }
    }
}
