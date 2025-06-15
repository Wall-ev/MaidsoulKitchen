package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import net.minecraft.Util;

import java.time.Duration;

public class TimeUtil {
    // 超时检测
    private static final long WARNING_TIME_NANOS = Duration.ofMillis(50L).toNanos();

    public static void record(Runnable runnable, String name) {
        long timeRecord = Util.getNanos();
        runnable.run();
        timeRecord = Util.getNanos() - timeRecord;
        double timeMs = timeRecord / 1000000.0;
        MaidsoulKitchen.LOGGER.error("{} taking time: {} ms", name, timeMs);
    }

}
