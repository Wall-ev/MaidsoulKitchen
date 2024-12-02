package com.github.wallev.farmsoulkitchen.compat.kubejs.recipes;

import com.github.tartaricacid.touhoulittlemaid.util.EntityCraftingHelper;
import dev.latvian.mods.kubejs.item.InputItem;
import dev.latvian.mods.kubejs.recipe.RecipeKey;
import dev.latvian.mods.kubejs.recipe.component.ItemComponents;
import dev.latvian.mods.kubejs.recipe.component.NumberComponent;
import dev.latvian.mods.kubejs.recipe.schema.RecipeSchema;

public interface AltarJs {
    RecipeKey<InputItem[]> INPUTS = ItemComponents.UNWRAPPED_INPUT_ARRAY.key("ingredients");
    RecipeKey<Float> POWER = NumberComponent.FLOAT.key("power");
    RecipeKey<EntityCraftingHelper.Output> OUTPUT = TlmOutputComponent.INSTANCE.key("output");

    RecipeSchema SCHEMA = new RecipeSchema(INPUTS, POWER, OUTPUT);

}
