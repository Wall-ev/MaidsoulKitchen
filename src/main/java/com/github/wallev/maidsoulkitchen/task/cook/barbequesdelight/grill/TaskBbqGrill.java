package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.BD_GRILL)
public class TaskBbqGrill extends ICookTask<GrillBlockEntity, GrillingRecipe<?>> {
    @Override
    protected AbstractCookRule<GrillBlockEntity, GrillingRecipe<?>> createCookRule() {
        return GrillCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<GrillingRecipe<?>> createRecSerializerManager() {
        return GrillingRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<GrillBlockEntity> createCookBe(EntityMaid maid) {
        return new GrillBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.BD_GRILL.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BBQDBlocks.GRILL.asStack();
    }
}
