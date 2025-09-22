package com.github.wallev.maidsoulkitchen.compat.msm.bakeries.blender;


import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlerRegister;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.renyigesai.bakeries.api.block.WrappedHandler;
import com.renyigesai.bakeries.block.blender.BlenderBlockEntity;
import com.renyigesai.bakeries.init.BakeriesBlocks;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

@InvHandlerRegister(TaskInfo.MSM_BAKERIES_BLENDER)
public class BlenderBlockEntityContainerInvRegister extends IInvHandlerFactory<BlenderBlockEntity> {

    public BlenderBlockEntityContainerInvRegister() {
        super(BakeriesBlocks.BLENDER_ENTITY.get());
    }

    @Override
    protected IInvHandler create(BlenderBlockEntity blockEntity, @Nullable Direction side) {
        if (side == null) {
            return IInvHandler.cast(new CombinedInvWrapper(blockEntity.getInventory()));
        }

        return switch (side) {
            case UP -> IInvHandler.cast(new CombinedInvWrapper(blockEntity.getInventory()));
            case DOWN -> IInvHandler.cast(blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side)
                    .orElseThrow(() -> new IllegalStateException("BlenderBlockEntity must have item handler capability")));
            default -> IInvHandler.cast(new WrappedHandler(blockEntity.getInventory(), (i) -> false, (i, s) -> i == 9));
        };
    }
}
