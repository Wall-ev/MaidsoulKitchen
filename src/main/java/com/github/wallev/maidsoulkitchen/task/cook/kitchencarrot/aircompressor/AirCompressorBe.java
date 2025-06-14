package com.github.wallev.maidsoulkitchen.task.cook.kitchencarrot.aircompressor;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.mixin.kitchkarrot.AirCompressorBlockEntityAccessor;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import io.github.tt432.kitchenkarrot.blockentity.AirCompressorBlockEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.List;

public class AirCompressorBe extends CookBeBase<AirCompressorBlockEntity> {
    public AirCompressorBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof AirCompressorBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return IInvHandler.EMPTY;
    }

    @Override
    public IInvHandler getIngredientInv() {
        return (IInvHandler) be.getInput1();
    }

    @Override
    public int getIngredientSize() {
        return 5;
    }

    @Override
    public IInvHandler activeItemInv() {
        return (IInvHandler) be.getInput2();
    }

    @Override
    public int activeItemSlot() {
        return 0;
    }

    @Override
    public IInvHandler getResultInv() {
        return (IInvHandler) be.getOutput();
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public boolean recMatch() {
        return ((AirCompressorBlockEntityAccessor)be).mk$getRecipeFromItems() != null;
    }

    @Override
    public boolean cookStateMatch() {
        return ((AirCompressorBlockEntityAccessor)be).mk$hasEnergy();
    }

    @Override
    public void markChanged() {
        this.defaultChanged();
    }

    @Override
    protected List<ItemStack> contActiveItemStacks() {
        return AirCompressorRecSerializerManager.getInstance().getFuels();
    }
}
