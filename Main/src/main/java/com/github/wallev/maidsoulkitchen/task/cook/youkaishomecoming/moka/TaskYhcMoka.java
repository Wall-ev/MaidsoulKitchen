package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.moka;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FdPotCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMakerBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@AutoCookTaskRegister(TaskInfo.YHC_MOKA)
@TaskClassAnalyzer(TaskInfo.YHC_MOKA)
public class TaskYhcMoka extends ICookTask<MokaMakerBlockEntity, MokaRecipe> {

    @Override
    protected CookBeBase<MokaMakerBlockEntity> createCookBe(EntityMaid maid) {
        return new MokaBe(maid);
    }

    @Override
    protected AbstractCookRule<MokaMakerBlockEntity, MokaRecipe> createCookRule() {
        return FdPotCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<MokaRecipe> createRecSerializerManager() {
        return MokaRecSerializerManager.getInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.YHC_MOKA.getUid();
    }

    @Override
    protected Index indexEnum() {
        return Index.YHC_MOKA;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.MOKA.asStack();
    }
}
