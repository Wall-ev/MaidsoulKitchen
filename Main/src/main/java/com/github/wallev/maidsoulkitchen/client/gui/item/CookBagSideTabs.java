package com.github.wallev.maidsoulkitchen.client.gui.item;

import com.github.wallev.maidsoulkitchen.client.gui.widget.button.CookBagGuiSideTabButton;
import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import com.github.wallev.maidsoulkitchen.inventory.container.item.CookBagAbstractContainer;
import com.github.wallev.maidsoulkitchen.network.NetworkHandler;
import net.minecraft.client.gui.components.Button;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Locale;

@OnlyIn(Dist.CLIENT)
public record CookBagSideTabs<T extends CookBagAbstractContainer>(int pContainerId, int rightPos, int topPos) {
    private static final int SPACING = 25;

    public static int getTabHeights() {
        return SPACING * 2;
    }

    public CookBagGuiSideTabButton[] getTabs(CookBagAbstractContainerGui<T> gui) {
        // 任务配置界面按钮
        CookBagGuiSideTabButton taskConfig = genSideTabButton(SideTab.TASK_CONFIG, b -> {
            NetworkHandler.C2S.toggleCookBagGuiSideTab(0);
        });
        if (gui instanceof CookBagConfigGui) {
            taskConfig.active = false;
        }

        // 跳转帕秋莉手册按钮
        CookBagGuiSideTabButton taskBook = genSideTabButton(SideTab.TASK_BOOK, (b) -> {
            NetworkHandler.C2S.toggleCookBagGuiSideTab(1);
        });
        if (gui instanceof CookBagContainerGui) {
            taskBook.active = false;
        }

        return new CookBagGuiSideTabButton[]{taskConfig, taskBook};
    }

    private CookBagGuiSideTabButton genSideTabButton(SideTab sideTab, Button.OnPress onPressIn) {
        String name = sideTab.name().toLowerCase(Locale.ENGLISH);
        String titleLangKey = String.format("gui.touhou_little_maid.button.%s", name);
        String descLangKey = String.format("gui.touhou_little_maid.button.%s.desc", name);

        return new CookBagGuiSideTabButton(rightPos, topPos + sideTab.getIndex() * SPACING, sideTab.getIndex() * SPACING, onPressIn,
                List.of(VComponent.translatable(titleLangKey), VComponent.translatable(descLangKey)));
    }
}
