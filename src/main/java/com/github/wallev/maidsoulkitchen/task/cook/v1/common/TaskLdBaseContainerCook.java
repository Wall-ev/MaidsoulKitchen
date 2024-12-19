package com.github.wallev.maidsoulkitchen.task.cook.v1.common;

import com.github.wallev.maidsoulkitchen.task.cook.v1.common.bestate.IBaseCookLdBe;
import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public abstract class TaskLdBaseContainerCook<B extends BlockEntity & ImplementedInventory, R extends Recipe<? extends Container>> extends TaskBaseContainerCook<B, R> implements IBaseCookLdBe<B, R> {

    @Override
    @SuppressWarnings({"unchecked", "rawtypes"})
    public boolean beInnerCanCook(Container inventory, B be) {
        Level world = be.getLevel();
        if (world == null) return false;
        R recipe = (R) world.getRecipeManager()
                .getRecipeFor((RecipeType)getRecipeType(), be, world)
                .orElse(null);
        return canCook(be, recipe);
    }
}
