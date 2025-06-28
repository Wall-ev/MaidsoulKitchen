package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.ModGroup;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.LoadingModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class ImportAnalyzer {
    private static final String FILE_NAME = "mod_task_clazz.json";

    public static void writeModTaskClazz(Path rootOutputFolder) throws ClassNotFoundException {
        Map<ResourceLocation, Set<String>> modTaskClazz = collectModTaskClazz();
        ModTaskClass.CODEC.encodeStart(JsonOps.COMPRESSED, ModTaskClass.create(modTaskClazz))
                .resultOrPartial(error -> {
                    MaidsoulKitchen.LOGGER.error("生成失败：{}", error);
                })
                .ifPresent(data -> {
                    File file = new File(rootOutputFolder.toString().replace("generated", "main") + "\\" + FILE_NAME);
                    Gson gson = new GsonBuilder()
                            .setPrettyPrinting()  // 保留缩进
                            .create();
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdir();
                    }
                    try {
                        FileWriter fileWriter = new FileWriter(file);
                        fileWriter.write(gson.toJson(data));
                        fileWriter.close();
                        MaidsoulKitchen.LOGGER.info("生成成功：{}", file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    public static ModTaskClass readModTaskClazz() {
        try {
            Path resource = LoadingModList.get().getModFileById(MaidsoulKitchen.MOD_ID)
                    .getFile()
                    .findResource(FILE_NAME);
            String json = Files.readString(resource);
            JsonArray jsonData = JsonParser.parseString(json).getAsJsonArray();
            return ModTaskClass.CODEC.parse(JsonOps.COMPRESSED, jsonData)
                    .result()
                    .orElseThrow();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static Map<ResourceLocation, Set<String>> collectModTaskClazz() throws ClassNotFoundException {
        Type annotationType = Type.getType(TaskImport.class);
        List<ModFileScanData> allScanData = ModList.get().getAllScanData();
        Map<ResourceLocation, Set<String>> taskClazz = new HashMap<>();
        for (ModFileScanData scanData : allScanData) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                Type annotationedType = data.annotationType();
                if (Objects.equals(annotationedType, annotationType)) {
                    TaskInfo task = TaskInfo.by(getEnumHolderValue(data, "value"));
                    Set<String> taskModGroup = getTaskClazz(data);

                    String memberName = data.memberName();
                    Class<?> targetClazz = Class.forName(memberName);
                    String classPath = getClassPath(targetClazz);
                    Set<String> importClazzList = collectImportClazz(classPath, taskModGroup);

                    taskClazz.computeIfAbsent(task.getUid(), (uid) -> {
                        return new HashSet<>();
                    }).addAll(importClazzList);
                }
            }
        }
        return taskClazz;
    }

    private static String getEnumHolderValue(ModFileScanData.AnnotationData data, String name) {
        return ((ModAnnotation.EnumHolder) data.annotationData().get(name)).getValue();
    }

    private static Set<String> getTaskClazz(ModFileScanData.AnnotationData data) {
        TaskInfo task = TaskInfo.by(getEnumHolderValue(data, "value"));
        ModGroup group = task.group;
        ModGroup extraGroup = data.annotationData().get("extraGroup") != null ? ModGroup.by(getEnumHolderValue(data, "extraGroup")) : ModGroup.NONE;
        Set<String> clazzList = Sets.newHashSet(group.groups);
        clazzList.addAll(extraGroup.groups);
        return clazzList;
    }

    private static Set<String> collectImportClazz(String filePath, Set<String> conditionGroup) {
        Set<String> imports = new HashSet<>();
        Pattern importPattern = Pattern.compile("^\\s*import\\s+([^;]+);");

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = importPattern.matcher(line);
                if (matcher.find()) {
                    String group = matcher.group(1);
                    if (conditionGroup.stream().anyMatch(group::contains) && !group.endsWith("*")) {
                        if (group.startsWith("static ")) {
                            String[] split = group.split("\\.");
                            group = group.replace("static ", "").replace("." + split[split.length - 1], "").strip();
                        }
                        imports.add(group);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return imports;
    }

    private static String getClassPath(Class<?> clazz) {
        // 获取类的源文件路径
        String classFilePath = clazz.getName().replace('.', '/') + ".class";
        URL resource = clazz.getClassLoader().getResource(classFilePath);
        if (resource == null) {
            return "";
        }
        String rootDir = getRootDir(resource.getPath());
        return toSourcePath(rootDir, clazz.getName());
    }

    private static String toSourcePath(String rootDir, String classPath) {
        return rootDir + "src/main/java/" + classPath.replace(".", "/") + ".java";
    }

    private static String getRootDir(String classFilePath) {
        Pattern pattern = Pattern.compile("^(?<projectRoot>.+?)(build|target)[\\\\/]");

        Matcher matcher = pattern.matcher(classFilePath);
        if (matcher.find()) {
            return matcher.group("projectRoot");
        }
        throw new RuntimeException("路径获取失败，请检查： " + classFilePath);
    }
}
