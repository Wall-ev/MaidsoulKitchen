package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.TaskMixinAnalyzer;
import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import net.minecraft.resources.ResourceLocation;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.github.wallev.maidsoulkitchen.util.classana.clazz.ClassAnalyzerManager.FILE_NAME;

public class ClassAnalyzerTool {
    private static final String origin = "mcp";
    private static final String target = "srg";

    public static void analyzerAndGenerateFile(Path rootOutputFolder, ClassAnalyzerManager.ClassMap classMap) throws Exception {
        Map<ResourceLocation, TaskClazzInfo.ClazzTaskInfo> map = new HashMap<>();
        for (Map.Entry<TaskInfo, Set<Class<?>>> entry : classMap.getMap().entrySet()) {
            Set<Class<?>> classes = entry.getValue();
            TaskClazzInfo.ClazzInfo clazzInfo = analyze(classes);
            TaskInfo taskInfo = entry.getKey();
            TaskClazzInfo.ClazzTaskInfo clazzTaskInfo = TaskClazzInfo.ClazzTaskInfo.create(taskInfo, clazzInfo);
            map.put(taskInfo.getUid(), clazzTaskInfo);
        }
        TaskMixinAnalyzer.ModTaskMixinMap modTaskMixinMap = TaskMixinAnalyzer.collectModTaskClazz();

        TaskClazzInfo taskClazzInfo = new TaskClazzInfo(map, modTaskMixinMap);

        TaskClazzInfo.CODEC.encodeStart(JsonOps.INSTANCE, taskClazzInfo)
                .resultOrPartial(error -> {
                    MaidsoulKitchen.LOGGER.error("Build failed：{}", error);
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
                        MaidsoulKitchen.LOGGER.info("Build succeed：{}", file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    // 分析多个类
    private static TaskClazzInfo.ClazzInfo analyze(Set<Class<?>> targetClasses) throws Exception {
        ClazzInfoRuntime clazzInfoRuntime = new ClazzInfoRuntime();

        for (Class<?> clazz : targetClasses) {
            analyzeSingleClass(clazz, clazzInfoRuntime);
        }

        return clazzInfoRuntime.toClazzInfo();
    }

    /**
     *     public static String getSrgName(INameMappingService.Domain domain, String name) {
     *         return Launcher.INSTANCE.environment()
     *                 .findNameMapping(target)
     *                 .map((f) -> {
     *                     String apply = f.apply(domain, name);
     *                     return apply;
     *                 }).orElse(name);
     *     }
     *
     *     public static String getClassSrgName(String name) {
     *         return getSrgName(INameMappingService.Domain.CLASS, name);
     *     }
     *
     *     public static String getMethodSrgName(String name) {
     *         return getSrgName(INameMappingService.Domain.METHOD, name);
     *     }
     *
     *     public static String getFieldSrgName(String name) {
     *         return getSrgName(INameMappingService.Domain.FIELD, name);
     *     }
     */

    /**
     * 分析单个类，提取类的字段、方法等信息，并递归分析内部类
     */
    private static void analyzeSingleClass(Class<?> clazz, ClazzInfoRuntime clazzInfoRuntime) throws IOException, ClassNotFoundException {
        // 分析类的字段
        for (Field field : clazz.getDeclaredFields()) {
            if (ClassAnalyzerManager.ClassMap.isAllowed(field.getDeclaringClass().getName())) {
                String fieldName = field.getDeclaringClass().getName() + "#" + field.getName();
                clazzInfoRuntime.addField(fieldName);
            }
        }

        // 分析类的方法
        ClassReader cr = new ClassReader(clazz.getName());
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor,
                                             String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name,
                                                String descriptor, boolean isInterface) {
                        String className = owner.replace('/', '.');
                        if (ClassAnalyzerManager.ClassMap.isAllowed(className)) {
                            String methodName = className + "#" + name + descriptor;
                            boolean isMcMethod = false;
                            try {
                                isMcMethod = McMethodOrFieldVerify.isMcMethod(className, name + descriptor);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            if (isMcMethod) {
                                return;
                            }

                            clazzInfoRuntime.addClazz(className);
                            clazzInfoRuntime.addMethod(methodName);
                        }
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        String className = owner.replace('/', '.');
                        if (ClassAnalyzerManager.ClassMap.isAllowed(className)) {
                            String fieldName = className + "#" + name;
                            boolean isMcFiled = false;
                            try {
                                isMcFiled = McMethodOrFieldVerify.isMcField(className, fieldName);
                            } catch (ClassNotFoundException e) {
                                throw new RuntimeException(e);
                            }
                            if (isMcFiled) {
                                return;
                            }
                            clazzInfoRuntime.addClazz(className);
                            clazzInfoRuntime.addField(fieldName);
                        }
                    }

                    @Override
                    public void visitTypeInsn(int opcode, String type) {
                        String className = type.replace('/', '.');

                        if (ClassAnalyzerManager.ClassMap.isAllowed(className)) {
                        }
                    }
                };
            }
        }, 0);

        // 递归分析内部类
        for (Class<?> innerClass : findAllInnerClasses(clazz)) {
            analyzeSingleClass(innerClass, clazzInfoRuntime);
        }
    }

    // 查找所有内部类
    private static Set<Class<?>> findAllInnerClasses(Class<?> clazz) throws IOException, ClassNotFoundException {
        Set<Class<?>> innerClasses = new LinkedHashSet<>();

        // 通过反射获取内部类
        for (Class<?> declaredClass : clazz.getDeclaredClasses()) {
            innerClasses.add(declaredClass);
            innerClasses.addAll(findAllInnerClasses(declaredClass));
        }

        // 扫描类路径查找匿名内部类
        String className = clazz.getName().replace('.', '/');
        URL resource = clazz.getClassLoader().getResource(className + ".class");

        if (resource != null && resource.toString().startsWith("jar:")) {
            String jarPath = resource.toString().substring(4, resource.toString().indexOf("!"));
            try (JarFile jarFile = new JarFile(jarPath.substring(5))) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(className) && entryName.contains("$") && entryName.endsWith(".class")) {
                        String innerClassName = entryName.replace('/', '.').replace(".class", "");
                        innerClasses.add(Class.forName(innerClassName));
                    }
                }
            }
        }

        return innerClasses;
    }

    private record ClazzInfoRuntime(Set<String> classes, Set<String> methods, Set<String> fields) {
        public ClazzInfoRuntime() {
            this(new HashSet<>(), new HashSet<>(), new HashSet<>());
        }

        public void addClazz(String clazzName) {
            this.classes.add(clazzName);
        }

        public void addMethod(String methodName) {
            this.methods.add(methodName);
        }

        public void addField(String fieldName) {
            this.fields.add(fieldName);
        }

        public TaskClazzInfo.ClazzInfo toClazzInfo() {
            return new TaskClazzInfo.ClazzInfo(Lists.newArrayList(classes), Lists.newArrayList(methods), Lists.newArrayList(fields));
        }
    }
}