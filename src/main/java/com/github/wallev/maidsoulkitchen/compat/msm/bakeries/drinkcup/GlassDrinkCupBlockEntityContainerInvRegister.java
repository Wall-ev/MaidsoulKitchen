package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.drinkcup;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.renyigesai.bakeries.block.glass_drink_cup.GlassDrinkCupBlockEntity;
import com.renyigesai.bakeries.init.BakeriesBlocks;
import net.minecraft.core.Direction;
import org.jetbrains.annotations.Nullable;

@InvHandlerRegister(TaskInfo.MSM_BAKERY_DRINK_CUP)
public class GlassDrinkCupBlockEntityContainerInvRegister extends IInvHandlerFactory<GlassDrinkCupBlockEntity> {

    public GlassDrinkCupBlockEntityContainerInvRegister() {
        super(BakeriesBlocks.DRINK_CUP_ENTITY.get());
    }

    @Override
    protected IInvHandler create(GlassDrinkCupBlockEntity blockEntity, @Nullable Direction side) {
        return (IInvHandler) blockEntity.getInventory();
    }
}
