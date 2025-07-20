package com.github.wallev.maidsoulkitchen.util;

import com.github.wallev.maidsoulkitchen.vhelper.client.chat.VComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

public class TextContactUtil {

    public static <T> MutableComponent contact(List<T> list, Function<T, Component> component) {
        return contact(list, "、", component);
    }

    public static <T> MutableComponent contact(List<T> list, MutableComponent pre, Function<T, Component> component) {
        return contact(list, pre, "、", component);
    }

    public static <T> MutableComponent contact(List<T> list, Component contactStyle, Function<T, Component> component) {
        return contact(list, VComponent.empty(), contactStyle, component);
    }

    public static <T> MutableComponent contact(List<T> list, String contactStyle, Function<T, Component> component) {
        return contact(list, VComponent.empty(), contactStyle, component);
    }

    public static <T> MutableComponent contact(List<T> list, MutableComponent pre, Component contactStyle, Function<T, Component> component) {
        return contact(list, pre, (o) -> o.append(contactStyle), component);
    }

    public static <T> MutableComponent contact(List<T> list, MutableComponent pre, String contactStyle, Function<T, Component> component) {
        return contact(list, pre, (o) -> o.append(contactStyle), component);
    }

    public static <T> MutableComponent contact(List<T> list, MutableComponent pre, Consumer<MutableComponent> contactStyle, Function<T, Component> component) {
        int i = 0, size = list.size();
        for (T t : list) {
            pre.append(component.apply(t));
            if (++i < size) {
                contactStyle.accept(pre);
            }
        }
        return pre;
    }
}
