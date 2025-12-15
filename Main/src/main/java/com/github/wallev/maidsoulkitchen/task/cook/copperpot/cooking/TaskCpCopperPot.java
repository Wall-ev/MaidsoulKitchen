package com.github.wallev.maidsoulkitchen.task.cook.copperpot.cooking;

import com.davigj.copperpot.common.block.entity.CopperPotBlockEntity;
import com.davigj.copperpot.core.registry.CPBlocks;
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
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

import com.github.wallev.maidsoulkitchen.task.cook.common.task.AutoCookTaskRegister;

@AutoCookTaskRegister(TaskInfo.COPPER_POT)
@TaskClassAnalyzer(TaskInfo.COPPER_POT)
public class TaskCpCopperPot extends ICookTask<CopperPotBlockEntity, CookingPotRecipe> {

    @Override
    protected CookBeBase<CopperPotBlockEntity> createCookBe(EntityMaid maid) {
        return new CopperPotBe(maid);
    }

    @Override
    protected AbstractCookRule<CopperPotBlockEntity, CookingPotRecipe> createCookRule() {
        return FdPotCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CookingPotRecipe> createRecSerializerManager() {
        return CopperPotRecSerializerManager.getInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.COPPER_POT.getUid();
    }

    @Override
    public ItemStack getIcon() {
        return CPBlocks.COPPER_POT.get().asItem().getDefaultInstance();
    }

    @Override
    protected @Nullable Index indexEnum() {
        return Index.CP_COPPER_POT;
    }
}
