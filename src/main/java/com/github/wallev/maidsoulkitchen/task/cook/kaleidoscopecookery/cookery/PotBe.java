package com.github.wallev.maidsoulkitchen.task.cook.kaleidoscopecookery.cookery;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.ysbbbbbb.kaleidoscopecookery.block.StoveBlock;
import com.github.ysbbbbbb.kaleidoscopecookery.block.entity.PotBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class PotBe extends CookBeBase<PotBlockEntity> {
    public PotBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof PotBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return (IInvHandler) be;
    }

    @Override
    public int getIngredientSize() {
        return 9;
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public ItemStack getResult() {
        return be.getResult();
    }

    @Override
    public boolean recMatch() {
        return this.hasInputs();
    }

    @Override
    public boolean cookStateMatch() {
        return this.isHit();
    }

    public boolean isHit() {
        Level level = be.getLevel();
        assert level != null;
        BlockPos blockPos = be.getBlockPos();
        return level.getBlockState(blockPos.below()).getOptionalValue(BlockStateProperties.LIT).orElse(false);
    }

    public boolean canFlitByItem() {
        Level level = be.getLevel();
        assert level != null;
        BlockPos blockPos = be.getBlockPos();
        Block block = level.getBlockState(blockPos.below()).getBlock();
        if (block instanceof StoveBlock) {
            return true;
        }
        return false;
    }

    @Override
    public void markChanged() {
        be.refresh();
    }
}
