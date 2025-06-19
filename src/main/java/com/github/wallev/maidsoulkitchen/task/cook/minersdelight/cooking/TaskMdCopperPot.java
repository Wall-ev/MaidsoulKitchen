package com.github.wallev.maidsoulkitchen.task.cook.minersdelight.cooking;

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
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.setup.MDBlocks;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;

public class TaskMdCopperPot extends ICookTask<CopperPotBlockEntity, CookingPotRecipe> {

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
    public TaskDataKey<CookData> getCookDataKey() {
        return DataRegister.MD_COPPER_POT;
    }

    @Override
    public ResourceLocation getUid() {
        return TaskInfo.MD_COOK_POT.uid;
    }

    @Override
    public ItemStack getIcon() {
        return MDBlocks.COPPER_POT.asStack();
    }
}
