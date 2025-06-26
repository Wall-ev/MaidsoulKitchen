package com.github.wallev.maidsoulkitchen.task.cook.crokckpot.crockpot;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

@TaskClassAnalyzer(TaskInfo.CP_CROCK_POT)
public class CrockPotBe extends CookBeBase<CrockPotBlockEntity> {
    public CrockPotBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof CrockPotBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.getItemHandler();
    }

    @Override
    public int getIngredientSize() {
        return 4;
    }

    @Override
    public int getResultSlot() {
        return 5;
    }

    @Override
    public boolean recMatch() {
        return this.recMatch(be.getRecipeWrapper(), (c) -> {
            return CrockPotCookingRecipe.getRecipeFor(c, serverLevel);
        });
    }

    @Override
    public boolean cookStateMatch() {
        return be.isBurning();
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }

    @Override
    public int activeItemSlot() {
        return 4;
    }

    @Override
    public List<ItemStack> contActiveItemStacks() {
        return CrockPotRecSerializerManager.getInstance().getFuels();
    }
}
