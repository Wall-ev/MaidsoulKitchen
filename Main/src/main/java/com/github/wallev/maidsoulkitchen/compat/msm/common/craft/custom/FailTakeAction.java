package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.InvHandlersHelper;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.jetbrains.annotations.Nullable;
import studio.fantasyit.maid_storage_manager.craft.context.common.CommonTakeItemAction;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.util.InvUtil;

import java.util.function.Function;

public class FailTakeAction extends CommonTakeItemAction implements IFailGuideUseActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.ofMod("fail_take");

    private TakerInteractHelper takerInteractHelper;
    public FailTakeAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        Result start = super.start();
        if (start != Result.FAIL) {
            takerInteractHelper = new TakerInteractHelper(
                    craftGuideStepData.getStorage().pos,
                    craftGuideStepData.getStorage().side,
                    (ServerLevel) maid.level
            );
        }
        return start;
    }

    @Override
    public Result tick() {
        return this.take();
    }

    public Result take() {
        Function<ItemStack, ItemStack> taker = itemStack -> {
            int toTakeCount = itemStack.getCount();
            ItemStack takenItem = itemStack.copyWithCount(toTakeCount);
            ItemStack itemStack1 = InvUtil.tryPlace(maid.getAvailableInv(false), takenItem);
            takenItem.shrink(itemStack1.getCount());
            return itemStack.copyWithCount(itemStack.getCount() - takenItem.getCount());
        };
        this.takerInteractHelper.takeItemTick(taker);

        return Result.SUCCESS;
    }

    public static class TakerInteractHelper {
        final public BlockPos target;
        final ServerLevel level;

        @Nullable
        final BlockEntity blockEntity;
        @Nullable
        public IInvHandler iInvHandler;
        int currentSlot = 0;
        int countPreTick = 10;

        public TakerInteractHelper(BlockPos targetPos, @Nullable Direction side, ServerLevel level) {
            this.target = targetPos;
            this.level = level;
            this.blockEntity = level.getBlockEntity(target);

            if (blockEntity != null) {
                BlockEntityType<?> type = blockEntity.getType();
                IInvHandlerFactory<?> invHandlerFactory = InvHandlersHelper.get(type);

                if (invHandlerFactory != null) {
                    iInvHandler = invHandlerFactory.createInv(blockEntity, side);

                    if (iInvHandler.kl$getSlots() > 60)
                        countPreTick = iInvHandler.kl$getSlots() / 6;
                } else {
                    iInvHandler = (IInvHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, side).orElse(null);

                    if (iInvHandler != null && iInvHandler.kl$getSlots() > 60) {
                        countPreTick = iInvHandler.kl$getSlots() / 6;
                    }
                }
            }
        }

        protected boolean isStillValid() {
            if (blockEntity == null || iInvHandler == null) return false;
            return !blockEntity.isRemoved();
        }

        public void takeItemTick(Function<ItemStack, ItemStack> cb) {
            if (iInvHandler == null) return;
            for (; currentSlot < iInvHandler.kl$getSlots(); currentSlot++) {
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
            }
        }
    }
}
