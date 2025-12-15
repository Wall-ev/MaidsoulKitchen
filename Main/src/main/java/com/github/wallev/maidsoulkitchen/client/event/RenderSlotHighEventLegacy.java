package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoEventSubscriber;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@AutoEventSubscriber(mods = Mods.TLM_SLOT_LEGACY, value = Dist.CLIENT)
public class RenderSlotHighEventLegacy {

    @SubscribeEvent
    public static void renderSlotHigh(MaidContainerGuiEvent.Render event) {
        SlotRenderAndTipsHandler.renderSlotHighlight(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
        SlotRenderAndTipsHandler.renderTips(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
    }

}
