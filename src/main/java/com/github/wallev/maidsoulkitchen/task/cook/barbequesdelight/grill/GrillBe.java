package com.github.wallev.maidsoulkitchen.task.cook.barbequesdelight.grill;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.be.CookBeBase;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClassAnalyzer;
import com.mao.barbequesdelight.content.block.GrillBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;

@TaskClassAnalyzer(TaskInfo.BD_GRILL)
public class GrillBe extends CookBeBase<GrillBlockEntity> {
    public GrillBe(EntityMaid maid) {
        super(maid);
    }

    @Override
    public boolean isCookBe(BlockEntity be) {
        return be instanceof GrillBlockEntity;
    }

    @Override
    public IInvHandler getInv() {
        return null;
    }

    @Override
    public int getIngredientSize() {
        return 0;
    }

    @Override
    public int getResultSlot() {
        return 0;
    }

    @Override
    public boolean recMatch() {
        return false;
    }

    @Override
    public boolean cookStateMatch() {
        return be.isHeated();
    }

    @Override
    public void markChanged() {
        be.inventoryChanged();
    }
}
