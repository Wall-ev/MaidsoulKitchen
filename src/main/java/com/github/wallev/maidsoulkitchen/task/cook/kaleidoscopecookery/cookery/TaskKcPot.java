package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.PotBlockEntity;
import com.github.ysbbbbbb.kaleidoscopecookery.init.ModItems;
import com.github.ysbbbbbb.kaleidoscopecookery.recipe.PotRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

@TaskClassAnalyzer(TaskInfo.KC_POT)
public class TaskKcPot extends ICookTask<PotBlockEntity, PotRecipe> {
    @Override
    protected AbstractCookRule<PotBlockEntity, PotRecipe> createCookRule() {
        return PotCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<PotRecipe> createRecSerializerManager() {
        return PotRecSerializerManager.getInstance();
    }

    @Override
    protected CookBeBase<PotBlockEntity> createCookBe(EntityMaid maid) {
        return new PotBe(maid);
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.KC_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.POT.get().getDefaultInstance();
    }
}
