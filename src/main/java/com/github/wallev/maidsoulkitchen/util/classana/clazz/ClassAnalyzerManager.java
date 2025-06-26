package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.ModGroup;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
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
    public static final String FILE_NAME = "mod_task_clazz.json";

    public static void writeModTaskClazz(Path rootOutputFolder) throws Exception {
        Type annotationType = Type.getType(TaskClassAnalyzer.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        ClassMap clazzMap = new ClassMap();
        for (ModFileScanData scanData : allScanData) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                Type annotationedType = data.annotationType();
                if (Objects.equals(annotationedType, annotationType)) {
                    Object taskValue = data.annotationData().get("value");
                    if (taskValue == null) {
                        throw new RuntimeException("请指定任务类别: " + data.memberName());
                    }
                    TaskInfo task = TaskInfo.by(getEnumHolderValue(data, "value"));
                    String memberName = data.memberName();
                    clazzMap.addClazz(task, Class.forName(memberName));
                }
            }
        }

        ClassAnalyzerTool.analyzerAndGenerateFile(rootOutputFolder, clazzMap);
    }

    private static TaskClazzInfo readModTaskClazzFromFile() {
        try {
            Path resource = LoadingModList.get().getModFileById(MaidsoulKitchen.MOD_ID)
                    .getFile()
                    .findResource(FILE_NAME);
            String json = Files.readString(resource);
            JsonObject jsonData = JsonParser.parseString(json).getAsJsonObject();
            return TaskClazzInfo.CODEC.parse(JsonOps.INSTANCE, jsonData)
                    .result()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<ResourceLocation, Boolean> readModTaskClazz() throws IOException {
        TaskClazzInfo taskClazzInfo = readModTaskClazzFromFile();
        return VerifyExistence.verify(taskClazzInfo);
    }

    private static String getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        return ((ModAnnotation.EnumHolder) data.annotationData().get(name)).getValue();
    }

    public static class ClassMap {
        private final Map<TaskInfo, Set<Class<?>>> map = new HashMap<>();

        private void addClazzes(TaskInfo taskInfo, Set<Class<?>> clazzes) {
            this.map.computeIfAbsent(taskInfo, (taskInfo1) -> {
                return new HashSet<>();
            }).addAll(clazzes);
        }

        private void addClazz(TaskInfo taskInfo, Class<?> clazz) {
            this.map.computeIfAbsent(taskInfo, (taskInfo1) -> {
                return new HashSet<>();
            }).add(clazz);
        }

        public List<Class<?>> getClazzs(TaskInfo taskInfo) {
            return Lists.newArrayList(this.map.get(taskInfo));
        }

        public Map<TaskInfo, Set<Class<?>>> getMap() {
            return map;
        }

        public static boolean isAllowed(String className) {
            for (String group : ModGroup.BLACK.groups) {
                if (className.startsWith(group)) {
                    return false;
                }
            }
            return true;
        }
    }

}
