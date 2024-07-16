package com.github.catbert.tlma.inventory.container;

import net.minecraft.nbt.CompoundTag;

public class ClientTaskSettingMenuManager {
    private static CompoundTag menuData;
    private ClientTaskSettingMenuManager() {
    }

    public static void setMenuData(CompoundTag menuData) {
        ClientTaskSettingMenuManager.menuData = menuData;
    }

    public static CompoundTag getMenuData() {
        return ClientTaskSettingMenuManager.menuData;
    }

    public static void clearMenuData() {
        ClientTaskSettingMenuManager.menuData = null;
    }
}
