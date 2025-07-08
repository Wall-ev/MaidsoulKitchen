package com.github.wallev.maidsoulkitchen.task.cook.copperpot.cooking;

import com.davigj.copperpot.common.block.entity.CopperPotBlockEntity;
import com.davigj.copperpot.core.registry.CPBlocks;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FdPotCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

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
        return CookTask.CP_COPPER_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return CPBlocks.COPPER_POT.get().asItem().getDefaultInstance();
    }
}
