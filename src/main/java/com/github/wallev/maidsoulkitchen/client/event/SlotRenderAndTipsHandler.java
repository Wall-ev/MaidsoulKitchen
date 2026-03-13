package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;
public class SlotRenderAndTipsHandler {
    static byte HUB_SLOT = (byte) ((Mods.TLM_SLOT_MODERN.versionLoad() ? 42 : 50) + ItemCulinaryHub.INV_SLOT);

    public static void init() {
    }

    public static void renderSlotHighlight(AbstractMaidContainerGui<?> gui, GuiGraphics graphics, int guiLeft, int guiTop) {
        AbstractMaidContainer menu = gui.getMenu();
        if (menu.getCarried().is(MkItems.CULINARY_HUB.get()) && menu.slots.size() > HUB_SLOT) {
            final int hubSlotIndex = HUB_SLOT;
//            final int color = new Color(44, 255, 44, 96).getRGB();
            final int color = 1613561644;
            Slot hubSlot = menu.getSlot(hubSlotIndex);
            AbstractContainerScreen.renderSlotHighlight(graphics, guiLeft + hubSlot.x, guiTop + hubSlot.y, 0, color);
        }
    }

}