package com.github.wallev.maidsoulkitchen.util.debug;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;

public class AspectDebug {
    public static void init() {
        if (MaidsoulKitchen.DEBUG) {
            AspectDebugInner.init();
        }
    }
}
