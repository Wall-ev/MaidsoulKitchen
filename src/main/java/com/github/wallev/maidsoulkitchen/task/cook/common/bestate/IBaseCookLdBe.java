package com.github.wallev.maidsoulkitchen.task.cook.common.bestate;

import com.github.wallev.maidsoulkitchen.api.task.cook.IContainerCookBe;
import com.github.wallev.maidsoulkitchen.task.cook.common.cbaccessor.ILdCbeAccessor;
import de.cristelknight.doapi.common.world.ImplementedInventory;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface IBaseCookLdBe<B extends BlockEntity & ImplementedInventory, R extends Recipe<? extends Container>> extends IContainerCookBe<B> {

    @SuppressWarnings("unchecked")
    default boolean canCook(B be, R recipe){
        return ((ILdCbeAccessor<B, R>) be).tlmk$canCraft(recipe, be.getLevel().registryAccess());
    }

}
