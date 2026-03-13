package com.github.wallev.maidsoulkitchen.vhelper.client.chat;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import javax.annotation.Nullable;

public abstract class VComponent implements Component {
    public static final Component EMPTY = CommonComponents.EMPTY;
    public static final Component NEW_LINE = CommonComponents.NEW_LINE;

    private VComponent() {
    }

    public static Component nullToEmpty(@Nullable String text) {
        return Component.nullToEmpty(text);
    }

    public static MutableComponent literal(String text) {
        return Component.literal(text);
    }

    public static MutableComponent translatable(String key) {
        return Component.translatable(key);
    }

    public static MutableComponent translatable(String key, Object... arg) {
        return Component.translatable(key, arg);
    }

    public static MutableComponent empty() {
        return Component.empty();
    }

    public static MutableComponent keybind(String name) {
        return Component.keybind(name);
    }
}
