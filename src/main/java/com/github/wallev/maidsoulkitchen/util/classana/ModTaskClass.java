package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.*;

public record ModTaskClass(Map<ResourceLocation, List<String>> list){
    public static final Codec<Map<ResourceLocation, List<String>>> MAP_CODEC = Codec.unboundedMap(ResourceLocation.CODEC, Codec.STRING.listOf())
            .xmap((map0) -> map0, (s) -> s);
    public static final Codec<ModTaskClass> CODEC = RecordCodecBuilder.create(instance -> {
        RecordCodecBuilder<ModTaskClass, Map<ResourceLocation, List<String>>> builder = MAP_CODEC.fieldOf("list").forGetter(ModTaskClass::list);
        return instance.group(builder).apply(instance, (list) -> {
            return new ModTaskClass(list);
        });
    });

    public static ModTaskClass create(Map<ResourceLocation, Set<String>> list) {
        Map<ResourceLocation, List<String>> listMap = new HashMap<>();
        list.forEach((key, val) -> {
            listMap.put(key, Lists.newArrayList(val));
        });
        return new ModTaskClass(listMap);
    }

    public boolean canLoad(ResourceLocation taskUid) {
        for (String clazz : list.getOrDefault(taskUid, List.of())) {
            try {
                Class.forName(clazz);
            } catch (ClassNotFoundException e) {
                // ERROR] [maidsoulkitchen/]
                TaskInfo taskInfo = TaskInfo.by(taskUid);
                assert taskInfo != null;
                Mods bindMod = taskInfo.bindMod;
                TaskLoadError.putError(taskUid);
                MaidsoulKitchen.LOGGER.error("ModTaskClass.LoadError: task: {}, class: {}", taskUid, clazz);
                return false;
            }
        }
        return true;
    }
}
