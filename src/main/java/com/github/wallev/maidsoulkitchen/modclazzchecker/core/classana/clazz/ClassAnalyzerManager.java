package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.ModClazzChecker;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.TaskMixinAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class ClassAnalyzerManager {

    public static void writeModTaskClazz(Path rootOutputFolder, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
        Type taskClazzAnnotationType = checkManager.getTaskClazzAnnotationType();
        Type taskMixinClazzAnnotationType = checkManager.getTaskClazzMixinAnnotationType();
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        ClassMap clazzMap = new ClassMap();
        for (ModFileScanData scanData : allScanData) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                Type annotationedType = data.annotationType();
                String memberName = data.memberName();
                if (Objects.equals(annotationedType, taskClazzAnnotationType)) {
                    Object taskValue = data.annotationData().get("value");
                    if (taskValue == null) {
                        throw new RuntimeException("Please specify the task category: " + data.memberName());
                    }
                    ITaskInfo<?> task = checkManager.taskInfoByKey(getEnumHolderValue(data, "value"));
                    clazzMap.addClazz(task, Class.forName(memberName));
                }
                if (Objects.equals(annotationedType, taskMixinClazzAnnotationType) && data.memberName().startsWith(checkManager.getMixinPackage())) {
                    Object taskValue = data.annotationData().get("value");
                    if (taskValue == null) {
                        throw new RuntimeException("Please specify the task category: " + data.memberName());
                    }
                    List<ModAnnotation.EnumHolder> tasks = getEnumHolders(data, "value");
                    for (ModAnnotation.EnumHolder taskVal : tasks) {
                        ITaskInfo<?> task = checkManager.taskInfoByKey(taskVal.getValue());
                        clazzMap.addTaskMixin(task, memberName);
                    }
                }
            }
        }

        ClassAnalyzerTool.analyzerAndGenerateFile(rootOutputFolder, clazzMap, checkManager);
    }

    public static TaskClazzInfo readModTaskClazzFromFile(BaseClazzCheckManager<?, ?> checkManager) {
        try {
            Path resource = LoadingModList.get().getModFileById(checkManager.getModId())
                    .getFile()
                    .findResource(checkManager.getFileName());
            String json = Files.readString(resource);
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
            return TaskClazzInfo.CODEC.apply(checkManager.getModsCodecO()).parse(JsonOps.INSTANCE, jsonData)
                    .resultOrPartial(error -> {
                        ModClazzChecker.LOGGER.error("read modtaskclazz error：{}, from file: {}", error, resource);
                    })
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static TaskMixinAnalyzer.ModTaskMixinMap readModTaskMixinClazzFromFile(BaseClazzCheckManager<?, ?> checkManager) {
        try {
            Path resource = LoadingModList.get().getModFileById(checkManager.getModId())
                    .getFile()
                    .findResource(checkManager.getFileName());
            String json = Files.readString(resource);
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject().getAsJsonObject("mixinInfo");
            return TaskMixinAnalyzer.ModTaskMixinMap.CODEC.apply(checkManager.getModsCodecO()).parse(JsonOps.INSTANCE, jsonData)
                    .resultOrPartial(error -> {
                        ModClazzChecker.LOGGER.error("read modmixinclazz error：{}, from file: {}", error, resource);
                    })
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Boolean> readModTaskClazz(BaseClazzCheckManager<?, ?> checkManager) throws IOException {
        TaskClazzInfo taskClazzInfo = readModTaskClazzFromFile(checkManager);
        return VerifyExistence.verify(taskClazzInfo, checkManager);
    }

    public static Map<String, Boolean> readModTaskClazz(TaskClazzInfo taskClazzInfo, BaseClazzCheckManager<?, ?> checkManager) throws IOException {
        return VerifyExistence.verify(taskClazzInfo, checkManager);
    }

    private static String getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        return ((ModAnnotation.EnumHolder) data.annotationData().get(name)).getValue();
    }

    @SuppressWarnings("unchecked")
    private static List<ModAnnotation.EnumHolder> getEnumHolders(ModFileScanData.AnnotationData data, String name) {
        return (List<ModAnnotation.EnumHolder>) data.annotationData().get(name);
    }

    public static class ClassMap {
        private final Map<ITaskInfo<?>, Set<Class<?>>> map = new HashMap<>();
        private final Map<ITaskInfo<?>, Set<String>> mixinMap = new HashMap<>();

        private void addClazzes(ITaskInfo<?> taskInfo, Set<Class<?>> clazzes) {
            this.map.computeIfAbsent(taskInfo, (taskInfo1) -> {
                return new HashSet<>();
            }).addAll(clazzes);
        }

        private void addClazz(ITaskInfo<?> taskInfo, Class<?> clazz) {
            this.map.computeIfAbsent(taskInfo, (taskInfo1) -> {
                return new HashSet<>();
            }).add(clazz);
        }

        public List<Class<?>> getClazzs(ITaskInfo<?> taskInfo) {
            return Lists.newArrayList(this.map.get(taskInfo));
        }

        public Map<ITaskInfo<?>, Set<Class<?>>> getMap() {
            return map;
        }

        public void addTaskMixin(ITaskInfo<?> taskInfo, String mixin) {
            this.mixinMap.computeIfAbsent(taskInfo, (taskInfo1) -> {
                return new HashSet<>();
            }).add(mixin);
        }

        public Map<ITaskInfo<?>, Set<String>> getMixinMap() {
            return mixinMap;
        }

        public static boolean isAllowed(String className, BaseClazzCheckManager<?, ?> checkManager) {
            for (String group : checkManager.getBlackGroups()) {
                if (className.startsWith(group)) {
                    return false;
                }
                // fixme: [Lcom.github.wallev.maidsoulkitchen.api.task.farm.ICompatFarmHandler$Result;#clone()Ljava/lang/Object;
                // 不知啥情况会蹦出这个玩意来...
                if (className.contains(group)) {
                    return false;
                }
            }
            return true;
        }
    }

}
