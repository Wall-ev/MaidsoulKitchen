package com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.stove;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.manager.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FuelCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.satisfy.farm_and_charm.core.block.entity.StoveBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.StoveRecipe;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;

@TaskClassAnalyzer(TaskInfo.DFC_STOVE)
public class TaskDfcStove extends ICookTask<StoveBlockEntity, StoveRecipe> {

    @Override
    protected CookBeBase<StoveBlockEntity> createCookBe(EntityMaid maid) {
        return new StoveCookBe(maid);
    }

    @Override
    protected AbstractCookRule<StoveBlockEntity, StoveRecipe> createCookRule() {
        return FuelCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<StoveRecipe> createRecSerializerManager() {
        return StoveRecSerializerManager.getInstance();
    }

    @Override
    protected MaidCookManager<StoveRecipe> createRecipesManager(EntityMaid maid, CookBeBase<StoveBlockEntity> cookBe) {
        return new MaidCookManager<>(recSerializerManager, maid, this, cookBe);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.DFC_STOVE.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.STOVE.get().asItem().getDefaultInstance();
    }
}
