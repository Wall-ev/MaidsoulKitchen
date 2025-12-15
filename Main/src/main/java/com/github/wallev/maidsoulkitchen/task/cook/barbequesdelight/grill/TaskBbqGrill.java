package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;
import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import com.mao.barbequesdelight.content.recipe.GrillingRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import static com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask.Index.BD_GRILL;

@AutoCookTaskRegister(TaskInfo.BD_GRILL)
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
        return TaskInfo.BD_GRILL.getUid();
    }

    @Override
    protected Index indexEnum() {
        return BD_GRILL;
    }



    @Override
    public ItemStack getIcon() {
        return BBQDBlocks.GRILL.asStack();
    }
}
