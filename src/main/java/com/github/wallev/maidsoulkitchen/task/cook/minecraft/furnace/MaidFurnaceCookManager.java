package com.github.wallev.maidsoulkitchen.task.cook.minecraft.furnace;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidConditionCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.RecipeType;

public class MaidFurnaceCookManager extends MaidConditionCookManager<AbstractCookingRecipe, RecipeType<?>> {
    public MaidFurnaceCookManager(RecSerializerManager<AbstractCookingRecipe> recSerializerManager, EntityMaid maid, ICookTask<?, AbstractCookingRecipe> task, CookBeBase<?> cookBeBase) {
        super(recSerializerManager, maid, task, cookBeBase);
    }

    @Override
    protected RecipeType<?> getRecipeCondition(AbstractCookingRecipe abstractCookingRecipe) {
        return abstractCookingRecipe.getType();
    }

    @Override
    protected RecipeType<?> getBeCondition(CookBeBase<?> cookBeBase) {
        return ((IAbstractFurnaceAccessor) cookBeBase.getBe()).tlmk$getRecipeType();
    }

    @Override
    protected boolean isValid(RecipeType<?> beCondition, RecipeType<?> rCondition) {
        return beCondition.equals(rCondition);
    }

    @Override
    protected boolean recIsValid(MKRecipe<AbstractCookingRecipe> r) {
        return super.recIsValid(r);
    }
}
