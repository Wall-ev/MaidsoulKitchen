package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import net.minecraft.client.gui.GuiGraphics;

public class RenderSlotHighEventModern {

    public static void renderSlotHighlight(AbstractMaidContainerGui<?> gui, GuiGraphics graphics, int guiLeft, int guiTop) {
        if (!(gui instanceof IBackpackContainerScreen iBackpackContainerScreen))
            return;
        SlotRenderAndTipsHandler.renderSlotHighlight(gui, graphics, guiLeft, guiTop);
    }
}