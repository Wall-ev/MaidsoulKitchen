package com.github.wallev.maidsoulkitchen.client.event;

import com.github.tartaricacid.touhoulittlemaid.api.event.client.MaidContainerGuiEvent;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.task.MaidTaskConfigGui;
import com.github.tartaricacid.touhoulittlemaid.client.gui.widget.button.MaidTabButton;
import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import com.github.tartaricacid.touhoulittlemaid.util.TipsHelper;
import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.CookTaskManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import static net.minecraft.client.gui.screens.inventory.AbstractContainerScreen.renderSlotHighlight;

@Mod.EventBusSubscriber(modid = MaidsoulKitchen.MOD_ID, value = Dist.CLIENT)
public class RenderSlotHighEvent {

    @SubscribeEvent
    public static void renderSlotHigh(MaidContainerGuiEvent.Render event) {
        AbstractMaidContainerGui<?> gui = event.getGui();
        EntityMaid maid = gui.getMaid();
        AbstractMaidContainer menu = gui.getMenu();

        int guiLeft = gui.getGuiLeft();
        int guiTop = gui.getGuiTop();
        GuiGraphics graphics = event.getGraphics();
        if (menu.getCarried().is(ModItems.CULINARY_HUB.get()) && menu.slots.size() >= 55) {
            final int hubSlotIndex = 55;
//            final int color = new Color(44, 255, 44, 96).getRGB();
            final int color = 1613561644;
            Slot hubSlot = menu.getSlot(hubSlotIndex);
            renderSlotHighlight(graphics, guiLeft + hubSlot.x, guiTop + hubSlot.y, 0, color);
        }

        if (maid.getTask() instanceof TaskCook taskCook && !(gui instanceof MaidTaskConfigGui<?>)) {
            KitchenData taskData = taskCook.getTaskData(maid);
            boolean isIdle = taskData.getCookName().equals(CookTaskManager.getIdleTask().getUid());
            if (!isIdle) {
                return;
            }
            MaidTabButton configButton = new MaidTabButton(guiLeft + 119, guiTop + 5 + 5, 0, "", b -> {});
            TipsHelper.renderTips(graphics, configButton, VComponent.translatable("gui.maidsoulkitchen.to_setting_cook_task"));
        }
    }

}
