package com.github.wallev.maidsoulkitchen.compat.msm.immortalers_delight.enchantal_cooler;

import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import com.renyigesai.immortalers_delight.block.enchantal_cooler.EnchantalCoolerBlockEntity;
import com.renyigesai.immortalers_delight.init.ImmortalersDelightBlocks;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.jetbrains.annotations.Nullable;

//@ContainerInvRegister(TaskInfo.MSM_IMD_ENCHANTAL_COOLER)
public class ImdEnchantalCoolerInvHandler extends IInvHandlerFactory<EnchantalCoolerBlockEntity> {
    public ImdEnchantalCoolerInvHandler() {
        super(ImmortalersDelightBlocks.ENCHANTAL_COOLER_ENTITY.get());
    }

    @Override
    protected IInvHandler create(EnchantalCoolerBlockEntity blockEntity, @Nullable Direction side) {
        if (side == null) {
            return (IInvHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        }

        switch (side) {
            case UP -> {
                return (IInvHandler) new CombinedInvWrapper(blockEntity.getFuelslot(), blockEntity.getInventory());
            }
            default -> {
                return (IInvHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
            }
        }
    }
}
