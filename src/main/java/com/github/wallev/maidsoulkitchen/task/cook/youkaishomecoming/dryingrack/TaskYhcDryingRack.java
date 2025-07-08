package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.dryingrack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.NormalCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.YHC_DRYING_RACK)
public class TaskYhcDryingRack extends ICookTask<DryingRackBlockEntity, DryingRackRecipe> {
    @Override
    protected AbstractCookRule<DryingRackBlockEntity, DryingRackRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<DryingRackRecipe> createRecSerializerManager() {
        return DryingRackRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<DryingRackBlockEntity> createCookBe(EntityMaid maid) {
        return new DryingRackBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.YHC_DRYING_RACK.uid;
    }

    @Override
    public ItemStack getIcon() {
        return YHBlocks.RACK.asStack();
    }
}
