package com.github.wallev.maidsoulkitchen.task.cook.beachparty.palmbar;

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
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;
import net.satisfy.beachparty.core.recipe.PalmBarRecipe;
import net.satisfy.beachparty.core.registry.ObjectRegistry;

@TaskClassAnalyzer(TaskInfo.DBP_PALM_BAR)
public class TaskDbpPalmBar extends ICookTask<PalmBarBlockEntity, PalmBarRecipe> {
    @Override
    protected AbstractCookRule<PalmBarBlockEntity, PalmBarRecipe> createCookRule() {
        return NormalCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<PalmBarRecipe> createRecSerializerManager() {
        return PalmBarRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<PalmBarBlockEntity> createCookBe(EntityMaid maid) {
        return new PalmBarCookBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.DBP_PALM_BAR.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ObjectRegistry.PALM_BAR.get().asItem().getDefaultInstance();
    }
}
