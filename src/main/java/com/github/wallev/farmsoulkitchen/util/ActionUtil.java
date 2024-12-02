package com.github.wallev.farmsoulkitchen.util;

import com.github.wallev.farmsoulkitchen.foundation.utility.Mods;

public final class ActionUtil {

    private ActionUtil() {
    }

    public static void modRun(Mods mod, Runnable runnable) {
        if (mod.isLoaded()) {
            runnable.run();
        }
    }
}
