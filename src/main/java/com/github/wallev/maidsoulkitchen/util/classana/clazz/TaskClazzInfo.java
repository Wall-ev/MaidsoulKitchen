package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public record TaskClazzInfo(Map<ResourceLocation, ClazzTaskInfo> clazzInfoMap, List<String> allClazzs) {

    public static final Codec<TaskClazzInfo> CODEC = RecordCodecBuilder.create(ins -> ins.group(
            Codec.unboundedMap(ResourceLocation.CODEC, ClazzTaskInfo.CODEC).fieldOf("clazzInfoMap").forGetter(TaskClazzInfo::clazzInfoMap),
            Codec.STRING.listOf().fieldOf("allClazzs").forGetter(TaskClazzInfo::allClazzs)
    ).apply(ins, TaskClazzInfo::new));

    public TaskClazzInfo(Map<ResourceLocation, ClazzTaskInfo> clazzInfoMap) {
        this(clazzInfoMap, createAllClazzs(clazzInfoMap));
    }

    private static List<String> createAllClazzs(Map<ResourceLocation, ClazzTaskInfo> clazzInfoMap) {
        Set<String> clazzs = new HashSet<>();
        for (ClazzInfo value : clazzInfoMap.values().stream().map(clazzTaskInfo -> clazzTaskInfo.clazzInfo).toList()) {
            clazzs.addAll(value.classes);
        }
        return Lists.newArrayList(clazzs);
    }

    public record ClazzInfo(List<String> classes, List<String> methods, List<String> fields) {
        public static final Codec<ClazzInfo> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.STRING.listOf().fieldOf("classes").forGetter(ClazzInfo::classes),
                Codec.STRING.listOf().fieldOf("methods").forGetter(ClazzInfo::methods),
                Codec.STRING.listOf().fieldOf("fields").forGetter(ClazzInfo::fields)
        ).apply(ins, ClazzInfo::new));
    }

    public record ClazzTaskInfo(String bindMod, ClazzInfo clazzInfo) {
        public static final Codec<ClazzTaskInfo> CODEC = RecordCodecBuilder.create(ins -> ins.group(
                Codec.STRING.fieldOf("bindMod").forGetter(ClazzTaskInfo::bindMod),
                ClazzInfo.CODEC.fieldOf("clazzInfo").forGetter(ClazzTaskInfo::clazzInfo)
        ).apply(ins, ClazzTaskInfo::new));

        public static ClazzTaskInfo create(TaskInfo taskInfo, ClazzInfo clazzInfo) {
            return new ClazzTaskInfo(taskInfo.bindMod.name(), clazzInfo);
        }

        public boolean modLoaded() {
            return Mods.by(bindMod).versionLoaded;
        }
    }
}
