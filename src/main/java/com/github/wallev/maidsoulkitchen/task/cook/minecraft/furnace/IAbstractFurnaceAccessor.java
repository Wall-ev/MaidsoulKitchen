package com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

@TaskClassAnalyzer(TaskInfo.FURNACE)
public interface IAbstractFurnaceAccessor {
    RecipeType<AbstractCookingRecipe> tlmk$getRecipeType();

    boolean tlmk$isLit();
}
