package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.foundation.utility.Mods;

public final class ActionUtil {

    private ActionUtil() {
    }

    public static void modRun(Mods mod, Runnable runnable) {
        if (mod.isLoaded()) {
            runnable.run();
        }
    }
}
