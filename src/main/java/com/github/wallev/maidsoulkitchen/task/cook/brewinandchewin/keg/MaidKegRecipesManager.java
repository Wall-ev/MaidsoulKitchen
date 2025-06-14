package com.github.wallev.maidsoulkitchen.task.cook.brewinandchewin.keg;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidConditionRecipesManager2;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import umpaz.brewinandchewin.common.block.entity.KegBlockEntity;
import umpaz.brewinandchewin.common.crafting.KegFermentingRecipe;

public class MaidKegRecipesManager extends MaidConditionRecipesManager2<KegFermentingRecipe, Integer> {
    public MaidKegRecipesManager(RecSerializerManager<KegFermentingRecipe> recSerializerManager, EntityMaid maid, ICookTask<?, KegFermentingRecipe> task, CookBeBase<?> cookBeBase) {
        super(recSerializerManager, maid, task, cookBeBase);
    }

    @Override
    protected Integer getRecipeCondition(KegFermentingRecipe kegFermentingRecipe) {
        return kegFermentingRecipe.getTemperature();
    }

    @Override
    protected Integer getBeCondition(CookBeBase<?> cookBeBase) {
        return ((KegBlockEntity) cookBeBase.getBe()).getTemperature();
    }

    @Override
    protected boolean isValid(Integer be, Integer rCondition) {
        return KegBlockEntity.isValidTemp(be, rCondition);
    }
}
