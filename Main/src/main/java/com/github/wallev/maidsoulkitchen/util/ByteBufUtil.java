package com.github.wallev.maidsoulkitchen.util;

import com.google.common.collect.Maps;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.commons.compress.utils.Lists;

import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Function;

public final class ByteBufUtil {

    public static <C> void writeCustomList(List<C> list, FriendlyByteBuf buf, Consumer<C> cConsumer) {
        buf.writeVarInt(list.size());
        for (C c : list) {
            cConsumer.accept(c);
        }
    }

    public static <C> List<C> readCustomList(FriendlyByteBuf buf, Function<FriendlyByteBuf, C> byteBufFunction) {
        int size = buf.readVarInt();
        List<C> list = Lists.newArrayList();
        for (int i = 0; i < size; i++) {
            list.add(byteBufFunction.apply(buf));
        }

        return list;
    }

    public static void writeStrList(List<String> list, FriendlyByteBuf buf) {
        writeCustomList(list, buf, (buf::writeUtf));
    }

    public static List<String> readStrList(List<String> list, FriendlyByteBuf buf) {
       return readCustomList(buf, FriendlyByteBuf::readUtf);
    }

    public static void writeMapSB(Map<String, Boolean> map, FriendlyByteBuf buf) {
        buf.writeVarInt(map.size());
        map.forEach((key, value) -> {
            buf.writeUtf(key);
            buf.writeBoolean(value);
        });
    }

    public static Map<String, Boolean> readMapSB(FriendlyByteBuf buf) {
        int size = buf.readVarInt();
        Map<String, Boolean> map = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            map.put(buf.readUtf(), buf.readBoolean());
        }
        return map;
    }

    public static <C> void writeMapSCustom(Map<String, C> map, FriendlyByteBuf buf, Consumer<C> cConsumer) {
        buf.writeVarInt(map.size());
        map.forEach((key, value) -> {
            buf.writeUtf(key);
            cConsumer.accept(value);
        });
    }

    public static <C> Map<String, C> readMapSCustom(FriendlyByteBuf buf, Function<FriendlyByteBuf, C> byteBufCFunction) {
        int size = buf.readVarInt();
        Map<String, C> map = Maps.newHashMap();
        for (int i = 0; i < size; i++) {
            map.put(buf.readUtf(), byteBufCFunction.apply(buf));
        }
        return map;
    }

}
