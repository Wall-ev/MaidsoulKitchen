package com.github.wallev.maidsoulkitchen.handler.rule.common;

import com.github.wallev.maidsoulkitchen.handler.rec.CookRec;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class DefaultCookRecRuleSerializer<R extends Recipe<? extends Container>> extends AbstractCookRecRuleSerializer<R, CookRec<R>> {
    public DefaultCookRecRuleSerializer(RecipeType<R> recipeType) {
        super(recipeType);
    }
}
