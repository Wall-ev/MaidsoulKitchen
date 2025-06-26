package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.debug.annotation.TimeRecord;
import com.github.wallev.maidsoulkitchen.util.ModUtil;
import com.github.wallev.maidsoulkitchen.util.modutility.Mods;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Path;
import java.util.*;

public class VerifyExistence {

    // 验证类、方法和字段的存在性
    @TimeRecord
    public static Map<ResourceLocation, Boolean> verify(TaskClazzInfo taskClazzInfo) throws IOException {
        Map<ResourceLocation, Boolean> taskResult = new HashMap<>();
        MultiClassAnalysisResult multiClassAnalysisResult = new MultiClassAnalysisResult();

        Map<String, ClazzInfo> allClazzInfo = new HashMap<>();
        for (String clazz : taskClazzInfo.allClazzs()) {
            ClazzInfo clazzInfo = ClazzInfo.create(clazz);
            if (clazzInfo != null) {
                allClazzInfo.put(clazz, clazzInfo);
            }
        }

        for (Map.Entry<ResourceLocation, TaskClazzInfo.ClazzTaskInfo> entry : taskClazzInfo.clazzInfoMap().entrySet()) {
            ResourceLocation taskUid = entry.getKey();
            TaskClazzInfo.ClazzTaskInfo value = entry.getValue();
            Mods bindMod = Mods.by(value.bindMod());
            boolean modLoaded = bindMod.versionLoaded;
            if (!modLoaded) {
                continue;
            }
            TaskClazzInfo.ClazzInfo clazzInfo = value.clazzInfo();
            ClassAnalysisResult result = new ClassAnalysisResult(taskUid, bindMod.modId, ModUtil.getModVersion(bindMod.modId));
            result.classes.addAll(clazzInfo.classes());
            result.methods.addAll(clazzInfo.methods());
            result.fields.addAll(clazzInfo.fields());
            boolean verifyResult = verify(result, allClazzInfo);
            multiClassAnalysisResult.addClassResult(result);
            taskResult.put(taskUid, verifyResult);
        }

        // 导出为文本文件
        Path path = multiClassAnalysisResult.exportToFile();
        MaidsoulKitchen.LOGGER.info("任务分析报告已导出至: {}", path.toAbsolutePath());
        return taskResult;
    }

    // 验证类、方法和字段的存在性
    public static boolean verify(ClassAnalysisResult result, Map<String, ClazzInfo> allClazzInfo) {
        boolean result0 = true;
        for (String className : result.classes) {
            ClazzInfo clazzInfo = allClazzInfo.get(className);
            if (clazzInfo != null) {
                result.classExistence.put(className, true);
            } else {
                result0 = false;
                result.classExistence.put(className, false);
                result.addLog(new LogEntry(LogLevel.WARNING, "类不存在: " + className));
            }
        }

        // 验证方法存在性
        for (String methodSignature : result.methods) {
            String[] parts = methodSignature.split("#");
            if (parts.length != 2) {
                result.methodExistence.put(methodSignature, false);
                result.addLog(new LogEntry(LogLevel.WARNING, "无效的方法签名: " + methodSignature));
                continue;
            }

            String className = parts[0];
            String methodAllName = parts[1];

            ClazzInfo clazzInfo = allClazzInfo.get(className);
            if (clazzInfo != null) {
                List<String> allMethods = clazzInfo.methods;
                boolean contains = allMethods.stream()
                        .anyMatch(m -> m.endsWith(methodAllName));
                result.methodExistence.put(methodSignature, contains);

                if (!contains) {
                    result0 = false;
                    result.addLog(new LogEntry(LogLevel.WARNING, "方法不存在: " + methodSignature));
                }
            } else {
                result0 = false;
                result.classExistence.put(className, false);
                result.addLog(new LogEntry(LogLevel.WARNING, "类不存在: " + className));
            }
        }

        // 验证字段存在性
        for (String fieldSignature : result.fields) {
            String[] parts = fieldSignature.split("#");
            if (parts.length != 2) {
                result.fieldExistence.put(fieldSignature, false);
                result.addLog(new LogEntry(LogLevel.WARNING, "无效的字段签名: " + fieldSignature));
                continue;
            }

            String className = parts[0];
            String fieldName = parts[1];

            ClazzInfo clazzInfo = allClazzInfo.get(className);
            if (clazzInfo != null) {
                List<String> allFields = clazzInfo.fields();
                boolean contains = allFields.contains(fieldName);
                result.fieldExistence.put(fieldSignature, contains);

                if (!contains) {
                    result0 = false;
                    result.addLog(new LogEntry(LogLevel.WARNING, "字段不存在: " + fieldSignature));
                }
            } else {
                result0 = false;
                result.fieldExistence.put(fieldSignature, false);
                result.addLog(new LogEntry(LogLevel.WARNING, "类不存在，无法验证字段: " + className));
            }
        }
        return result0;
    }

    /**
     * 获取目标类及其所有父类、接口的所有方法和构造器
     */
    private static List<String> getAllMethodsIncludingInherited(Class<?> targetClass) {
        Set<String> members = new LinkedHashSet<>();
        Deque<Class<?>> classesToProcess = new LinkedList<>();
        Set<Class<?>> processedClasses = new HashSet<>();

        classesToProcess.add(targetClass);

        while (!classesToProcess.isEmpty()) {
            Class<?> currentClass = classesToProcess.pop();
            if (currentClass == null || processedClasses.contains(currentClass)) {
                continue;
            }
            processedClasses.add(currentClass);

            // 添加构造器
            Arrays.stream(currentClass.getDeclaredConstructors())
                    .map(c -> SignatureConverter.toASMString(c))
                    .forEach(members::add);

            // 添加方法
            Arrays.stream(currentClass.getDeclaredMethods())
                    .map(m -> SignatureConverter.toASMString(m))
                    .forEach(members::add);

            // 添加父类
            if (currentClass.getSuperclass() != null) {
                classesToProcess.add(currentClass.getSuperclass());
            }

            // 添加接口
            classesToProcess.addAll(Arrays.asList(currentClass.getInterfaces()));
        }

        return new ArrayList<>(members);
    }

    /**
     * 获取目标类及其所有父类的所有属性（包括私有、受保护）
     */
    private static List<String> getAllFieldsIncludingInherited(Class<?> targetClass) {
        Set<String> fields = new LinkedHashSet<>(); // 使用LinkedHashSet保持顺序且去重
        Class<?> currentClass = targetClass;
        while (currentClass != null) {
            Arrays.stream(currentClass.getDeclaredFields())
                    .map(Field::getName)
                    .forEach(fields::add);
            currentClass = currentClass.getSuperclass();
        }
        return new ArrayList<>(fields);
    }

    public record ClazzInfo(List<String> methods, List<String> fields) {
        public static ClazzInfo create(String clazzName) {
            try {
                ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                Class<?> aClass = Class.forName(clazzName, false, classLoader);
                List<String> allMethods = getAllMethodsIncludingInherited(aClass);
                List<String> allFields = getAllFieldsIncludingInherited(aClass);
                return new ClazzInfo(allMethods, allFields);
            } catch (ClassNotFoundException e) {
                return null;
            }
        }
    }
}
