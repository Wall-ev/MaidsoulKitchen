package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;

import java.awt.*;

import static net.minecraft.client.gui.screens.inventory.AbstractContainerScreen.renderSlotHighlight;

public class RenderHelper {

    public static void renderHubSlotHighlight(AbstractMaidContainerGui<?> gui, GuiGraphics graphics) {
        final int hubSlotIndex = 55;
        final int color = new Color(44, 255, 44, 96).getRGB();
        Slot hubSlot = gui.getMenu().getSlot(hubSlotIndex);
        renderSlotHighlight(graphics, hubSlot.x, hubSlot.y, 0, color);
    }

}
