package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.contents.*;

import javax.annotation.Nullable;
import java.util.Optional;

public abstract class VComponent implements Component {

    public static Component nullToEmpty(@Nullable String text) {
        return text != null ? literal(text) : CommonComponents.EMPTY;
    }

    public static MutableComponent literal(String text) {
        return MutableComponent.create(new LiteralContents(text));
    }

    public static MutableComponent translatable(String key) {
        return MutableComponent.create(new TranslatableContents(key, null, TranslatableContents.NO_ARGS));
    }

    public static MutableComponent translatable(String key, Object... arg) {
        return MutableComponent.create(new TranslatableContents(key, null, arg));
    }

    public static MutableComponent translatableWithFallback(String key, @Nullable String fallBack) {
        return MutableComponent.create(new TranslatableContents(key, fallBack, TranslatableContents.NO_ARGS));
    }

    public static MutableComponent translatableWithFallback(String key, @Nullable String fallBack, Object... arg) {
        return MutableComponent.create(new TranslatableContents(key, fallBack, arg));
    }

    public static MutableComponent empty() {
        return MutableComponent.create(ComponentContents.EMPTY);
    }

    public static MutableComponent keybind(String name) {
        return MutableComponent.create(new KeybindContents(name));
    }

    public static MutableComponent nbt(String nbtPathPattern, boolean interpreting, Optional<Component> separator, DataSource dataSource) {
        return MutableComponent.create(new NbtContents(nbtPathPattern, interpreting, separator, dataSource));
    }

    public static MutableComponent score(String name, String objective) {
        return MutableComponent.create(new ScoreContents(name, objective));
    }

    public static MutableComponent selector(String pattern, Optional<Component> separator) {
        return MutableComponent.create(new SelectorContents(pattern, separator));
    }
    
}
