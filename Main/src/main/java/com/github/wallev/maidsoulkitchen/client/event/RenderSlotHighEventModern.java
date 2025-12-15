package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.BaubleContainerScreen;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.backpack.IBackpackContainerScreen;
import com.github.wallev.maidsoulkitchen.lib.auto.event.AutoEventSubscriber;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@AutoEventSubscriber(mods = Mods.TLM_SLOT_MODERN, value = Dist.CLIENT)
public class RenderSlotHighEventModern {

    @SubscribeEvent
    public static void renderSlotHigh(MaidContainerGuiEvent.Render event) {
        SlotRenderAndTipsHandler.renderTips(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
    }

    @SubscribeEvent
    public void renderSlotHigh1(MaidContainerGuiEvent.Render event) {
        renderSlotHighlight(event.getGui(), event.getGraphics(), event.getLeftPos(), event.getTopPos());
    }

    public static void renderSlotHighlight(AbstractMaidContainerGui<?> gui, GuiGraphics graphics, int guiLeft, int guiTop) {
        if (!(gui instanceof IBackpackContainerScreen iBackpackContainerScreen))
            return;
        if (gui instanceof BaubleContainerScreen)
            return;
        SlotRenderAndTipsHandler.renderSlotHighlight(gui, graphics, guiLeft, guiTop);
    }
}
