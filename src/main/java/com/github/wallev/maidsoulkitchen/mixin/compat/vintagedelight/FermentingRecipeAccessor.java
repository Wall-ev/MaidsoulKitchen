package com.github.wallev.maidsoulkitchen.mixin.compat.vintagedelight;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskMixin;
import net.minecraft.world.item.crafting.Ingredient;
import net.ribs.vintagedelight.recipe.FermentingRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@TaskMixin(TaskInfo.MSM_VTD_FERMENTING_JAR)
@Mixin(value = FermentingRecipe.class, remap = false)
public interface FermentingRecipeAccessor extends IMskMixinInterface {
    @Accessor("containerIngredient")
    Ingredient msk$getContainerIngredient();
}
