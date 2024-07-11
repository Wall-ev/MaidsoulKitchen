package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.api.task.IAddonFarmTask;
import com.github.catbert.tlma.api.task.v1.cook.ICookTask;
import com.github.catbert.tlma.api.task.v1.farm.ICompatFarm;
import com.github.catbert.tlma.client.gui.entity.maid.cook.ILittleMaidTaskSettingScreen;
import com.github.catbert.tlma.client.gui.mod.PatchouliScreen;
import com.github.catbert.tlma.client.gui.widget.button.MaidSideTabButton;
import com.github.catbert.tlma.entity.passive.SideTabIndex;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
import com.github.catbert.tlma.task.TaskFeedAndDrinkOwner;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import net.minecraft.network.chat.Component;

import java.util.List;

//209, 107
// +26, 21, 74
public class MaidSideTabs<T extends AbstractMaidContainer> {
    private final int entityId;
    private final int rightPos;
    private final int topPos;

    public MaidSideTabs(int entityId, int rightPos, int topPos) {
        this.entityId = entityId;
        this.rightPos = rightPos + 251;
        this.topPos = topPos + 28 + 9;
    }

    private void settingBtnPress(AbstractMaidContainerGui<T> screen) {
        IMaidTask task = screen.getMaid().getTask();

        if (task instanceof ICookTask<?, ?>) {
            NetworkHandler.CHANNEL.sendToServer(new ToggleSideTabMessage(entityId, SideTabIndex.SETTING.getIndex()));
        } else if (task instanceof ICompatFarm<?>) {

        } else if (task instanceof IAddonFarmTask) {

        } else if (task instanceof TaskFeedAndDrinkOwner) {

        } else if (task instanceof ILittleMaidTask) {

        } else {

        }
    }

    public MaidSideTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        int i = 0, startY = 107, spacing = 25;
        MaidSideTabButton setting = new MaidSideTabButton(rightPos, topPos, startY, (b) -> settingBtnPress(screen), List.of(Component.translatable("gui.touhou_little_maid_addon.btn.task_setting"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_setting.desc"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_warn_text")));
        if (screen instanceof ILittleMaidTaskSettingScreen) {
            setting.active = false;
        }
        i++;

        MaidSideTabButton book = new MaidSideTabButton(rightPos, topPos + i * spacing, startY + i * spacing, (b) -> {
            PatchouliScreen.open();
        }, List.of(Component.translatable("gui.touhou_little_maid_addon.btn.task_book"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_book.desc"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_warn_text")));
        i++;

        MaidSideTabButton info = new MaidSideTabButton(rightPos, topPos + i * spacing, startY + i * spacing, (b) -> {
//                NetworkHandler.CHANNEL.sendToServer(new ToggleTabMessage(entityId, TabIndex.CONFIG)));
        }, List.of(Component.translatable("gui.touhou_little_maid_addon.btn.task_info"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_info.desc"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_warn_text")));
//        if (screen instanceof MaidConfigContainerGui) {
//            info.active = false;
//        }
        i++;

        return new MaidSideTabButton[]{setting, book, info};
    }
}
