package com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.candlelight.core.block.entity.LargeCookingPotBlockEntity;
import net.satisfy.candlelight.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.Index.DCL_COOKING_POT;

@AutoCookTaskRegister(TaskInfo.DCL_COOKING_POT)
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
        return TaskInfo.DCL_COOKING_POT.getUid();
    }

    @Override
    protected Index indexEnum() {
        return DCL_COOKING_POT;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.COOKING_POT.get().asItem().getDefaultInstance();
    }
}
