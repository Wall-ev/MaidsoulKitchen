package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.brewinandchewin;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.ContainerCookRecSerializer;
import net.minecraft.world.item.ItemStack;
import umpaz.brewinandchewin.common.crafting.KegRecipe;
import umpaz.brewinandchewin.common.registry.BCRecipeTypes;

public class BncKeyRecipeSerializer extends ContainerCookRecSerializer<KegRecipe> {
    public BncKeyRecipeSerializer() {
        super(BCRecipeTypes.FERMENTING.get());
    }

    @Override
    public ItemStack getContainer(KegRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
