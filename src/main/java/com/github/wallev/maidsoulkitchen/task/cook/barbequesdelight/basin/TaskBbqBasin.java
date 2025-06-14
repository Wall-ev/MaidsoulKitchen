package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.basin;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.mao.barbequesdelight.content.block.BasinBlockEntity;
import com.mao.barbequesdelight.content.recipe.SkeweringRecipe;
import com.mao.barbequesdelight.init.registrate.BBQDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TaskBbqBasin extends ICookTask<BasinBlockEntity, SkeweringRecipe<?>> {
    @Override
    protected AbstractCookRule<BasinBlockEntity, SkeweringRecipe<?>> createCookRule() {
        return BasinCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<SkeweringRecipe<?>> createRecSerializerManager() {
        return SkeweringRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<BasinBlockEntity> createCookBe(EntityMaid maid) {
        return new BasinBe(maid);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.BD_BASIN;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.BD_BASIN.uid;
    }

    @Override
    public ItemStack getIcon() {
        return BBQDBlocks.BASIN.asStack();
    }
}
