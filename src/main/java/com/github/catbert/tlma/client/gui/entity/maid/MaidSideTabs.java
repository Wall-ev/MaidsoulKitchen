package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.client.gui.entity.maid.cook.MaidTaskConfigerGui;
import com.github.catbert.tlma.client.gui.mod.PatchouliWarningScreen;
import com.github.catbert.tlma.client.gui.widget.button.MaidSideTabButton;
import com.github.catbert.tlma.entity.passive.SideTabIndex;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import vazkii.patchouli.api.PatchouliAPI;

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

    private void settingBtnPressed(AbstractMaidContainerGui<T> gui) {
        IMaidTask task = gui.getMaid().getTask();

        if (task instanceof ILittleMaidTask) {
            NetworkHandler.CHANNEL.sendToServer(new ToggleSideTabMessage(gui.getMenu().containerId, entityId, SideTabIndex.SETTING.getIndex(), task.getUid()));
        } else {

        }
    }

    public MaidSideTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        int i = 0, startY = 107, spacing = 25;
        MaidSideTabButton setting = new MaidSideTabButton(rightPos, topPos, startY, (b) -> settingBtnPressed(screen), List.of(Component.translatable("gui.touhou_little_maid_addon.btn.task_setting"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_setting.desc"),
                Component.translatable("gui.touhou_little_maid_addon.btn.task_warn_text")));
        if (screen instanceof MaidTaskConfigerGui<?>) {
            setting.active = false;
        }
        i++;

        MaidSideTabButton book = new MaidSideTabButton(rightPos, topPos + i * spacing, startY + i * spacing, (b) -> {
            if (ModList.get().isLoaded("patchouli")) {
                PatchouliAPI.IPatchouliAPI iPatchouliAPI = PatchouliAPI.get();
                iPatchouliAPI.openBookGUI(new ResourceLocation(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo"));
                //todo
//                iPatchouliAPI.openBookEntry();
            } else {
                PatchouliWarningScreen.open();
            }
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
