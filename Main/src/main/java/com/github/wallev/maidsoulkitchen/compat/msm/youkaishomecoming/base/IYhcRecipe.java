package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.base;

import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;

public interface IYhcRecipe<R extends Recipe<? extends Container>> {

    @SuppressWarnings("unchecked")
    default R castRecipe(Recipe<?> recipe) {
        return (R) recipe;
    }

}
