package com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.util.IFailGuideUseActionContext;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.mutable.MutableBoolean;
import org.jetbrains.annotations.Nullable;
import studio.fantasyit.maid_storage_manager.craft.context.AbstractCraftActionContext;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideData;
import studio.fantasyit.maid_storage_manager.craft.data.CraftGuideStepData;
import studio.fantasyit.maid_storage_manager.craft.work.CraftLayer;
import studio.fantasyit.maid_storage_manager.storage.MaidStorage;
import studio.fantasyit.maid_storage_manager.storage.Target;
import studio.fantasyit.maid_storage_manager.storage.base.IMaidStorage;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageContext;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageExtractableContext;
import studio.fantasyit.maid_storage_manager.storage.base.IStorageInteractContext;
import studio.fantasyit.maid_storage_manager.util.InvUtil;
import studio.fantasyit.maid_storage_manager.util.ItemStackUtil;

import java.util.List;
import java.util.function.Function;

public class TakeItemAction extends AbstractCraftActionContext implements IFailGuideUseActionContext {
    public static final ResourceLocation TYPE = VResourceLocation.createMod("fail_take_all");
    protected IStorageContext storageContext;

    public TakeItemAction(EntityMaid maid, CraftGuideData craftGuideData, CraftGuideStepData craftGuideStepData, CraftLayer layer) {
        super(maid, craftGuideData, craftGuideStepData, layer);
    }

    @Override
    public Result start() {
        ServerLevel level = (ServerLevel) maid.level();
        Target target = craftGuideStepData.getStorage();
        @Nullable Target validTarget = MaidStorage.getInstance().isValidTarget(level, maid, target);
        if (validTarget == null) {
            return Result.FAIL;
        }
        @Nullable IMaidStorage storageType = MaidStorage.getInstance().getStorage(validTarget.getType());
        if (storageType == null) {
            return Result.FAIL;
        }
        storageContext = storageType.onStartCollect(level, maid, validTarget);
        if (storageContext == null) {
            return Result.FAIL;
        }
        storageContext.start(maid, level, validTarget);
        return Result.CONTINUE;
    }

    @Override
    public Result tick() {
        if (allDone()) return Result.SUCCESS;
        MutableBoolean hasChange = new MutableBoolean(false);
        List<ItemStack> allItems = craftGuideStepData.getOutput();
        Function<ItemStack, ItemStack> taker = itemStack -> {
            if (itemStack.isEmpty()) {
                return itemStack;
            }

            ItemStack takenItem = itemStack.copyWithCount(itemStack.getCount());
            ItemStack itemStack1 = InvUtil.tryPlace(maid.getAvailableInv(false), takenItem);
            takenItem.shrink(itemStack1.getCount());
            craftLayer.addCurrentStepPlacedCounts(idx, takenItem.getCount());
            if (takenItem.getCount() > 0) hasChange.setTrue();
            return itemStack.copyWithCount(itemStack.getCount() - takenItem.getCount());

        };
        if (storageContext instanceof IStorageExtractableContext isec) {
            if (isec.hasTask())
                isec.tick(taker);
            else
                isec.setExtractByExisting(item -> allItems.stream().anyMatch(i -> ItemStackUtil.isSameInCrafting(i, item)));
        } else if (storageContext instanceof IStorageInteractContext isic) {
            isic.tick(taker);
        }
        if (storageContext.isDone())
            if (craftGuideStepData.isOptional())
                return Result.SUCCESS;
            else {
                storageContext.reset();
                if (storageContext instanceof IStorageExtractableContext isec)
                    isec.clearTask();
                return hasChange.getValue() ? Result.CONTINUE_INTERRUPTABLE : Result.NOT_DONE_INTERRUPTABLE;
            }
        return hasChange.getValue() ? Result.CONTINUE : Result.NOT_DONE;
    }

    @Override
    public void stop() {
        if (storageContext != null) {
            storageContext.finish();
        }
    }


    private boolean allDone() {
        if (craftGuideStepData == null) return false;
        List<ItemStack> items = craftGuideStepData.getOutput();
        for (int i = 0; i < items.size(); i++) {
            if (craftLayer.getCurrentStepCount(i) < items.get(i).getCount()) {
                return false;
            }
        }
        return true;
    }


}
