package com.github.wallev.maidsoulkitchen.compat.msm.farmersdelight.cookingpot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;
import vectorwing.farmersdelight.common.block.entity.CookingPotBlockEntity;
import vectorwing.farmersdelight.common.registry.ModBlockEntityTypes;

@InvHandlerRegister(TaskInfo.MSM_FD_COOKING_POT)
public class FdCookingPotInvHandler extends IInvHandlerFactory<CookingPotBlockEntity> {
    public FdCookingPotInvHandler() {
        super(ModBlockEntityTypes.COOKING_POT.get());
    }

    @Override
    protected IInvHandler create(CookingPotBlockEntity blockEntity, @Nullable Direction side) {
        return createForFdPot(blockEntity, blockEntity.getInventory(), side);
    }
}
