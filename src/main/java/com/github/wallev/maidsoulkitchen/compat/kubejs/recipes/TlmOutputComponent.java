package com.github.wallev.maidsoulkitchen.compat.kubejs.recipes;

import com.github.tartaricacid.touhoulittlemaid.util.EntityCraftingHelper;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import dev.latvian.mods.kubejs.recipe.component.RecipeComponent;
import dev.latvian.mods.kubejs.util.MapJS;

public class TlmOutputComponent implements RecipeComponent<EntityCraftingHelper.Output> {
    public static final RecipeComponent<EntityCraftingHelper.Output> INSTANCE = new TlmOutputComponent();
    @Override
    public String componentType() {
        return "touhou_little_maid_output";
    }

    @Override
    public Class<?> componentClass() {
        return EntityCraftingHelper.Output.class;
    }

    @Override
    public JsonElement write(RecipeJS recipe, EntityCraftingHelper.Output value) {
        final JsonObject obj = new JsonObject();
        obj.addProperty("type", value.getType().getDescriptionId());
        if (!value.getData().isEmpty()){
            obj.addProperty("nbt", value.getData().toString());
        }
        return obj;
    }

    @Override
    public EntityCraftingHelper.Output read(RecipeJS recipe, Object from) {
        return EntityCraftingHelper.getEntityData(MapJS.json(from));
    }
}
