package com.github.wallev.farmsoulkitchen.compat.kubejs;

import com.github.wallev.farmsoulkitchen.compat.kubejs.recipes.AltarJs;
import dev.latvian.mods.kubejs.KubeJSPlugin;
import dev.latvian.mods.kubejs.recipe.schema.RegisterRecipeSchemasEvent;
import net.minecraft.resources.ResourceLocation;

public class KubejsTlmPlugin extends KubeJSPlugin {

    @Override
    public void registerRecipeSchemas(RegisterRecipeSchemasEvent event) {
        event.register(new ResourceLocation("touhou_little_maid:altar_crafting"), AltarJs.SCHEMA);
    }
}
