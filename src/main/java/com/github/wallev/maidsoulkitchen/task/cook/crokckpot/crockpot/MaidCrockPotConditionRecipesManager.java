package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidConditionRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;

public class MaidCrockPotConditionRecipesManager extends MaidConditionRecipesManager2<CrockPotCookingRecipe, Integer> {
    public MaidCrockPotConditionRecipesManager(RecSerializerManager<CrockPotCookingRecipe> recSerializerManager, EntityMaid maid, ICookTask<?, CrockPotCookingRecipe> task, CookBeBase<?> cookBeBase) {
        super(recSerializerManager, maid, task, cookBeBase);
    }

    @Override
    protected Integer getRecipeCondition(CrockPotCookingRecipe crockPotCookingRecipe) {
        return crockPotCookingRecipe.getPotLevel();
    }

    @Override
    protected Integer getBeCondition(CookBeBase<?> cookBeBase) {
        return ((CrockPotBlockEntity)cookBeBase.getBe()).getPotLevel();
    }

    @Override
    protected boolean isValid(Integer beCondition, Integer rCondition) {
        return beCondition >= rCondition;
    }
}
