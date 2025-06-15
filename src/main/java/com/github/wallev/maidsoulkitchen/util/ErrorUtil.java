package com.github.wallev.maidsoulkitchen.util;

import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.function.Supplier;

public final class ErrorUtil {

    private ErrorUtil() {
    }

    public static void reportError(Exception e) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player == null) {
            return;
        }
        MutableComponent errorMessage = Component.literal("[Ai Error]: " + e.getMessage()).withStyle(ChatFormatting.RED);
        player.sendSystemMessage(errorMessage);
    }

    public static void errorRun(Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
            reportError(e);
        }
    }

    public static <T> T errorRun(Supplier<T> runnable, T def) {
        try {
            return runnable.get();
        } catch (Exception e) {
            e.printStackTrace();
            reportError(e);
            return def;
        }
    }
}
