package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.init.MkItems;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.client.gui.screens.inventory.AbstractContainerScreen.renderSlotHighlight;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID)
public class RenderSlotHighEvent {

    @SubscribeEvent
    public static void renderSlotHigh(MaidContainerGuiEvent.Render event) {
        AbstractMaidContainerGui<?> gui = event.getGui();
        AbstractMaidContainer menu = gui.getMenu();
        if (menu.getCarried().is(MkItems.CULINARY_HUB.get()) && menu.slots.size() >= 55) {
            final int hubSlotIndex = 55;
//            final int color = new Color(44, 255, 44, 96).getRGB();
            final int color = 1613561644;
            Slot hubSlot = menu.getSlot(hubSlotIndex);
            renderSlotHighlight(event.getGraphics(), hubSlot.x + gui.getGuiLeft(), hubSlot.y + gui.getGuiTop(), 0, color);
        }
    }

}
