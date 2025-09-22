package com.github.wallev.maidsoulkitchen.compat.msm.common.storage.item;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlersHelper;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContextContainerHandlerCollect;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContextContainerHandlerStore;
import com.github.wallev.maidsoulkitchen.compat.msm.common.storage.ContextContainerHandlerView;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import studio.fantasyit.maid_storage_manager.storage.Target;
import studio.fantasyit.maid_storage_manager.storage.base.AbstractFilterableBlockStorage;
import studio.fantasyit.maid_storage_manager.storage.base.IMaidStorage;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageContext;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class ItemStackStorage implements IMaidStorage {

    public static final ResourceLocation TYPE = VResourceLocation.createMod("itemstack");

    @Override
    public ResourceLocation getType() {
        return TYPE;
    }

    @Override
    public boolean isValidTarget(ServerLevel level, LivingEntity maid, BlockPos block, @Nullable Direction side, BlockState blockState, BlockEntity blockEntity) {
        return true;
//        if (blockEntity == null) return false;
//
//        BlockEntityType<?> type = blockEntity.getType();
//        return InvHandlersHelper.get(type) != null;
    }

    @Override
    public @Nullable IStorageContext onStartCollect(ServerLevel level, EntityMaid maid, Target storage) {
        return new ContextItemStackCollect();
    }

    @Override
    public @Nullable IStorageContext onStartPlace(ServerLevel level, EntityMaid maid, Target storage) {
        return new ContextItemStackCollect();
    }

    @Override
    public @Nullable IStorageContext onStartView(ServerLevel level, EntityMaid maid, Target storage) {
        return new ContexttItemStackView();
    }

    @Override
    public @Nullable IStorageContext onPreviewFilter(ServerLevel level, EntityMaid maid, Target storage) {
        return new AbstractFilterableBlockStorage();
    }
}