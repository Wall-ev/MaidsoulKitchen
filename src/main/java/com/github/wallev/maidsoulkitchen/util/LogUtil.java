package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import org.apache.logging.log4j.Logger;

public class LogUtil {
    private static final boolean DEBUG = MaidsoulKitchen.DEBUG;
    private static final Logger LOGGER = MaidsoulKitchen.LOGGER;

    private static void log(Runnable runnable) {
        if (DEBUG) {
            runnable.run();
        }
    }

    public static void log() {
    }

    public static void debug(String message, Object p0) {
        log(() -> LOGGER.debug(message, p0));
    }
}
