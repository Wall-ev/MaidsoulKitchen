package com.github.wallev.maidsoulkitchen.compat.msm.common.inv;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.items.wrapper.SidedInvWrapper;

import javax.annotation.Nullable;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public abstract class WorldlyContainerInvHandlerFactory<B extends BlockEntity & WorldlyContainer> extends IInvHandlerFactory<B> {
    public WorldlyContainerInvHandlerFactory(BlockEntityType<B> type) {
        super(type);
    }

    @Override
    protected IInvHandler create(B blockEntity, @Nullable Direction side) {
        return (IInvHandler) new SidedInvWrapper(blockEntity, side);
    }
}
