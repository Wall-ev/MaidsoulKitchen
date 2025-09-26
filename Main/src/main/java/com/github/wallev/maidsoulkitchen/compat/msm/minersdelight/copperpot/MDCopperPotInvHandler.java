package com.github.wallev.maidsoulkitchen.compat.msm.minersdelight.copperpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.sammy.minersdelight.content.block.copper_pot.CopperPotBlockEntity;
import com.sammy.minersdelight.setup.MDBlockEntities;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

@InvHandlerRegister(TaskInfo.MD_COOK_POT)
public class MDCopperPotInvHandler extends IInvHandlerFactory<CopperPotBlockEntity> {
    public MDCopperPotInvHandler() {
        super(MDBlockEntities.COPPER_POT.get());
    }

    @Override
    protected IInvHandler create(CopperPotBlockEntity blockEntity, @Nullable Direction side) {
        return createForFdPot(blockEntity, blockEntity.getInventory(), side);
    }
}
