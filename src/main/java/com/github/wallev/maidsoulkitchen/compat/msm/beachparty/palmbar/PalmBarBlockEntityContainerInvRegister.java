package com.github.wallev.maidsoulkitchen.compat.msm.beachparty.palmbar;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.WorldlyContainerInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.core.Direction;
import net.satisfy.beachparty.core.block.entity.PalmBarBlockEntity;
import net.satisfy.beachparty.core.registry.EntityTypeRegistry;
import org.jetbrains.annotations.Nullable;

@InvHandlerRegister(TaskInfo.MSM_BEARCH_PARTY_PALM_BAR)
public class PalmBarBlockEntityContainerInvRegister extends WorldlyContainerInvHandlerFactory<PalmBarBlockEntity> {

    public PalmBarBlockEntityContainerInvRegister() {
        super(EntityTypeRegistry.PALM_BAR_BLOCK_ENTITY.get());
    }

    @Override
    protected IInvHandler create(PalmBarBlockEntity blockEntity, @Nullable Direction side) {
        if (side == null) {
            return IInvHandler.createSlotLimitInvWrapper(blockEntity, 0, 4);
        }

        return switch (side) {
            case UP -> IInvHandler.createSlotLimitInvWrapper(blockEntity, 1, 4);
            case DOWN -> IInvHandler.createSlotLimitInvWrapper(blockEntity, 0);
            default -> IInvHandler.createSlotLimitInvWrapper(blockEntity, 0, 4);
        };
    }
}
