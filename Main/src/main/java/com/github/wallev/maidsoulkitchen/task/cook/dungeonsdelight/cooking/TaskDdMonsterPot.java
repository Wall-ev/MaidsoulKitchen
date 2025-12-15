package com.github.wallev.maidsoulkitchen.task.cook.dungeonsdelight.cooking;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FdPotCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.yirmiri.dungeonsdelight.common.block.entity.MonsterPotBlockEntity;
import net.yirmiri.dungeonsdelight.common.block.entity.container.MonsterPotRecipe;
import net.yirmiri.dungeonsdelight.core.registry.DDBlocks;
import org.jetbrains.annotations.Nullable;

import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;

@AutoCookTaskRegister(TaskInfo.MONSTER_POT)
@TaskClassAnalyzer(TaskInfo.MONSTER_POT)
public class TaskDdMonsterPot extends ICookTask<MonsterPotBlockEntity, MonsterPotRecipe> {

    @Override
    protected CookBeBase<MonsterPotBlockEntity> createCookBe(EntityMaid maid) {
        return new MonsterPotBe(maid);
    }

    @Override
    protected AbstractCookRule<MonsterPotBlockEntity, MonsterPotRecipe> createCookRule() {
        return FdPotCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<MonsterPotRecipe> createRecSerializerManager() {
        return MonsterPotRecSerializerManager.getInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.MONSTER_POT.getUid();
    }

    @Override
    public ItemStack getIcon() {
        return DDBlocks.MONSTER_POT.get().asItem().getDefaultInstance();
    }

    @Override
    protected @Nullable Index indexEnum() {
        return Index.DD_MONSTER_POT;
    }
}
