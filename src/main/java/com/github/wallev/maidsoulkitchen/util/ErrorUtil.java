package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.sounds.SoundEvents;
import org.apache.commons.lang3.exception.ExceptionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

public final class ErrorUtil {

    private ErrorUtil() {
    }

    public static void reportError2LocalPlayer(Throwable e) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        String[] rootCauseStackTrace = ExceptionUtils.getRootCauseStackTrace(e);

        List<StackTraceElement> limit = Arrays.stream(e.getStackTrace()).limit(10).toList();
        String string = limit.toString();

        MutableComponent errorMessage = Component.literal("--------[Ai Error]-----------\n")
//                .append(ExceptionUtils.getStackTrace(e))
                .append(string)
                .withStyle(ChatFormatting.RED);
        player.sendSystemMessage(errorMessage);
        player.playSound(SoundEvents.GLASS_BREAK, 1.0f, 1.0f);
    }

    public static void errorRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
            reportError2LocalPlayer(e);
        }
    }

    public static <T> T errorRun(Supplier<T> runnable, T def) {
        try {
            return runnable.get();
        } catch (Exception e) {
            e.printStackTrace();
            reportError2LocalPlayer(e);
            return def;
        }
    }
}
