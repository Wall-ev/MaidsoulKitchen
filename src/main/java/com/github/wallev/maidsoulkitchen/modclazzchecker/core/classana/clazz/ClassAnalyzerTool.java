package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.ModClazzChecker;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ModTaskMixinMap;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.TaskMixinAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mojang.serialization.JsonOps;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.*;
import org.spongepowered.asm.mixin.Mixin;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Field;
import java.net.URL;
import java.nio.file.Path;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class ClassAnalyzerTool {

    public static void analyzerAndGenerateFile(Path rootOutputFolder, ClassAnalyzerManager.ClassMap classMap, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
        Map<ITaskInfo<?>, ClazzInfoRuntime> runtimeMap = new HashMap<>();

        for (Map.Entry<ITaskInfo<?>, Set<Class<?>>> entry : classMap.getMap().entrySet()) {
            Set<Class<?>> classes = entry.getValue();
            ClazzInfoRuntime infoRuntime = analyze(classes, checkManager);
            ITaskInfo<?> taskInfo = entry.getKey();
//            TaskClazzInfo.ClazzTaskInfo clazzTaskInfo = TaskClazzInfo.ClazzTaskInfo.create(taskInfo, clazzInfo);
//            map.put(taskInfo.getUidStr(), clazzTaskInfo);
            runtimeMap.put(taskInfo, infoRuntime);
        }
        for (Map.Entry<ITaskInfo<?>, Set<String>> entry : classMap.getMixinMap().entrySet()) {
            ITaskInfo<?> key = entry.getKey();
            Set<String> vals = entry.getValue();
            ClazzInfoRuntime clazzInfoRuntime = runtimeMap.computeIfAbsent(key, (k) -> {
                return new ClazzInfoRuntime();
            });
            for (String mixinClazz : vals) {
                analyzerFromMixinTask(checkManager, mixinClazz, clazzInfoRuntime);
            }
        }

        Map<String, TaskClazzInfo.ClazzTaskInfo> map = new HashMap<>();
        runtimeMap.forEach((k, v) -> {
            map.put(k.getUidStr(), TaskClazzInfo.ClazzTaskInfo.create(k, v.toClazzInfo()));
        });

        ModTaskMixinMap modTaskMixinMap = TaskMixinAnalyzer.collectModTaskClazz(checkManager);

        TaskClazzInfo taskClazzInfo = new TaskClazzInfo(map, modTaskMixinMap);

        TaskClazzInfo.CODEC.apply(checkManager.getModsCodecO()).encodeStart(JsonOps.INSTANCE, taskClazzInfo)
                .resultOrPartial(error -> {
                    ModClazzChecker.LOGGER.error("Build failed：{}", error);
                })
                .ifPresent(data -> {
                    File file = new File(rootOutputFolder.toString().replace("generated", "main") + "\\" + checkManager.getFileName());
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
                        ModClazzChecker.LOGGER.info("Build succeed：{}", file.getPath());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    /**
     * 这种接口注入还不支持分析，但会被{@link TaskMixinAnalyzer}分析处理，所以有待商榷
     * <p>
     * {@link com.github.wallev.maidsoulkitchen.mixin.compat.youkaishomecoming.KettleBlockAccessor }
     * <p>
     *     @Mixin(value = KettleBlock.class, remap = false)
     *     public interface KettleBlockAccessor {
     *         @Accessor("MAP")
     *         static Lazy<Map<Ingredient, Integer>> waters() {
     *             throw new AssertionError();
     *         }
     *    }
     *    */
    private static void analyzerFromMixinTask(BaseClazzCheckManager<?, ?> checkManager, String mixinClazzName, ClazzInfoRuntime clazzInfoRuntime) throws Exception {
        String targetMixinSource = getTargetMixinSource(mixinClazzName);

        // 分析类的方法
        ClassReader cr = new ClassReader(mixinClazzName);
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor,
                                             String signature, String[] exceptions) {
                return new MethodVisitor(Opcodes.ASM9) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String name,
                                                String descriptor, boolean isInterface) {
                        // 解析字段描述符并提取涉及的类
                        Set<String> classesFromDescriptor = parseClassesFromDescriptor(descriptor);
                        for (String descriptorClass : classesFromDescriptor) {
                            if (ClassAnalyzerManager.ClassMap.isAllowed(descriptorClass, checkManager)) {
                                clazzInfoRuntime.addClazz(descriptorClass);
                            }
                        }


                        String replace = owner.replace('/', '.');
                        String className = replace.contains(mixinClazzName) ? targetMixinSource : replace;
                        String methodName = className + "#" + name + descriptor;
                        boolean isMcMethod = false;
                        try {
                            isMcMethod = McMethodOrFieldVerify.isMcMethod(className, name + descriptor, checkManager);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (isMcMethod) {
                            return;
                        }

                        if (ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
                            clazzInfoRuntime.addClazz(className);
                            clazzInfoRuntime.addMethod(methodName);
                        }
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        String replace = owner.replace('/', '.');
                        String className = replace.contains(mixinClazzName) ? targetMixinSource : replace;
                        if (ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
                            String fieldName = className + "#" + name;
                            boolean isMcFiled = false;
                            try {
                                isMcFiled = McMethodOrFieldVerify.isMcField(className, fieldName, checkManager);
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
                };
            }
        }, 0);

    }

    @SuppressWarnings({"SameParameterValue", "unchecked"})
    private static String getTargetMixinSource(String mixinClazzName) {
        String targetMixinSource = "";

        Type mixinType = Type.getType(Mixin.class);
        breakScan:
        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                Type annotationedType = data.annotationType();
                if (annotationedType.equals(mixinType)) {
                    String mixinClazz = data.memberName();
                    if (mixinClazz.equals(mixinClazzName)) {
                        if (data.annotationData().get("value") != null) {
                            String string = ((List<?>) data.annotationData().get("value")).get(0).toString();
                            string = string.substring(1, string.length() - 1);
                            targetMixinSource = string.replace("/", ".");
                            break breakScan;
                        }

                        if (data.annotationData().get("targets") != null) {
                            List<String> targets = (List<String>) data.annotationData().get("targets");
                            for (String target : targets) {
                                targetMixinSource = target.replace("/", ".");
                                break breakScan;
                            }
                        }
                    }
                }
            }
        }
        if (targetMixinSource.isEmpty()) {
            throw new RuntimeException("can not find" + mixinClazzName + " target source");
        }
        return targetMixinSource;
    }

    // 分析多个类
    private static ClazzInfoRuntime analyze(Set<Class<?>> targetClasses, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
        ClazzInfoRuntime clazzInfoRuntime = new ClazzInfoRuntime();

        for (Class<?> clazz : targetClasses) {
            analyzeSingleClass(clazz, clazzInfoRuntime, checkManager);
            for (Class<?> superAndInterfaceClazz : getSuperAndInterfaceClazzs(clazz, checkManager)) {
                analyzeSingleClass(superAndInterfaceClazz, clazzInfoRuntime, checkManager);
            }
        }

        return clazzInfoRuntime;
    }

    private static Set<Class<?>> getSuperAndInterfaceClazzs(Class<?> clazz, BaseClazzCheckManager<?, ?> checkManager) {
        final String modPackage = checkManager.getModPackage();
        Set<Class<?>> superClazzs = new HashSet<>();
        Class<?> superClazz = clazz.getSuperclass();
        if (superClazz != null && superClazz.getName().startsWith(modPackage)) {
            superClazzs.add(superClazz);
            superClazzs.addAll(getSuperAndInterfaceClazzs(superClazz, checkManager));
        }
        Class<?>[] interfaceClazzs = clazz.getInterfaces();
        for (Class<?> interfaceClazz : interfaceClazzs) {
            if (interfaceClazz.getName().startsWith(modPackage)) {
                superClazzs.add(interfaceClazz);
                superClazzs.addAll(getSuperAndInterfaceClazzs(interfaceClazz, checkManager));
            }
        }


        return superClazzs;
    }

    /**
     * 分析单个类，提取类的字段、方法等信息，并递归分析内部类
     */
    private static void analyzeSingleClass(Class<?> clazz, ClazzInfoRuntime clazzInfoRuntime, BaseClazzCheckManager<?, ?> checkManager) throws IOException, ClassNotFoundException {
        // 分析类的字段
        for (Field field : clazz.getDeclaredFields()) {
            if (ClassAnalyzerManager.ClassMap.isAllowed(field.getDeclaringClass().getName(), checkManager)) {
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
                        // 解析字段描述符并提取涉及的类
                        Set<String> classesFromDescriptor = parseClassesFromDescriptor(descriptor);
                        for (String descriptorClass : classesFromDescriptor) {
                            if (ClassAnalyzerManager.ClassMap.isAllowed(descriptorClass, checkManager)) {
                                clazzInfoRuntime.addClazz(descriptorClass);
                            }
                        }


                        String className = owner.replace('/', '.');
                        String methodName = className + "#" + name + descriptor;
                        boolean isMcMethod = false;
                        try {
                            isMcMethod = McMethodOrFieldVerify.isMcMethod(className, name + descriptor, checkManager);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        if (isMcMethod) {
                            return;
                        }

                        if (ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
                            clazzInfoRuntime.addClazz(className);
                            clazzInfoRuntime.addMethod(methodName);
                        }
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
                        String className = owner.replace('/', '.');
                        if (ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
                            String fieldName = className + "#" + name;
                            boolean isMcFiled = false;
                            try {
                                isMcFiled = McMethodOrFieldVerify.isMcField(className, fieldName, checkManager);
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
                };
            }
        }, 0);

        // 递归分析内部类
        for (Class<?> innerClass : findAllInnerClasses(clazz)) {
            analyzeSingleClass(innerClass, clazzInfoRuntime, checkManager);
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

    /**
     * 解析方法或字段描述符，提取其中涉及的所有类
     *
     * @param descriptor 方法或字段描述符
     * @return 描述符中涉及的类名集合
     */
    private static Set<String> parseClassesFromDescriptor(String descriptor) {
        Set<String> classes = new HashSet<>();

        // 处理方法描述符格式: (参数类型)返回类型
        if (descriptor.startsWith("(")) {
            int paramEnd = descriptor.indexOf(')');
            String paramPart = descriptor.substring(1, paramEnd);
            String returnPart = descriptor.substring(paramEnd + 1);

            // 解析参数类型
            parseTypes(paramPart, classes);

            // 解析返回类型
            parseTypes(returnPart, classes);
        } else {
            // 处理字段描述符（直接是类型描述符）
            parseTypes(descriptor, classes);
        }

        return classes;
    }

    /**
     * 解析类型描述符，提取其中涉及的类
     *
     * @param typeDescriptor 类型描述符
     * @param classes 结果集合，用于存储提取的类名
     */
    private static void parseTypes(String typeDescriptor, Set<String> classes) {
        int index = 0;
        while (index < typeDescriptor.length()) {
            char c = typeDescriptor.charAt(index);
            if (c == 'L') {
                // 对象类型: L全限定名;
                int end = typeDescriptor.indexOf(';', index);
                if (end != -1) {
                    String className = typeDescriptor.substring(index + 1, end).replace('/', '.');
                    classes.add(className);
                    index = end + 1;
                } else {
                    // 无效的描述符格式
                    index++;
                }
            } else if (c == '[') {
                // 数组类型: [元素类型
                index++;
            } else {
                // 基本类型: I, J, Z, etc.
                index++;
            }
        }
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
            List<String> clazzsSort = classes.stream().sorted().toList();
            List<String> methodsSort = methods.stream().sorted().toList();
            List<String> fieldsSort = fields.stream().sorted().toList();

            return new TaskClazzInfo.ClazzInfo(clazzsSort, methodsSort, fieldsSort);
        }
    }
}