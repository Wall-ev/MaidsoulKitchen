package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.asm.AsmAnnotationUtil2;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import org.objectweb.asm.*;
import org.objectweb.asm.commons.JSRInlinerAdapter;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * 纯ASM方式分析类信息，不加载类到JVM
 */
public class ASMClassAnalyzer {

    public static ClazzInfoRuntime analyze(Set<String> targetClassNames, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
        ClazzInfoRuntime clazzInfoRuntime = new ClazzInfoRuntime();

        // 用于记录已经分析过的类，避免重复处理
        Set<String> analyzedClasses = new HashSet<>();

        for (String className : targetClassNames) {
            if (analyzedClasses.contains(className)) {
                continue;
            }
            analyzedClasses.add(className);

            // 分析当前类
            analyzeSingleClassByASM(className, clazzInfoRuntime, checkManager, analyzedClasses);

            // 获取并分析父类和接口
            Set<String> superAndInterfaceClassNames = getSuperAndInterfaceClassNames(className, checkManager);
            for (String superOrInterfaceName : superAndInterfaceClassNames) {
                if (!analyzedClasses.contains(superOrInterfaceName) && superOrInterfaceName.startsWith(checkManager.getModPackage())) {
                    analyzedClasses.add(superOrInterfaceName);
                    analyzeSingleClassByASM(superOrInterfaceName, clazzInfoRuntime, checkManager, analyzedClasses);
                }
            }
        }

        return clazzInfoRuntime;
    }

    // 获取父类和接口的类名（纯ASM方式）
    private static Set<String> getSuperAndInterfaceClassNames(String className, BaseClazzCheckManager<?, ?> checkManager) throws Exception {
        Set<String> superClassNames = new HashSet<>();
        byte[] classBytes = getClassBytes(className);
        if (classBytes == null) {
            return superClassNames;
        }

        ClassReader cr = new ClassReader(classBytes);
        // 解析类的继承和接口信息
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                // 处理父类
                if (superName != null) {
                    String superClassName = superName.replace('/', '.');
                    if (superClassName.startsWith(checkManager.getModPackage())) {
                        superClassNames.add(superClassName);
                        // 递归获取父类的父类
                        try {
                            superClassNames.addAll(getSuperAndInterfaceClassNames(superClassName, checkManager));
                        } catch (Exception e) {
                            // 父类不存在时忽略
                        }
                    }
                }

                // 处理接口
                if (interfaces != null) {
                    for (String interfaceName : interfaces) {
                        String interfaceClassName = interfaceName.replace('/', '.');
                        if (interfaceClassName.startsWith(checkManager.getModPackage())) {
                            superClassNames.add(interfaceClassName);
                            // 递归获取接口的父接口
                            try {
                                superClassNames.addAll(getSuperAndInterfaceClassNames(interfaceClassName, checkManager));
                            } catch (Exception e) {
                                // 接口不存在时忽略
                            }
                        }
                    }
                }
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return superClassNames;
    }

    @SuppressWarnings("unchecked")
    private static String getTargetFieldOrMethodOwnerWithMixin(String fullName, String targetName, AsmAnnotationUtil2.ClassAllAnnotation classAllAnnotation) {
        Map<String, Object> fieldAnnoValue1 = classAllAnnotation.getFieldAnnoValue(fullName, Shadow.class.getName());
        Map<String, Object> fieldAnnoValue2 = classAllAnnotation.getFieldAnnoValue(fullName, Accessor.class.getName());
        if (fieldAnnoValue1 != null || fieldAnnoValue2 != null) {
            List<Type> mixins = (List<Type>) Objects.requireNonNull(classAllAnnotation.getClassAnnoValue(Mixin.class.getName())).get("value");
            for (Type mixin : mixins) {
                String targetClazz = AsmAnnotationUtil2.asmDescToClassName(mixin.toString());
                if (ClassLoader.getSystemResource(targetClazz.replace('.', '/') + ".class") != null) {
                    fullName = targetClazz + "#" + targetName;
                    return fullName;
                }
            }


            if (classAllAnnotation.getClassAnnoValue(Pseudo.class.getName()) == null) {
                return fullName;
            }
            List<String> targets = (List<String>) Objects.requireNonNull(classAllAnnotation.getClassAnnoValue(Mixin.class.getName())).get("value");
            for (String target : targets) {
                String targetClazz = AsmAnnotationUtil2.asmDescToClassName(target);
                fullName = targetClazz + "#" + targetName;
                return fullName;
            }
        }
        return fullName;
    }

    /**
     * 纯ASM方式分析单个类，不加载类
     */
    private static void analyzeSingleClassByASM(String className, ClazzInfoRuntime clazzInfoRuntime, BaseClazzCheckManager<?, ?> checkManager, Set<String> analyzedClasses) throws Exception {
        ClassReader cr = new ClassReader(className);
        AsmAnnotationUtil2.ClassAllAnnotation classAllAnnotation = AsmAnnotationUtil2.parseClassAllAnnotation(className);
        boolean isMixinClazz = classAllAnnotation.getClassAnnoValue(Mixin.class.getName()) != null;

        // 1. 分析类的字段（纯ASM方式）
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                String fieldOwner = className;
                String fieldName = fieldOwner + "#" + name;
                if (isMixinClazz) {
                    fieldName = getTargetFieldOrMethodOwnerWithMixin(fieldName, name, classAllAnnotation);
                }

                if (ClassAnalyzerManager.ClassMap.isAllowed(fieldOwner, checkManager)) {
                    clazzInfoRuntime.addField(fieldName);
                }
                return super.visitField(access, name, descriptor, signature, value);
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        // 2. 分析类的方法指令（方法调用、字段访问）
        cr.accept(new ClassVisitor(Opcodes.ASM9) {
            @Override
            public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
                // 内联JSR/RET指令，避免ASM解析异常
                mv = new JSRInlinerAdapter(mv, access, name, descriptor, signature, exceptions);

                return new MethodVisitor(Opcodes.ASM9, mv) {
                    @Override
                    public void visitMethodInsn(int opcode, String owner, String methodName, String methodDesc, boolean isInterface) {
                        super.visitMethodInsn(opcode, owner, methodName, methodDesc, isInterface);

                        // 解析方法描述符中的类
                        Set<String> classesFromDescriptor = parseClassesFromDescriptor(methodDesc);
                        for (String descClass : classesFromDescriptor) {
                            if (ClassAnalyzerManager.ClassMap.isAllowed(descClass, checkManager)) {
                                clazzInfoRuntime.addClazz(descClass);
                            }
                        }

                        String targetClassName = owner.replace('/', '.');
                        String fullMethodName = targetClassName + "#" + methodName + methodDesc;
                        if (isMixinClazz) {
                            fullMethodName = getTargetFieldOrMethodOwnerWithMixin(fullMethodName, name, classAllAnnotation);
                        }


                        // 检查是否是MC方法（改为纯字节码方式，避免类加载）
                        boolean isMcMethod = ASMMcMethodOrFieldVerify.isMcMethodByBytecode(targetClassName, methodName + methodDesc, checkManager);
                        if (isMcMethod) {
                            return;
                        }

                        if (ClassAnalyzerManager.ClassMap.isAllowed(targetClassName, checkManager)) {
                            clazzInfoRuntime.addClazz(targetClassName);
                            clazzInfoRuntime.addMethod(fullMethodName);
                        }
                    }

                    @Override
                    public void visitFieldInsn(int opcode, String owner, String fieldName, String fieldDesc) {
                        super.visitFieldInsn(opcode, owner, fieldName, fieldDesc);

                        String targetClassName = owner.replace('/', '.');
                        if (ClassAnalyzerManager.ClassMap.isAllowed(targetClassName, checkManager)) {
                            String fullFieldName = targetClassName + "#" + fieldName;
                            if (isMixinClazz) {
                                fullFieldName = getTargetFieldOrMethodOwnerWithMixin(fullFieldName, fieldName, classAllAnnotation);
                            }

                            // 检查是否是MC字段（改为纯字节码方式）
                            boolean isMcField = ASMMcMethodOrFieldVerify.isMcField(targetClassName, fullFieldName, checkManager);
                            if (isMcField) {
                                return;
                            }

                            clazzInfoRuntime.addClazz(targetClassName);
                            clazzInfoRuntime.addField(fullFieldName);
                        }
                    }
                };
            }
        }, 0);

        // 3. 分析内部类（纯ASM方式，不加载类）
        Set<String> innerClassNames = ASMInnerClassScanner.findAllInnerClasses(className);
        for (String innerClassName : innerClassNames) {
            if (!analyzedClasses.contains(innerClassName)) {
                analyzedClasses.add(innerClassName);
                analyzeSingleClassByASM(innerClassName, clazzInfoRuntime, checkManager, analyzedClasses);
            }
        }
    }
//
//    // 查找所有内部类名（纯字节码方式，不加载类）
//    private static Set<String> findAllInnerClassNames(String outerClassName) throws Exception {
//        Set<String> innerClassNames = new LinkedHashSet<>();
//
//        // 将类名转换为资源路径
//        String outerClassResource = outerClassName.replace('.', '/') + ".class";
//        URL resource = ClassLoader.getSystemClassLoader().getResource(outerClassResource);
//
//        if (resource == null) {
//            return innerClassNames;
//        }
//
//        // 处理JAR包中的类
//        if (resource.toString().startsWith("jar:")) {
//            String jarUrl = resource.toString().substring(4, resource.toString().indexOf("!"));
//            String jarPath = URLDecoder.decode(jarUrl, StandardCharsets.UTF_8);
//            // 处理file:/开头的路径
//            if (jarPath.startsWith("file:/")) {
//                jarPath = jarPath.substring(5);
//                // 处理Windows路径（如/C:/xxx）
//                if (jarPath.startsWith("/") && jarPath.length() > 2 && jarPath.charAt(2) == ':') {
//                    jarPath = jarPath.substring(1);
//                }
//            }
//
//            try (JarFile jarFile = new JarFile(new File(new URI(jarPath)))) {
//                Enumeration<JarEntry> entries = jarFile.entries();
//                String outerClassPrefix = outerClassName.replace('.', '/');
//
//                while (entries.hasMoreElements()) {
//                    JarEntry entry = entries.nextElement();
//                    String entryName = entry.getName();
//
//                    // 匹配内部类（包含$，且以外部类前缀开头，以.class结尾）
//                    if (entryName.startsWith(outerClassPrefix) && entryName.contains("$") && entryName.endsWith(".class")) {
//
//                        String innerClassName = entryName.replace('/', '.').replace(".class", "");
//                        innerClassNames.add(innerClassName);
//                    }
//                }
//            } catch (URISyntaxException e) {
//                throw new IOException("解析JAR路径失败", e);
//            }
//        }
//        // 处理文件系统中的类
//        else if (resource.toString().startsWith("file:")) {
//            File outerClassFile = new File(resource.toURI());
//            File parentDir = outerClassFile.getParentFile();
//            String outerClassNamePrefix = outerClassName.replace(".", File.separator);
//
//            if (parentDir.exists()) {
//                File[] classFiles = parentDir.listFiles((dir, name) -> name.startsWith(outerClassNamePrefix.substring(outerClassNamePrefix.lastIndexOf(File.separator) + 1) + "$") && name.endsWith(".class"));
//
//                if (classFiles != null) {
//                    for (File classFile : classFiles) {
//                        String relativePath = classFile.getAbsolutePath().substring(parentDir.getAbsolutePath().length() + 1);
//                        String innerClassName = relativePath.replace(File.separator, ".").replace(".class", "");
//                        innerClassNames.add(innerClassName);
//                    }
//                }
//            }
//        }
//
//        return innerClassNames;
//    }

    // 获取类的字节码（从类路径/JAR包读取，不加载类）
    private static byte[] getClassBytes(String className) throws IOException {
        String classResource = className.replace('.', '/') + ".class";
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(classResource)) {
            if (is == null) {
                return null;
            }
            return is.readAllBytes();
        }
    }

    // 解析方法描述符中的类名（原有逻辑保留）
    private static Set<String> parseClassesFromDescriptor(String descriptor) {
        Set<String> classNames = new HashSet<>();
        // 实现你的描述符解析逻辑，例如：
        // 解析 (Ljava/lang/String;ILcom/example/MyClass;)V 中的类名
        // 这里是示例实现，你可以替换为原有逻辑
        int index = 0;
        while (index < descriptor.length()) {
            if (descriptor.charAt(index) == 'L') {
                int endIndex = descriptor.indexOf(';', index);
                if (endIndex != -1) {
                    String internalClassName = descriptor.substring(index + 1, endIndex);
                    classNames.add(internalClassName.replace('/', '.'));
                    index = endIndex + 1;
                } else {
                    index++;
                }
            } else if (descriptor.charAt(index) == '[') {
                index++;
            } else {
                index++;
            }
        }
        return classNames;
    }

    // 占位类：保持原有代码结构兼容
    public static class ClazzInfoRuntime {
        public final Set<String> fields = new HashSet<>();
        public final Set<String> methods = new HashSet<>();
        public final Set<String> clazzes = new HashSet<>();

        public void addField(String fieldName) {
            fields.add(fieldName);
        }

        public void addMethod(String methodName) {
            methods.add(methodName);
        }

        public void addClazz(String className) {
            clazzes.add(className);
        }
    }
//
//    // 占位类：需要你将原有isMcMethod/isMcField改为纯字节码实现
//    static class McMethodOrFieldVerify {
//        public static boolean isMcMethodByBytecode(String className, String methodDesc, BaseClazzCheckManager<?, ?> checkManager) {
//            // 实现逻辑：不加载类，通过读取字节码判断是否是MC方法
//            // 你需要替换为实际的判断逻辑
//            return false;
//        }
//
//        public static boolean isMcFieldByBytecode(String className, String fieldName, BaseClazzCheckManager<?, ?> checkManager) {
//            // 实现逻辑：不加载类，通过读取字节码判断是否是MC字段
//            // 你需要替换为实际的判断逻辑
//            return false;
//        }
//    }
//
//    // 占位类：保持原有代码结构兼容
//    static class ClassAnalyzerManager {
//        static class ClassMap {
//            public static boolean isAllowed(String className, BaseClazzCheckManager<?, ?> checkManager) {
//                // 实现你的过滤逻辑
//                return className.startsWith(checkManager.getModPackage());
//            }
//        }
//    }
}
