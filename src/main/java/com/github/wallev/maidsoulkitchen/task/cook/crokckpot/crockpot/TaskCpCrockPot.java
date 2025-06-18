package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.CookData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.MaidCookManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.AbstractCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.cook.FuelCookRule;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.RecSerializerManager;
import com.sihenzhang.crockpot.block.CrockPotBlocks;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TaskCpCrockPot extends ICookTask<CrockPotBlockEntity, CrockPotCookingRecipe> {

    @Override
    protected CookBeBase<CrockPotBlockEntity> createCookBe(EntityMaid maid) {
        return new CrockPotBe(maid);
    }

    @Override
    protected AbstractCookRule<CrockPotBlockEntity, CrockPotCookingRecipe> createCookRule() {
        return FuelCookRule.getInstance();
    }

    @Override
    protected RecSerializerManager<CrockPotCookingRecipe> createRecSerializerManager() {
        return CrockPotRecSerializerManager.getInstance();
    }

    @Override
    protected MaidCookManager<CrockPotCookingRecipe> createRecipesManager(EntityMaid maid, CookBeBase<CrockPotBlockEntity> cookBe) {
        return new MaidCrockPotConditionCookManager(recSerializerManager, maid, this, cookBe);
    }

    @Override
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.CP_CROCK_POT;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.CP_CROCK_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return CrockPotBlocks.CROCK_POT.get().asItem().getDefaultInstance();
    }
}
