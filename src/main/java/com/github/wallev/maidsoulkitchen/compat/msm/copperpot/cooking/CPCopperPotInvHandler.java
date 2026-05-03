package com.github.wallev.maidsoulkitchen.compat.msm.copperpot.cooking;

import com.davigj.copperpot.common.block.entity.CopperPotBlockEntity;
import com.davigj.copperpot.core.registry.CPBlockEntityTypes;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

@InvHandlerRegister(TaskInfo.COPPER_POT)
public class CPCopperPotInvHandler extends IInvHandlerFactory<CopperPotBlockEntity> {
    public CPCopperPotInvHandler() {
        super(CPBlockEntityTypes.COPPER_POT.get());
    }

    @Override
    protected IInvHandler create(CopperPotBlockEntity blockEntity, @Nullable Direction side) {
        return createForFdPot(blockEntity, blockEntity.getInventory(), side);
    }
}
