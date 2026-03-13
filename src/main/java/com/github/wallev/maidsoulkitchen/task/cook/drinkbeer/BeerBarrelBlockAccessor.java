package com.github.wallev.maidsoulkitchen.task.cook.drinkbeer;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import lekavar.lma.drinkbeer.recipes.BrewingRecipe;


import javax.annotation.Nullable;

public interface BeerBarrelBlockAccessor extends IMskMixinInterface {

    int tlmk$statusCode();

    boolean tlmk$canBrew(@Nullable BrewingRecipe recipe);

    boolean tlmk$hasEnoughEmptyCap(BrewingRecipe recipe);
}
