package com.github.wallev.maidsoulkitchen.compat.msm.common.storage;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlersHelper;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskClassAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.WorldlyContainer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ContainerOpenersCounter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;
import studio.fantasyit.maid_storage_manager.storage.ItemHandler.SimulateTargetInteractHelper;

import java.lang.reflect.Field;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static studio.fantasyit.maid_storage_manager.storage.ItemHandler.SimulateTargetInteractHelper.*;

@TaskClassAnalyzer(TaskInfo.MSM_CORE)
public class SimulateInvTargetInteractHelper {
    final public BlockPos target;
    final ServerLevel level;
    @Nullable
    final BlockEntity blockEntity;
    final Player opener;
    private final EntityMaid maid;
    @Nullable
    public IInvHandler iInvHandler;
    int currentSlot = 0;
    int restTick = 0;
    int countPreTick = 10;
    boolean notOpener = false;

    public SimulateInvTargetInteractHelper(EntityMaid maid, BlockPos targetPos, @Nullable Direction side, ServerLevel level) {
        this.maid = maid;
        this.target = targetPos;
        this.level = level;
        this.blockEntity = level.getBlockEntity(target);
        this.opener = SimulateTargetInteractHelper.ChestOpener.getOrCreate(level, maid);
        if (blockEntity != null) {
            BlockEntityType<?> type = blockEntity.getType();
            IInvHandlerFactory<?> invHandlerFactory = InvHandlersHelper.get(type);

            if (invHandlerFactory != null) {
                iInvHandler = invHandlerFactory.createInv(blockEntity, side);

                if (iInvHandler.kl$getSlots() > 60)
                    countPreTick = iInvHandler.kl$getSlots() / 6;
            }
//            else {
//                iInvHandler = (IInvHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null);
//
//                if (iInvHandler != null && iInvHandler.kl$getSlots() > 60) {
//                    countPreTick = iInvHandler.kl$getSlots() / 6;
//                }
//            }
        }
    }

    protected boolean isStillValid() {
        if (blockEntity == null || iInvHandler == null) return false;
        return !blockEntity.isRemoved();
    }


    private Optional<ContainerOpenersCounter> trySeekCounter(BlockEntity blockEntity) {
        Field[] fields = blockEntity.getClass().getDeclaredFields();
        for (Field field : fields) {
            if (field.getType().equals(ContainerOpenersCounter.class)) {
                try {
                    field.setAccessible(true);
                    return Optional.of((ContainerOpenersCounter) field.get(blockEntity));
                } catch (IllegalAccessException ignore) {
                }
            }
        }
        if (blockEntity instanceof WorldlyContainer wc) {
            return Optional.of(new ContainerOpenersCounter() {
                @Override
                public void decrementOpeners(Player p_155469_, Level p_155470_, BlockPos p_155471_, BlockState p_155472_) {
                    wc.stopOpen(p_155469_);
                }

                @Override
                public void incrementOpeners(Player p_155453_, Level p_155454_, BlockPos p_155455_, BlockState p_155456_) {
                    wc.startOpen(p_155453_);
                }

                @Override
                protected void onOpen(Level p_155460_, BlockPos p_155461_, BlockState p_155462_) {

                }

                @Override
                protected void onClose(Level p_155473_, BlockPos p_155474_, BlockState p_155475_) {
                }

                @Override
                protected void openerCountChanged(Level p_155463_, BlockPos p_155464_, BlockState p_155465_, int p_155466_, int p_155467_) {
                }

                @Override
                protected boolean isOwnContainer(Player p_155451_) {
                    return false;
                }
            });
        }
        return Optional.empty();
    }

    public boolean doneTaking() {
        if (restTick > 0) return false;
        if (!isStillValid()) return true;
        return iInvHandler == null || currentSlot >= iInvHandler.kl$getSlots();
    }

    public boolean doneViewing() {
        return doneTaking();
    }

    public void open() {
        if (blockEntity == null) return;
        maid.swing(InteractionHand.MAIN_HAND);
        if (!isMaidOpening(maid)) {
            trySeekCounter(blockEntity).ifPresent(containerOpenersCounter -> {
                containerOpenersCounter.incrementOpeners(opener,
                        level,
                        target,
                        level.getBlockState(target));
            });
            addCounter(maid, target);
        } else {
            notOpener = true;
        }
        currentSlot = 0;
    }

    public void takeItemTick(Function<ItemStack, ItemStack> cb) {
        if (iInvHandler == null) return;
        int count = 0;
        for (; currentSlot < iInvHandler.kl$getSlots(); currentSlot++) {
            if (++count >= countPreTick) break;
            //可以获取到的物品
            ItemStack copy = iInvHandler.kl$extractItem(currentSlot,
                    iInvHandler.kl$getStackInSlot(currentSlot).getCount(),
                    true).copy();
            int originalCount = copy.getCount();
            if (copy.isEmpty()) continue;
            //获取在处理后剩余的物品数量
            ItemStack result = cb.apply(copy);
            //如果没有变化，则跳过
            if (result.getCount() == originalCount) continue;
            iInvHandler.kl$extractItem(currentSlot, originalCount - result.getCount(), false);
            break;
        }
    }

    public void viewItemTick(Consumer<ItemStack> cb) {
        if (iInvHandler == null) return;
        int count = 0;
        for (; currentSlot < iInvHandler.kl$getSlots(); currentSlot++) {
            if (++count >= countPreTick) break;
            ItemStack stack = iInvHandler.kl$getStackInSlot(currentSlot);
            if (stack.isEmpty()) continue;
            cb.accept(stack);
        }
    }

    public void reset() {
        currentSlot = 0;
    }

    public void stop() {
        if (!notOpener) {
            if (blockEntity != null && opener != null && isStillValid()) {
                trySeekCounter(blockEntity).ifPresent(containerOpenersCounter -> {
                    containerOpenersCounter.decrementOpeners(opener,
                            level,
                            target,
                            level.getBlockState(target));
                });
            }
            removeCounter(maid, target);
        }
    }
}