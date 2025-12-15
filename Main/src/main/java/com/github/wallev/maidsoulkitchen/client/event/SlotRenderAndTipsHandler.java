package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidTabButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.util.TipsHelper;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.item.ItemCulinaryHub;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.Slot;

public class SlotRenderAndTipsHandler {
    static byte HUB_SLOT = (byte) ((Mods.TLM_SLOT_MODERN.versionLoad() ? 42 : 50) + ItemCulinaryHub.INV_SLOT);

    public static void renderTips(AbstractMaidContainerGui<?> gui, GuiGraphics graphics, int guiLeft, int guiTop) {
        EntityMaid maid = gui.getMaid();
        if (!(maid.getTask() instanceof TaskCook taskCook))
            return;
        if (gui instanceof MaidTaskConfigGui<?>)
            return;

        KitchenData taskData = taskCook.getTaskData(maid);
        boolean isIdle = taskData.getCookName().equals(CookTaskManager.getIdleTask().getUid());
        if (!isIdle) {
            return;
        }
        MaidTabButton configButton = new MaidTabButton(guiLeft + 119, guiTop + 5 + 5, 0, "", b -> {});
        TipsHelper.renderTips(graphics, configButton, VComponent.translatable("gui.maidsoulkitchen.to_setting_cook_task"));
    }

    public static void renderSlotHighlight(AbstractMaidContainerGui<?> gui, GuiGraphics graphics, int guiLeft, int guiTop) {
        AbstractMaidContainer menu = gui.getMenu();
        if (menu.getCarried().is(ModItems.CULINARY_HUB.get()) && menu.slots.size() > HUB_SLOT) {
            final int hubSlotIndex = HUB_SLOT;
//            final int color = new Color(44, 255, 44, 96).getRGB();
            final int color = 1613561644;
            Slot hubSlot = menu.getSlot(hubSlotIndex);
            AbstractContainerScreen.renderSlotHighlight(graphics, guiLeft + hubSlot.x, guiTop + hubSlot.y, 0, color);
        }
    }

}
