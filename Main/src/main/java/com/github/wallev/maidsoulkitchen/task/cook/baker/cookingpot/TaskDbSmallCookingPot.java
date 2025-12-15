package com.github.wallev.maidsoulkitchen.task.cook.baker.cookingpot;

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
import net.satisfy.bakery.core.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.Index.DBK_COOKING_POT;

@AutoCookTaskRegister(TaskInfo.DBK_COOKING_POT)
@TaskClassAnalyzer(TaskInfo.DBK_COOKING_POT)
public class TaskDbSmallCookingPot extends ICookTask<SmallCookingPotBlockEntity, CookingPotRecipe> {
    @Override
    protected AbstractCookRule<SmallCookingPotBlockEntity, CookingPotRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CookingPotRecipe> createRecSerializerManager() {
        return SmallCookingPotRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<SmallCookingPotBlockEntity> createCookBe(EntityMaid maid) {
        return new SmallCookingPotCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.DBK_COOKING_POT.getUid();
    }

    @Override
    protected Index indexEnum() {
        return DBK_COOKING_POT;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.SMALL_COOKING_POT.get().asItem().getDefaultInstance();
    }
}
