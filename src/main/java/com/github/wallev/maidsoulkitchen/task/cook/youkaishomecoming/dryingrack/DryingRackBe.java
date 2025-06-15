package com.github.wallev.maidsoulkitchen.task.cook.youkaishomecoming.dryingrack;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemDefinition;
import com.github.wallev.maidsoulkitchen.task.cook.common.inv.ItemInventory;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidItem;
import com.github.wallev.maidsoulkitchen.task.cook.common.rule.rec.MaidRec;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackBlockEntity;
import dev.xkmc.youkaishomecoming.content.pot.rack.DryingRackRecipe;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

public class DryingRackBe extends CookBeBase<DryingRackBlockEntity> {
    public DryingRackBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof DryingRackBlockEntity && this.canDryingRack(be);
    }

    public boolean canDryingRack() {
        return canDryingRack(be);
    }

    public boolean canDryingRack(BlockEntity be) {
        BlockPos pos = be.getBlockPos();
        Level level = be.getLevel();
        if (level == null) {
            return false;
        }
        return level.canSeeSky(pos) && level.isDay() && !level.isRainingAt(pos);
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be.getInventory();
    }

    @Override
    public int getIngredientSize() {
        return 4;
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public ItemStack getResult() {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean recMatch() {
        if (this.canDryingRack()) {
            for (ItemStack stack : be.getItems()) {
                if (!stack.isEmpty()) {
                    return true;
                }
            }
            return false;
        }
        return false;
    }

    @Override
    public boolean cookStateMatch() {
        return true;
    }

    @Override
    public boolean insertInputs(MaidRec rec, ItemInventory itemInventory) {
        int cookingTime = ((DryingRackRecipe) rec.recipe()).getCookingTime();

        for (MaidItem maidItem : rec.maidItems()) {
            if (!maidItem.isEmpty()) {
                ItemDefinition item = maidItem.item();
                int count = Math.min(maidItem.count(), 4);

                for (ItemStack itemStack : itemInventory.getItemStacks(item)) {
                    if (itemStack.isEmpty()) continue;

                    for (int i = 0; i < count; i++) {
                        be.placeFood(itemStack, cookingTime);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }
}
