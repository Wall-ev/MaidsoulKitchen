package com.github.wallev.maidsoulkitchen.handler.serializer.recipe.youkaishomecoming;

import com.github.wallev.maidsoulkitchen.handler.serializer.rule.ContainerCookRecSerializer;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.world.item.ItemStack;

public class YhcKettleRecipeSerializer extends ContainerCookRecSerializer<KettleRecipe> {
    public YhcKettleRecipeSerializer() {
        super(YHBlocks.KETTLE_RT.get());
    }

    @Override
    public ItemStack getContainer(KettleRecipe recipe) {
        return recipe.getOutputContainer();
    }
}
