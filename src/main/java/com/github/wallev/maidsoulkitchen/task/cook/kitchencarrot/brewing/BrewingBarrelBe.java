package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.brewing;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import io.github.tt432.kitchenkarrot.blockentity.BrewingBarrelBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class BrewingBarrelBe extends CookBeBase<BrewingBarrelBlockEntity> {
    public BrewingBarrelBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof BrewingBarrelBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return IInvHandler.EMPTY;
    }

    @Override
    public IInvHandler getIngredientInv() {
        return (IInvHandler) be.input;
    }

    @Override
    public int getIngredientSize() {
        return 6;
    }

    @Override
    public IInvHandler getResultInv() {
        return (IInvHandler) be.result();
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    protected List<ItemStack> contActiveItemStacks() {
        return KkBrewingBarrelRecSerializerManager.getInstance().getFuels();
    }

    @Override
    public boolean recMatch() {
        return this.recMatchAccessor();
    }

    @Override
    public boolean cookStateMatch() {
        // @todo
        return false;
//        return be.hasEnoughWater();
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }


    @Override
    public boolean hasFluid() {
        // @todo
        return false;
//        return be.hasEnoughWater();
    }
}
