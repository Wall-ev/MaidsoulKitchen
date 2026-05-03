package com.github.wallev.maidsoulkitchen.compat.msm.youkaishomecoming.mokapot;

import com.github.wallev.maidsoulkitchen.compat.msm.common.inv.IInvHandlerFactory;
import com.github.wallev.maidsoulkitchen.task.cook.common.cook.inv.IInvHandler;
import dev.xkmc.youkaishomecoming.content.pot.moka.MokaMakerBlockEntity;
import dev.xkmc.youkaishomecoming.init.registrate.YHBlocks;
import net.minecraft.core.Direction;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.wrapper.RangedWrapper;
import org.jetbrains.annotations.Nullable;

public class MokaPotInvFactory extends IInvHandlerFactory<MokaMakerBlockEntity> {
    public MokaPotInvFactory() {
        super(YHBlocks.MOKA_BE.get());
    }

    @Override
    protected IInvHandler create(MokaMakerBlockEntity blockEntity, @Nullable Direction side) {
        if (side == null) {
            return (IInvHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
        }

        return switch (side) {
            case UP -> (IInvHandler) new RangedWrapper(blockEntity.getInventory(), 0, 4);
            case DOWN -> (IInvHandler) new RangedWrapper(blockEntity.getInventory(), 6, 7);
            default -> (IInvHandler) new RangedWrapper(blockEntity.getInventory(), 5, 6);
        };
    }
}
