package com.github.wallev.maidsoulkitchen.modclazzchecker.core.util;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.ModClazzChecker;
import net.minecraft.Util;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class TimeUtil {
    // 超时检测
    private static final long WARNING_TIME_NANOS = Duration.ofMillis(50L).toNanos();

    public static void record(Runnable runnable, String name) {
        long timeRecord = Util.getNanos();
        runnable.run();
        timeRecord = Util.getNanos() - timeRecord;
        double timeMs = timeRecord / 1000000.0;
        ModClazzChecker.LOGGER.error("{} taking time: {} ms", name, timeMs);
    }

    public static String getCurrentTimeWithFormat() {
        // 获取当前时间
        Date currentDate = new Date();
//        System.out.println("当前时间：" + currentDate);

        // 按照指定格式输出
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = sdf.format(currentDate);
        return formattedDate;
//        System.out.println("格式化后的时间：" + formattedDate);
    }

}
