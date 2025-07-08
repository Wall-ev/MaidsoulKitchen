package com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.candlelight.core.block.entity.LargeCookingPotBlockEntity;
import net.satisfy.candlelight.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;

@TaskClassAnalyzer(TaskInfo.DCL_COOKING_POT)
public class TaskDclCookingPot extends ICookTask<LargeCookingPotBlockEntity, CookingPotRecipe> {
    @Override
    protected AbstractCookRule<LargeCookingPotBlockEntity, CookingPotRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CookingPotRecipe> createRecSerializerManager() {
        return CookingPotRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<LargeCookingPotBlockEntity> createCookBe(EntityMaid maid) {
        return new CookingPotCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.DCL_COOKING_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.COOKING_POT.get().asItem().getDefaultInstance();
    }
}
