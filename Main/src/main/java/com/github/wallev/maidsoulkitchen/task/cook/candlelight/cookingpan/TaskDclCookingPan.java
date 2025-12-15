package com.github.wallev.maidsoulkitchen.task.cook.candlelight.cookingpan;

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
import net.satisfy.candlelight.core.block.entity.CookingPanBlockEntity;
import net.satisfy.candlelight.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.RoasterRecipe;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.Index.DCL_COOKING_PAN;

@AutoCookTaskRegister(TaskInfo.DCL_COOKING_PAN)
@TaskClassAnalyzer(TaskInfo.DCL_COOKING_PAN)
public class TaskDclCookingPan extends ICookTask<CookingPanBlockEntity, RoasterRecipe> {
    @Override
    protected AbstractCookRule<CookingPanBlockEntity, RoasterRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<RoasterRecipe> createRecSerializerManager() {
        return CookingPanRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<CookingPanBlockEntity> createCookBe(EntityMaid maid) {
        return new CookingPanCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DCL_COOKING_PAN.getUid();
    }

    @Override
    protected Index indexEnum() {
        return DCL_COOKING_PAN;
    }



    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.COOKING_PAN.get().asItem().getDefaultInstance();
    }
}
