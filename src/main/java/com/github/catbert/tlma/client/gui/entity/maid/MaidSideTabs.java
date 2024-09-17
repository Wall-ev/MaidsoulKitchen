package com.github.catbert.tlma.client.gui.entity.maid;

import com.github.catbert.tlma.api.ILittleMaidTask;
import com.github.catbert.tlma.client.gui.entity.maid.cook.MaidTaskConfigerGui;
import com.github.catbert.tlma.client.gui.mod.ClothConfigScreen;
import com.github.catbert.tlma.client.gui.mod.PatchouliWarningScreen;
import com.github.catbert.tlma.client.gui.widget.button.MaidSideTabButton;
import com.github.catbert.tlma.entity.passive.SideTab;
import com.github.catbert.tlma.foundation.utility.Mods;
import com.github.catbert.tlma.network.NetworkHandler;
import com.github.catbert.tlma.network.message.ToggleSideTabMessage;
import com.github.tartaricacid.touhoulittlemaid.TouhouLittleMaid;
import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;
import com.github.tartaricacid.touhoulittlemaid.client.gui.entity.maid.AbstractMaidContainerGui;
import com.github.tartaricacid.touhoulittlemaid.compat.cloth.MenuIntegration;
import com.github.tartaricacid.touhoulittlemaid.inventory.container.AbstractMaidContainer;
import me.shedaniel.clothconfig2.api.ConfigBuilder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
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
    private final int i = 0, startY = 107, spacing = 25;

    public MaidSideTabs(int entityId, int rightPos, int topPos) {
        this.entityId = entityId;
        this.rightPos = rightPos + 251;
        this.topPos = topPos + 28 + 9;
    }

    private void settingBtnPressed(AbstractMaidContainerGui<T> gui) {
        IMaidTask task = gui.getMaid().getTask();

        if (task instanceof ILittleMaidTask) {
            NetworkHandler.sendToServer(new ToggleSideTabMessage(gui.getMenu().containerId, entityId, SideTab.TASK_SETTING.getIndex(), task.getUid(), gui.isTaskListOpen(), ((IAbstractMaidContainerGui) gui).getTaskPage()));
        } else {

        }
    }

    public MaidSideTabButton[] getTabs(AbstractMaidContainerGui<T> screen) {
        MaidSideTabButton taskSetting = genSideTabButton(screen, SideTab.TASK_SETTING, (b) -> settingBtnPressed(screen));
        if (screen instanceof MaidTaskConfigerGui<?>) {
            taskSetting.active = false;
        }

        MaidSideTabButton taskBook = genSideTabButton(screen, SideTab.TASK_BOOK, (b) -> {
            if (ModList.get().isLoaded("patchouli")) {
                PatchouliAPI.IPatchouliAPI iPatchouliAPI = PatchouliAPI.get();
                iPatchouliAPI.openBookGUI(new ResourceLocation(TouhouLittleMaid.MOD_ID, "memorizable_gensokyo"));
                //todo
//                iPatchouliAPI.openBookEntry();
            } else {
                PatchouliWarningScreen.open();
            }
        });

        MaidSideTabButton taskInfo = genSideTabButton(screen, SideTab.TASK_INFO, (b) -> {
        });
        MaidSideTabButton modSetting = genSideTabButton(screen, SideTab.MOD_SETTING, (b) -> {
            Minecraft mc = Minecraft.getInstance();
            ConfigBuilder configBuilder = MenuIntegration.getConfigBuilder();
            configBuilder.setGlobalizedExpanded(true);
            Screen modConfigerScreen = Mods.CLOTH_CONFIG.isLoaded ? configBuilder.build() : new ClothConfigScreen(mc.screen);
            mc.setScreen(modConfigerScreen);

        });

        return new MaidSideTabButton[]{taskSetting, taskBook, taskInfo, modSetting};
    }

    private MaidSideTabButton genSideTabButton(AbstractMaidContainerGui<T> screen, SideTab sideTab, Button.OnPress onPressIn) {
        String titleLangKey = String.format("gui.touhou_little_maid_addon.btn.%s", sideTab.name().toLowerCase());
        String descLangKey = String.format("gui.touhou_little_maid_addon.btn.%s.desc", sideTab.name().toLowerCase());

        return new MaidSideTabButton(sideTab, rightPos, topPos + sideTab.getIndex() * spacing, startY + sideTab.getIndex() * spacing, onPressIn,
                List.of(Component.translatable(titleLangKey),
                        Component.translatable(descLangKey),
                        Component.translatable("gui.touhou_little_maid_addon.btn.task_warn_text")));
    }
}
