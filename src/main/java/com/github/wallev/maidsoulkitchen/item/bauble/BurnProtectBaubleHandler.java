package com.github.wallev.maidsoulkitchen.item.bauble;

import com.github.tartaricacid.touhoulittlemaid.item.bauble.BaubleManager;
import com.github.wallev.maidsoulkitchen.init.ModItems;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;

public class BurnProtectBaubleHandler {
    public static void register(BaubleManager manager) {
        if (Mods.TLM_SLOT_MODERN.versionLoad()) {
            manager.bind(ModItems.BURN_PROTECT_BAUBLE, new BurnProtectBaubleModern());
        } else if (Mods.TLM_SLOT_LEGACY.versionLoad()) {
            manager.bind(ModItems.BURN_PROTECT_BAUBLE, new BurnProtectBaubleLegacy());
        }
    }

}