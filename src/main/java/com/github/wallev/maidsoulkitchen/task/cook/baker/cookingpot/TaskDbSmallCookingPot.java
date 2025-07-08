package com.github.wallev.maidsoulkitchen.task.cook.baker.cookingpot;

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
import net.satisfy.bakery.core.block.entity.SmallCookingPotBlockEntity;
import net.satisfy.bakery.core.registry.ObjectRegistry;
import net.satisfy.farm_and_charm.core.recipe.CookingPotRecipe;

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
        return CookTask.DBK_COOKING_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.SMALL_COOKING_POT.get().asItem().getDefaultInstance();
    }
}
