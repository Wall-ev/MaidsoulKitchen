package com.github.wallev.maidsoulkitchen.task.cook.farm_and_charm.roaster;

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
import net.satisfy.farm_and_charm.core.block.entity.RoasterBlockEntity;
import net.satisfy.farm_and_charm.core.recipe.RoasterRecipe;
import net.satisfy.farm_and_charm.core.registry.ObjectRegistry;

@TaskClassAnalyzer(TaskInfo.DFC_ROASTER)
public class TaskDfcRoaster extends ICookTask<RoasterBlockEntity, RoasterRecipe> {
    @Override
    protected AbstractCookRule<RoasterBlockEntity, RoasterRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<RoasterRecipe> createRecSerializerManager() {
        return RoasterRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<RoasterBlockEntity> createCookBe(EntityMaid maid) {
        return new RoasterCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.DFC_ROASTER.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.ROASTER.get().asItem().getDefaultInstance();
    }
}
