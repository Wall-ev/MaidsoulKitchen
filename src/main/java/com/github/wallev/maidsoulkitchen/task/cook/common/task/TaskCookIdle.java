package com.github.wallev.maidsoulkitchen.task.cook.common.task;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.mkrec.MKRecipe;
import com.github.wallev.maidsoulkitchen.util.MemoryUtil;
import com.github.wallev.verhelper.server.ai.VBehaviorControl;
import com.mojang.datafixers.util.Pair;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

import java.util.List;

@SuppressWarnings("all")
public class TaskCookIdle extends ICookTask {
    @Override
    protected AbstractCookRule<?, ?> createCookRule() {
        return null;
    }

    @Override
    protected RecSerializerManager<?> createRecSerializerManager() {
        return null;
    }

    @Override
    protected CookBeBase<?> createCookBe(EntityMaid maid) {
        return null;
    }

    @Override
    public List<Pair<Integer, VBehaviorControl>> vCreateBrainTasks(EntityMaid maid) {
        MemoryUtil.resetCookWorkState(maid);
        return (List) TaskManager.getIdleTask().createBrainTasks(maid);
    }

    @Override
    public List<MKRecipe> getRecipes(EntityMaid maid) {
        return List.of();
    }

    @Override
    public String getRecipeTypeId() {
        return "";
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.IDLE.getUid();
    }

    @Override
    public ItemStack getIcon() {
        return MkItems.CULINARY_HUB.get().getDefaultInstance();
    }
}
