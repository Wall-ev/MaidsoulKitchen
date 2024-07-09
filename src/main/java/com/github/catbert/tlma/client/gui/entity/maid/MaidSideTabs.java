package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.catbert.tlma.client.gui.widget.button.MaidSideTabButton;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import net.minecraft.network.chat.Component;

//209, 107
// +26, 21, 74
public class MaidSideTabs<T extends AbstractMaidContainer> {
    private final int entityId;
    private final int rightPos;
    private final int topPos;

    public MaidSideTabs(int entityId, int rightPos, int topPos) {
        this.entityId = entityId;
        this.rightPos = rightPos + 251;
        this.topPos = topPos + 26 + 7;
    }

    public MaidSideTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        int i = 0;
        MaidSideTabButton setting = new MaidSideTabButton(rightPos, topPos, 107, (b) -> {
//                NetworkHandler.CHANNEL.sendToServer(new ToggleTabMessage(entityId, TabIndex.MAIN))
        }, Component.translatable("gui.touhou_little_maid_addon.btn.task_setting"));
//        if (screen instanceof IBackpackContainerScreen) {
//            setting.active = false;
//        }
        i++;

        MaidSideTabButton book = new MaidSideTabButton(rightPos, topPos + i * 25, 107 + i * 25, (b) -> {
//                NetworkHandler.CHANNEL.sendToServer(new ToggleTabMessage(entityId, TabIndex.CONFIG)));
        }, Component.translatable("gui.touhou_little_maid_addon.btn.task_book"));
//        if (screen instanceof MaidConfigContainerGui) {
//            book.active = false;
//        }
        i++;

        MaidSideTabButton info = new MaidSideTabButton(rightPos, topPos + i * 25, 107 + i * 25, (b) -> {
//                NetworkHandler.CHANNEL.sendToServer(new ToggleTabMessage(entityId, TabIndex.CONFIG)));
        }, Component.translatable("gui.touhou_little_maid_addon.btn.task_info"));
//        if (screen instanceof MaidConfigContainerGui) {
//            info.active = false;
//        }
        i++;

        return new MaidSideTabButton[]{setting, book, info};
    }
}
