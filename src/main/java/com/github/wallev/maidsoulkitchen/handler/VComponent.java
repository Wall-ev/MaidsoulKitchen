package com.github.wallev.maidsoulkitchen.handler;

import net.minecraft.network.chat.*;

public abstract class VComponent implements Component {
    public static final Component SPACE = literal(" ");
    public static final Component EMPTY = TextComponent.EMPTY;
    private VComponent() {
    }

    @SuppressWarnings("unchecked")
    public static <C> C empty() {
        return (C) EMPTY;
    }

    public static MutableComponent translatable(String key) {
        return new TranslatableComponent(key);
    }

    public static MutableComponent translatable(String key, Object... pArgs) {
        return new TranslatableComponent(key, pArgs);
    }

    public static MutableComponent literal(String string) {
        return new TextComponent(string);
    }

    
}
