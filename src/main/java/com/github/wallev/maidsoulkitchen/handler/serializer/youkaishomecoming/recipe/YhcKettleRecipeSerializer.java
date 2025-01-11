package com.github.wallev.maidsoulkitchen.handler.serializer.youkaishomecoming.recipe;

import com.github.wallev.maidsoulkitchen.handler.base.recipe.OutputContainerCookRecSerializer;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.ItemStack;

public class YhcKettleRecipeSerializer extends OutputContainerCookRecSerializer<KettleRecipe> {
    public YhcKettleRecipeSerializer() {
        super(YHBlocks.KETTLE_RT.get());
    }

    @Override
    public ItemStack getContainer(KettleRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
