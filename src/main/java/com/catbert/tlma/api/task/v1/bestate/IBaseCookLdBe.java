package com.catbert.tlma.api.task.v1.bestate;

import com.catbert.tlma.api.ILdCbeAccessor;
import com.catbert.tlma.api.task.v1.cook.IContainerCookBe;
import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBaseCookLdBe<B extends BlockEntity & ImplementedInventory, R extends Recipe<? extends Container>> extends IContainerCookBe<B> {

    @SuppressWarnings("unchecked")
    default boolean canCook(B be, R recipe){
        return ((ILdCbeAccessor<B, R>) be).canCraft$tlma(recipe, be.getLevel().registryAccess());
    }

}
