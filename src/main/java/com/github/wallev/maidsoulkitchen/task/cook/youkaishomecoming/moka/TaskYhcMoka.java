package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.moka;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FdPotCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMakerBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

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
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.YHC_MOKA;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.YHC_MOKA.uid;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.MOKA.asStack();
    }
}
