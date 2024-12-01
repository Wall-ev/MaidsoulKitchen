package com.github.catbert.tlma.util;

import com.github.catbert.tlma.foundation.utility.Mods;

public final class ActionUtil {

    private ActionUtil() {
    }

    public static void modRun(Mods mod, Runnable runnable) {
        if (mod.isLoaded()) {
            runnable.run();
        }
    }
}
