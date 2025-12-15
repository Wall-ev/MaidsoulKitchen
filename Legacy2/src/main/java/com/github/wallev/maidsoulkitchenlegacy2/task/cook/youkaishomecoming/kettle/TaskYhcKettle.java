package com.github.wallev.maidsoulkitchenlegacy2.task.cook.youkaishomecoming.kettle;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.legacy.task.AutoLegacyCookTaskRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.WaterFdPotCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.kettle.KettleRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@AutoLegacyCookTaskRegister(TaskInfo.YHC_TEA_KETTLE_LEGACY)
@TaskClassAnalyzer(TaskInfo.YHC_TEA_KETTLE)
public class TaskYhcKettle extends ICookTask<KettleBlockEntity, KettleRecipe> {
    @Override
    protected AbstractCookRule<KettleBlockEntity, KettleRecipe> createCookRule() {
        return WaterFdPotCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<KettleRecipe> createRecSerializerManager() {
        return KettleRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<KettleBlockEntity> createCookBe(EntityMaid maid) {
        return new KettleBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.YHC_TEA_KETTLE.getUid();
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.KETTLE.asStack();
    }
}
