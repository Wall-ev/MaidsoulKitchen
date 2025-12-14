package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.BaubleContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class RenderSlotHighEventModern {

    @SubscribeEvent
    public void renderSlotHigh(MaidContainerGuiEvent.Render event) {
        renderSlotHighlight(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
        SlotRenderAndTipsHandler.renderTips(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
    }

    public static void renderSlotHighlight(AbstractMaidContainerGui<?> gui, GuiGraphics graphics, int guiLeft, int guiTop) {
        if (!(gui instanceof IBackpackContainerScreen iBackpackContainerScreen))
            return;
        if (gui instanceof BaubleContainerScreen)
            return;
        SlotRenderAndTipsHandler.renderSlotHighlight(gui, graphics, guiLeft, guiTop);
    }
}