//package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;
//package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;
//
//import com.github.wallev.maidsoulkitchen.modclazzchecker.core.ModClazzChecker;
//import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
//import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ModTaskMixinMap;
//import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.TaskMixinAnalyzer;
//import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import com.mojang.serialization.JsonOps;
//import org.objectweb.asm.*;
//
//import java.io.*;
//import java.nio.file.*;
//import java.nio.file.attribute.BasicFileAttributes;
//import java.util.*;
//import java.util.jar.JarEntry;
//import java.util.jar.JarFile;
//
//public class ClassAnalyzerToolAllAsm {
//
//    // 存储类文件路径映射：类名 -> 字节码文件路径
//    private static final Map<String, Path> CLASS_FILES_CACHE = new HashMap<>();
//
//    public static void analyzerAndGenerateFile(Path rootOutputFolder, ClassAnalyzerManager.ClassMap classMap, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
//        // 初始化类文件缓存
//        initClassFilesCache(checkManager);
//
//        Map<ITaskInfo<?>, ClazzInfoRuntime> runtimeMap = new HashMap<>();
//
//        // 分析普通类（改为传入类名集合而非Class对象）
//        for (Map.Entry<ITaskInfo<?>, Set<String>> entry : classMap.getClassNameMap().entrySet()) {
//            Set<String> classNames = entry.getValue();
//            ClazzInfoRuntime infoRuntime = analyze(classNames, checkManager);
//            ITaskInfo<?> taskInfo = entry.getKey();
//            runtimeMap.put(taskInfo, infoRuntime);
//        }
//
//        // 分析Mixin类
//        for (Map.Entry<ITaskInfo<?>, Set<String>> entry : classMap.getMixinMap().entrySet()) {
//            ITaskInfo<?> key = entry.getKey();
//            Set<String> vals = entry.getValue();
//            ClazzInfoRuntime clazzInfoRuntime = runtimeMap.computeIfAbsent(key, (k) -> new ClazzInfoRuntime());
//
//            for (String mixinClazz : vals) {
//                analyzerFromMixinTask(checkManager, mixinClazz, clazzInfoRuntime);
//            }
//        }
//
//        // 构建输出数据
//        Map<String, TaskClazzInfo.ClazzTaskInfo> map = new HashMap<>();
//        runtimeMap.forEach((k, v) -> {
//            map.put(k.getUidStr(), TaskClazzInfo.ClazzTaskInfo.create(k, v.toClazzInfo()));
//        });
//
//        ModTaskMixinMap modTaskMixinMap = TaskMixinAnalyzer.collectModTaskClazz(checkManager);
//        TaskClazzInfo taskClazzInfo = new TaskClazzInfo(map, modTaskMixinMap);
//
//        // 生成JSON文件
//        TaskClazzInfo.CODEC.apply(checkManager.getModsCodecO()).encodeStart(JsonOps.INSTANCE, taskClazzInfo)
//                .resultOrPartial(error -> ModClazzChecker.LOGGER.error("Build failed：{}", error))
//                .ifPresent(data -> {
//                    File file = new File(rootOutputFolder.toString().replace("generated", "main") + File.separator + checkManager.getFileName());
//                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//                    try {
//                        if (!file.getParentFile().exists()) {
//                            file.getParentFile().mkdirs(); // 改为mkdirs支持多级目录
//                        }
//                        Files.writeString(file.toPath(), gson.toJson(data));
//                        ModClazzChecker.LOGGER.info("Build succeed：{}", file.getPath());
//                    } catch (IOException e) {
//                        throw new UncheckedIOException("Failed to write JSON file", e);
//                    }
//                });
//    }
//
//    /**
//     * 初始化类文件缓存，扫描所有类文件并建立类名到文件路径的映射
//     */
//    private static void initClassFilesCache(BaseClazzCheckManager<?, ?> checkManager) throws IOException {
//        CLASS_FILES_CACHE.clear();
//
//        // 扫描mod的类文件目录（根据实际情况调整路径）
//        Path classesDir = checkManager.getModClassesDir();
//        if (Files.exists(classesDir)) {
//            Files.walkFileTree(classesDir, new SimpleFileVisitor<>() {
//                @Override
//                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
//                    if (file.toString().endsWith(".class")) {
//                        // 计算类名：从classes目录到class文件的相对路径转换为类名
//                        Path relativePath = classesDir.relativize(file);
//                        String className = relativePath.toString()
//                                .replace(File.separatorChar, '.')
//                                .replace(".class", "");
//                        CLASS_FILES_CACHE.put(className, file);
//                    }
//                    return FileVisitResult.CONTINUE;
//                }
//            });
//        }
//
//        // 处理JAR文件中的类（如果有）
//        for (Path jarPath : checkManager.getModJarPaths()) {
//            if (Files.exists(jarPath)) {
//                try (JarFile jarFile = new JarFile(jarPath.toFile())) {
//                    Enumeration<JarEntry> entries = jarFile.entries();
//                    while (entries.hasMoreElements()) {
//                        JarEntry entry = entries.nextElement();
//                        if (!entry.isDirectory() && entry.getName().endsWith(".class")) {
//                            String className = entry.getName()
//                                    .replace('/', '.')
//                                    .replace(".class", "");
//                            CLASS_FILES_CACHE.put(className, jarPath);
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    /**
//     * 获取类的字节码读取器（不加载类，直接读取字节码文件）
//     */
//    private static ClassReader getClassReader(String className) throws IOException {
//        Path classPath = CLASS_FILES_CACHE.get(className);
//        if (classPath == null) {
//            throw new FileNotFoundException("Class file not found for: " + className);
//        }
//
//        // 处理普通class文件
//        if (Files.isRegularFile(classPath) && classPath.toString().endsWith(".class")) {
//            return new ClassReader(Files.readAllBytes(classPath));
//        }
//
//        // 处理JAR中的class文件
//        try (JarFile jarFile = new JarFile(classPath.toFile())) {
//            String entryName = className.replace('.', '/') + ".class";
//            JarEntry entry = jarFile.getJarEntry(entryName);
//            if (entry == null) {
//                throw new FileNotFoundException("Class entry not found in JAR: " + entryName);
//            }
//            try (InputStream is = jarFile.getInputStream(entry)) {
//                return new ClassReader(is);
//            }
//        }
//    }
//
//    /**
//     * 分析Mixin任务（完全基于ASM，不加载类）
//     */
//    private static void analyzerFromMixinTask(BaseClazzCheckManager<?, ?> checkManager, String mixinClazzName, ClazzInfoRuntime clazzInfoRuntime) throws Exception {
//        String targetMixinSource = getTargetMixinSource(mixinClazzName);
//        ClassReader cr = getClassReader(mixinClazzName);
//
//        cr.accept(new ClassVisitor(Opcodes.ASM9) {
//            @Override
//            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                return new MethodVisitor(Opcodes.ASM9) {
//                    @Override
//                    public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
//                        // 解析描述符中的类
//                        Set<String> classesFromDescriptor = parseClassesFromDescriptor(descriptor);
//                        for (String descriptorClass : classesFromDescriptor) {
//                            if (ClassAnalyzerManager.ClassMap.isAllowed(descriptorClass, checkManager)) {
//                                clazzInfoRuntime.addClazz(descriptorClass);
//                            }
//                        }
//
//                        String className = owner.replace('/', '.');
//                        // 替换Mixin类名为目标类名
//                        className = className.contains(mixinClazzName) ? targetMixinSource : className;
//                        String methodName = className + "#" + name + descriptor;
//
//                        try {
//                            if (McMethodOrFieldVerify.isMcMethod(className, name + descriptor, checkManager)) {
//                                return;
//                            }
//                        } catch (Exception e) {
//                            throw new RuntimeException(e);
//                        }
//
//                        if (ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
//                            clazzInfoRuntime.addClazz(className);
//                            clazzInfoRuntime.addMethod(methodName);
//                        }
//                    }
//
//                    @Override
//                    public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
//                        String className = owner.replace('/', '.');
//                        className = className.contains(mixinClazzName) ? targetMixinSource : className;
//
//                        if (ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
//                            String fieldName = className + "#" + name;
//                            try {
//                                if (McMethodOrFieldVerify.isMcField(className, fieldName, checkManager)) {
//                                    return;
//                                }
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//                            clazzInfoRuntime.addClazz(className);
//                            clazzInfoRuntime.addField(fieldName);
//                        }
//                    }
//                };
//            }
//        }, 0);
//    }
//
//    /**
//     * 获取Mixin注解的目标类名（基于ASM解析注解）
//     */
//    private static String getTargetMixinSource(String mixinClazzName) throws IOException {
//        ClassReader cr = getClassReader(mixinClazzName);
//        StringBuilder targetClass = new StringBuilder();
//
//        cr.accept(new ClassVisitor(Opcodes.ASM9) {
//            @Override
//            public AnnotationVisitor visitAnnotation(String descriptor, boolean visible) {
//                // 检查是否是Mixin注解
//                if ("Lorg/spongepowered/asm/mixin/Mixin;".equals(descriptor)) {
//                    return new AnnotationVisitor(Opcodes.ASM9) {
//                        @Override
//                        public void visit(String name, Object value) {
//                            if ("value".equals(name) && value instanceof String) {
//                                // 处理value属性（目标类）
//                                targetClass.append(((String) value).replace('/', '.'));
//                            } else if ("targets".equals(name) && value instanceof String) {
//                                targetClass.append(((String) value).replace('/', '.'));
//                            } else {
//                                super.visit(name, value);
//                            }
//                        }
//
//                        @Override
//                        public AnnotationVisitor visitArray(String name) {
//                            if ("value".equals(name)) {
//                                return new AnnotationVisitor(Opcodes.ASM9) {
//                                    @Override
//                                    public void visit(String nestedName, Object value) {
//                                        if (value instanceof String) {
//                                            targetClass.append(((String) value).replace('/', '.'));
//                                        }
//                                    }
//                                };
//                            }
//                            return super.visitArray(name);
//                        }
//                    };
//                }
//                return super.visitAnnotation(descriptor, visible);
//            }
//        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
//
//        if (targetClass.isEmpty()) {
//            throw new RuntimeException("Can not find target source for mixin class: " + mixinClazzName);
//        }
//        return targetClass.toString();
//    }
//
//    /**
//     * 分析多个类（基于类名）
//     */
//    private static ClazzInfoRuntime analyze(Set<String> classNames, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
//        ClazzInfoRuntime clazzInfoRuntime = new ClazzInfoRuntime();
//        Set<String> processedClasses = new HashSet<>();
//
//        for (String className : classNames) {
//            if (!processedClasses.contains(className)) {
//                analyzeSingleClass(className, clazzInfoRuntime, checkManager, processedClasses);
//
//                // 分析超类和接口
//                Set<String> superClasses = getSuperAndInterfaceClassNames(className, checkManager);
//                for (String superClass : superClasses) {
//                    if (!processedClasses.contains(superClass) && ClassAnalyzerManager.ClassMap.isAllowed(superClass, checkManager)) {
//                        analyzeSingleClass(superClass, clazzInfoRuntime, checkManager, processedClasses);
//                    }
//                }
//            }
//        }
//
//        return clazzInfoRuntime;
//    }
//
//    /**
//     * 获取类的超类和接口名（基于ASM解析）
//     */
//    private static Set<String> getSuperAndInterfaceClassNames(String className, BaseClazzCheckManager<?, ?> checkManager) throws IOException {
//        Set<String> superClassNames = new HashSet<>();
//        ClassReader cr = getClassReader(className);
//
//        cr.accept(new ClassVisitor(Opcodes.ASM9) {
//            @Override
//            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
//                // 处理超类
//                if (superName != null) {
//                    String superClassName = superName.replace('/', '.');
//                    if (superClassName.startsWith(checkManager.getModPackage())) {
//                        superClassNames.add(superClassName);
//                        // 递归获取超类的超类
//                        try {
//                            superClassNames.addAll(getSuperAndInterfaceClassNames(superClassName, checkManager));
//                        } catch (Exception e) {
//                            // 忽略找不到的类
//                            ModClazzChecker.LOGGER.warn("Failed to get super classes for: {}", superClassName, e);
//                        }
//                    }
//                }
//
//                // 处理接口
//                if (interfaces != null) {
//                    for (String interfaceName : interfaces) {
//                        String interfaceClassName = interfaceName.replace('/', '.');
//                        if (interfaceClassName.startsWith(checkManager.getModPackage())) {
//                            superClassNames.add(interfaceClassName);
//                            // 递归获取接口的超接口
//                            try {
//                                superClassNames.addAll(getSuperAndInterfaceClassNames(interfaceClassName, checkManager));
//                            } catch (Exception e) {
//                                ModClazzChecker.LOGGER.warn("Failed to get interfaces for: {}", interfaceClassName, e);
//                            }
//                        }
//                    }
//                }
//            }
//        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
//
//        return superClassNames;
//    }
//
//    /**
//     * 分析单个类（基于类名，不加载Class对象）
//     */
//    private static void analyzeSingleClass(String className, ClazzInfoRuntime clazzInfoRuntime,
//                                           BaseClazzCheckManager<?, ?> checkManager, Set<String> processedClasses) throws IOException {
//        if (!ClassAnalyzerManager.ClassMap.isAllowed(className, checkManager)) {
//            return;
//        }
//
//        processedClasses.add(className);
//        clazzInfoRuntime.addClazz(className);
//
//        try {
//            ClassReader cr = getClassReader(className);
//
//            // 分析字段（通过ASM）
//            cr.accept(new ClassVisitor(Opcodes.ASM9) {
//                @Override
//                public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
//                    String fieldName = className + "#" + name;
//                    try {
//                        if (!McMethodOrFieldVerify.isMcField(className, fieldName, checkManager)) {
//                            clazzInfoRuntime.addField(fieldName);
//
//                            // 解析字段描述符中的类
//                            Set<String> classesFromDescriptor = parseClassesFromDescriptor(descriptor);
//                            for (String descClass : classesFromDescriptor) {
//                                if (ClassAnalyzerManager.ClassMap.isAllowed(descClass, checkManager)) {
//                                    clazzInfoRuntime.addClazz(descClass);
//                                }
//                            }
//                        }
//                    } catch (Exception e) {
//                        throw new RuntimeException(e);
//                    }
//                    return super.visitField(access, name, descriptor, signature, value);
//                }
//
//                @Override
//                public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
//                    return new MethodVisitor(Opcodes.ASM9) {
//                        @Override
//                        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
//                            // 解析方法描述符中的类
//                            Set<String> classesFromDescriptor = parseClassesFromDescriptor(descriptor);
//                            for (String descClass : classesFromDescriptor) {
//                                if (ClassAnalyzerManager.ClassMap.isAllowed(descClass, checkManager)) {
//                                    clazzInfoRuntime.addClazz(descClass);
//                                }
//                            }
//
//                            String targetClassName = owner.replace('/', '.');
//                            String methodFullName = targetClassName + "#" + name + descriptor;
//
//                            try {
//                                if (McMethodOrFieldVerify.isMcMethod(targetClassName, name + descriptor, checkManager)) {
//                                    return;
//                                }
//                            } catch (Exception e) {
//                                throw new RuntimeException(e);
//                            }
//
//                            if (ClassAnalyzerManager.ClassMap.isAllowed(targetClassName, checkManager)) {
//                                clazzInfoRuntime.addClazz(targetClassName);
//                                clazzInfoRuntime.addMethod(methodFullName);
//                            }
//                        }
//
//                        @Override
//                        public void visitFieldInsn(int opcode, String owner, String name, String descriptor) {
//                            String targetClassName = owner.replace('/', '.');
//                            if (ClassAnalyzerManager.ClassMap.isAllowed(targetClassName, checkManager)) {
//                                String fieldFullName = targetClassName + "#" + name;
//                                try {
//                                    if (McMethodOrFieldVerify.isMcField(targetClassName, fieldFullName, checkManager)) {
//                                        return;
//                                    }
//                                } catch (Exception e) {
//                                    throw new RuntimeException(e);
//                                }
//                                clazzInfoRuntime.addClazz(targetClassName);
//                                clazzInfoRuntime.addField(fieldFullName);
//                            }
//                        }
//                    };
//                }
//            }, 0);
//
//            // 分析内部类
//            Set<String> innerClassNames = findAllInnerClassNames(className);
//            for (String innerClassName : innerClassNames) {
//                if (!processedClasses.contains(innerClassName)) {
//                    analyzeSingleClass(innerClassName, clazzInfoRuntime, checkManager, processedClasses);
//                }
//            }
//
//        } catch (FileNotFoundException e) {
//            ModClazzChecker.LOGGER.warn("Class file not found: {}", className);
//        }
//    }
//
//    /**
//     * 查找所有内部类名（基于文件扫描）
//     */
//    private static Set<String> findAllInnerClassNames(String className) {
//        Set<String> innerClassNames = new HashSet<>();
//        String baseClassPath = className.replace('.', '/');
//
//        // 从缓存中查找内部类（类名格式: 外部类$内部类 或 外部类$数字）
//        for (Map.Entry<String, Path> entry : CLASS_FILES_CACHE.entrySet()) {
//            String cachedClassName = entry.getKey();
//            if (cachedClassName.startsWith(className + "$") && !cachedClassName.contains("$" + "$")) {
//                innerClassNames.add(cachedClassName);
//            }
//        }
//
//        return innerClassNames;
//    }
//
//    /**
//     * 解析方法或字段描述符，提取其中涉及的所有类
//     */
//    private static Set<String> parseClassesFromDescriptor(String descriptor) {
//        Set<String> classes = new HashSet<>();
//        int index = 0;
//
//        while (index < descriptor.length()) {
//            char c = descriptor.charAt(index);
//            if (c == 'L') {
//                // 对象类型: L全限定名;
//                int end = descriptor.indexOf(';', index);
//                if (end != -1) {
//                    String className = descriptor.substring(index + 1, end).replace('/', '.');
//                    classes.add(className);
//                    index = end + 1;
//                } else {
//                    index++;
//                }
//            } else if (c == '[') {
//                // 数组类型: [元素类型
//                index++;
//            } else {
//                // 基本类型: I, J, Z, etc.
//                index++;
//            }
//        }
//
//        return classes;
//    }
//
//    /**
//     * 运行时类信息存储
//     */
//    private record ClazzInfoRuntime(Set<String> classes, Set<String> methods, Set<String> fields) {
//        public ClazzInfoRuntime() {
//            this(new HashSet<>(), new HashSet<>(), new HashSet<>());
//        }
//
//        public void addClazz(String clazzName) {
//            this.classes.add(clazzName);
//        }
//
//        public void addMethod(String methodName) {
//            this.methods.add(methodName);
//        }
//
//        public void addField(String fieldName) {
//            this.fields.add(fieldName);
//        }
//
//        public TaskClazzInfo.ClazzInfo toClazzInfo() {
//            List<String> clazzsSort = classes.stream().sorted().toList();
//            List<String> methodsSort = methods.stream().sorted().toList();
//            List<String> fieldsSort = fields.stream().sorted().toList();
//
//            return new TaskClazzInfo.ClazzInfo(clazzsSort, methodsSort, fieldsSort);
//        }
//    }
//
//    // ========== 需要适配的ClassMap接口 ==========
//    // 注意：你需要修改ClassAnalyzerManager.ClassMap类，添加以下方法：
//    // 1. getClassNameMap(): 返回 Map<ITaskInfo<?>, Set<String>> （原getMap()返回的是Class对象集合）
//    // 2. 确保isAllowed方法接收String类型的类名而非Class对象
//}