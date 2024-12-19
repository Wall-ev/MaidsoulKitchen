package com.github.wallev.maidsoulkitchen.client.gui.item;

import com.github.wallev.maidsoulkitchen.client.gui.widget.button.CookBagGuiSideTabButton;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagAbstractContainer;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import com.github.wallev.maidsoulkitchen.network.message.ToggleCookBagGuiSideTabMessage;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

import java.util.List;
import java.util.Locale;

public record CookBagSideTabs<T extends CookBagAbstractContainer>(int pContainerId, int rightPos, int topPos) {
    private static final int SPACING = 25;

    public static int getTabHeights() {
        return SPACING * 2;
    }

    public CookBagGuiSideTabButton[] getTabs(CookBagAbstractContainerGui<T> gui) {
        // 任务配置界面按钮
        CookBagGuiSideTabButton taskConfig = genSideTabButton(SideTab.TASK_CONFIG, b -> {
            NetworkHandler.sendToServer(new ToggleCookBagGuiSideTabMessage(0));
        });
        if (gui instanceof CookBagConfigContainerGui) {
            taskConfig.active = false;
        }

        // 跳转帕秋莉手册按钮
        CookBagGuiSideTabButton taskBook = genSideTabButton(SideTab.TASK_BOOK, (b) -> {
            NetworkHandler.sendToServer(new ToggleCookBagGuiSideTabMessage(1));
        });
        if (gui instanceof CookBagGui) {
            taskBook.active = false;
        }

        return new CookBagGuiSideTabButton[]{taskConfig, taskBook};
    }

    private CookBagGuiSideTabButton genSideTabButton(SideTab sideTab, Button.OnPress onPressIn) {
        String name = sideTab.name().toLowerCase(Locale.ENGLISH);
        String titleLangKey = String.format("gui.touhou_little_maid.button.%s", name);
        String descLangKey = String.format("gui.touhou_little_maid.button.%s.desc", name);

        return new CookBagGuiSideTabButton(rightPos, topPos + sideTab.getIndex() * SPACING, sideTab.getIndex() * SPACING, onPressIn,
                List.of(Component.translatable(titleLangKey), Component.translatable(descLangKey)));
    }
}
