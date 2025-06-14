package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;

import java.util.function.Supplier;

public final class ErrorUtil {

    private ErrorUtil() {
    }

    public static void reportError(EntityMaid maid, Exception e) {
        LivingEntity owner = maid.getOwner();
        assert owner != null;
        owner.sendSystemMessage(Component.literal(e.getMessage()));
    }

    public static void safeRun(EntityMaid maid, Runnable runnable) {
        try {
            runnable.run();
        } catch (Exception e) {
            e.printStackTrace();
            reportError(maid, e);
        }
    }

    public static <T> T safeRun(EntityMaid maid, Supplier<T> runnable, T def) {
        try {
            return runnable.get();
        } catch (Exception e) {
            e.printStackTrace();
            reportError(maid, e);
            return def;
        }
    }
}
