package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.TaskMixinAnalyzer;
import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;
import java.util.function.Function;

public class TaskClazzInfo {
    public static final Function<Codec<IMods>, Codec<TaskClazzInfo>> CODEC = (mc) -> {
        return RecordCodecBuilder.create(ins -> ins.group(
                Codec.unboundedMap(Codec.STRING, ClazzTaskInfo.CODEC).fieldOf("clazzInfoMap").forGetter(TaskClazzInfo::clazzInfoMap),
                Codec.STRING.listOf().fieldOf("allClazzs").forGetter(TaskClazzInfo::allClazzs),
                TaskMixinAnalyzer.ModTaskMixinMap.CODEC.apply(mc).optionalFieldOf("mixinInfo").forGetter(TaskClazzInfo::optionalTaskMixinMap)
        ).apply(ins, TaskClazzInfo::new));
    };

    private final Map<String, ClazzTaskInfo> clazzInfoMap;
    private final List<String> allClazzs;
    private TaskMixinAnalyzer.ModTaskMixinMap taskMixinMap;

    public TaskClazzInfo(Map<String, ClazzTaskInfo> clazzInfoMap, List<String> allClazzs) {
        this.clazzInfoMap = clazzInfoMap;
        this.allClazzs = allClazzs.stream().sorted().toList();
    }

    public TaskClazzInfo(Map<String, ClazzTaskInfo> clazzInfoMap, TaskMixinAnalyzer.ModTaskMixinMap taskMixinMap) {
        this(clazzInfoMap, createAllClazzs(clazzInfoMap), taskMixinMap);
    }

    public TaskClazzInfo(Map<String, ClazzTaskInfo> clazzInfoMap, List<String> allClazzs, TaskMixinAnalyzer.ModTaskMixinMap taskMixinMap) {
        this.clazzInfoMap = clazzInfoMap;
        this.allClazzs = allClazzs.stream().sorted().toList();
        this.taskMixinMap = taskMixinMap;
    }

    public TaskClazzInfo(Map<String, ClazzTaskInfo> stringClazzTaskInfoMap, List<String> list, Optional<TaskMixinAnalyzer.ModTaskMixinMap> modTaskMixinMap) {
        this.clazzInfoMap = stringClazzTaskInfoMap;
        this.allClazzs = list.stream().sorted().toList();
        this.taskMixinMap = modTaskMixinMap.orElse(null);
    }

    public Map<String, ClazzTaskInfo> clazzInfoMap() {
        return clazzInfoMap;
    }

    private Optional<TaskMixinAnalyzer.ModTaskMixinMap> optionalTaskMixinMap() {
        return Optional.ofNullable(taskMixinMap);
    }

    public List<String> allClazzs() {
        return allClazzs;
    }

    public void setTaskMixinMap(TaskMixinAnalyzer.ModTaskMixinMap taskMixinMap) {
        this.taskMixinMap = taskMixinMap;
    }

    public TaskMixinAnalyzer.ModTaskMixinMap taskMixinMap() {
        return taskMixinMap;
    }

    private static List<String> createAllClazzs(Map<String, ClazzTaskInfo> clazzInfoMap) {
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

        public static ClazzTaskInfo create(ITaskInfo<?> taskInfo, ClazzInfo clazzInfo) {
            return new ClazzTaskInfo(taskInfo.getBindMod().name(), clazzInfo);
        }
    }
}
