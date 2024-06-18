package com.catbert.tlma.task.cook.v1.common;

import com.catbert.tlma.api.task.v1.bestate.IBaseCookLdBe;
import com.catbert.tlma.api.task.v1.cook.IBaseContainerPotCook;
import com.catbert.tlma.api.task.v1.cook.ITaskCook;
import com.catbert.tlma.task.cook.handler.v2.MaidRecipesManager;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import satisfy.beachparty.recipe.TikiBarRecipe;
import satisfy.beachparty.registry.RecipeRegistry;

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
