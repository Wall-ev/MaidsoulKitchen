package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@OnlyIn(Dist.CLIENT)
public class RenderSlotHighEventLegacy {

    @SubscribeEvent
    public void renderSlotHigh(MaidContainerGuiEvent.Render event) {
        SlotRenderAndTipsHandler.renderSlotHighlight(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
        SlotRenderAndTipsHandler.renderTips(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
    }

}