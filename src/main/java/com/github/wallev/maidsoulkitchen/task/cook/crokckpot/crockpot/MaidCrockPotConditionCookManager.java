package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidConditionCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;

@TaskClassAnalyzer(TaskInfo.CP_CROCK_POT)
public class MaidCrockPotConditionCookManager extends MaidConditionCookManager<CrockPotCookingRecipe, Integer> {
    public MaidCrockPotConditionCookManager(RecSerializerManager<CrockPotCookingRecipe> recSerializerManager, EntityMaid maid, ICookTask<?, CrockPotCookingRecipe> task, CookBeBase<?> cookBeBase) {
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
