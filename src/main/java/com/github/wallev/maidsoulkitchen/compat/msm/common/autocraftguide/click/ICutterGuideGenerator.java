package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.click;

import com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base.ICookingRecipeGuideGenerator;
import com.github.wallev.maidsoulkitchen.compat.msm.common.util.CraftGuideOperator2;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;

import java.util.List;

public interface ICutterGuideGenerator<R extends Recipe<? extends Container>> extends ICookingRecipeGuideGenerator<R> {
//public interface ICutterGuideGenerator<R extends Recipe<C>, C extends Container> extends ICookingRecipeGuideGenerator<R, C> {

    @Override
    default void generateSteps(BlockPos pos, Level level, R recipe, CraftGuideOperator2 craftGuide, List<ItemStack> realItems, boolean needContainer, List<ItemStack> containers, List<ItemStack> outputs, List<ItemStack> remains) {
        craftGuide.addItemUse(realItems);

        int cutCount = this.cutCount(recipe);
        for (int i = 0; i < cutCount; i++) {
            craftGuide.addToolUse(containers.get(0));
        }

        craftGuide.addItemPickup(outputs);
    }

    @Override
    default int getRecipeTime(R recipe) {
        return 0;
    }

    @Override
    default List<Ingredient> getContainers(R recipe) {
        return this.getTools(recipe);
    }

    List<Ingredient> getTools(R recipe);

    int cutCount(R recipe);
}
