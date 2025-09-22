package com.github.wallev.maidsoulkitchen.compat.msm.kitchenkarrot.shaker;

import com.github.wallev.maidsoulkitchen.compat.msm.common.craft.custom.menu.IWrapMenu;
import io.github.tt432.kitchenkarrot.menu.ShakerMenu;
import io.github.tt432.kitchenkarrot.registries.ModMenuTypes;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ShakeMenuWrap extends IWrapMenu<ShakerMenu> {
    public static final ResourceLocation ID = ModMenuTypes.SHAKER.getId();

    public ShakeMenuWrap() {
        super(ID);
    }

    @Override
    public boolean isValidSlot(@Nullable Direction direction, int slot) {
        if (direction == null) {
            return true;
        }

        return switch (direction) {
            case UP -> slot >= 0 && slot < 5;
            case DOWN -> slot == 11;
            default -> slot >= 5 && slot < 11;
        };
    }
}
