package com.github.wallev.maidsoulkitchen.task.cook.farmersdelight.cookingpot;

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
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;

@TaskClassAnalyzer(TaskInfo.FD_COOK_POT)
public class TaskFdCookingPot extends ICookTask<CookingPotBlockEntity, CookingPotRecipe> {

    @Override
    protected CookBeBase<CookingPotBlockEntity> createCookBe(EntityMaid maid) {
        return new CookingPotBe(maid);
    }

    @Override
    protected AbstractCookRule<CookingPotBlockEntity, CookingPotRecipe> createCookRule() {
        return FdPotCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CookingPotRecipe> createRecSerializerManager() {
        return CookingPotRecSerializerManager.getInstance();
    }

    @Override
    public ResourceLocation getUid() {
        return CookTask.FD_COOK_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return ModItems.COOKING_POT.get().getDefaultInstance();
    }
}
