package com.github.wallev.maidsoulkitchen.datagen.recipe;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;

import java.util.List;
import java.util.Set;

public interface IModRecipeConditionBuilder<T extends IModRecipeConditionBuilder<T>> {

    Set<String> getModIds();

    List<ICondition> getConditions();

    default T addCondition(ICondition condition) {
        getConditions().add(condition);
        return (T) this;
    }

    default T addModRecipe(IMods mod) {
        if (getModIds().contains(mod.modId()))
            return (T) this;

        return this.addModRecipe(mod.modId());
    }

    default T addModRecipe(String modId) {
        if (getModIds().contains(modId))
            return (T) this;

        this.getModIds().add(modId);
        return addCondition(new ModLoadedCondition(modId));
    }

}
