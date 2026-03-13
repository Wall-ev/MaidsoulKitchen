package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.ModClazzChecker;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.IMskMixinInterface;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ITaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.util.ModUtil;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;
import org.objectweb.asm.*;
import org.objectweb.asm.signature.SignatureReader;
import org.objectweb.asm.signature.SignatureVisitor;
import org.objectweb.asm.signature.SignatureWriter;
import org.objectweb.asm.tree.ClassNode;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.*;

public class VerifyExistence {
    private static final Marker MARKER = MarkerManager.getMarker("VerifyExistence");
    private static final int ASM_VERSION = Opcodes.ASM9;

    // 验证类、方法和字段的存在性
    public static Map<String, Boolean> verify(TaskClazzInfo taskClazzInfo, BaseClazzCheckManager<?, ?> checkManager) throws IOException {
        MultiClassAnalysisResult multiClassAnalysisResult = new MultiClassAnalysisResult();
        Map<String, Boolean> taskResult = new HashMap<>();
        Map<String, List<String>> mixinList = taskClazzInfo.taskMixinMap().getMixinList();

        Map<String, ClazzInfo> allClazzInfo = new HashMap<>();
        for (String clazz : taskClazzInfo.allClazzs()) {
            ClazzInfo clazzInfo = ClazzInfo.create(clazz);
            if (clazzInfo != null) {
                allClazzInfo.put(clazz, clazzInfo);
            }
        }

        List<ClassAnalysisResult> errorClassResults = new ArrayList<>();
        for (Map.Entry<String, TaskClazzInfo.ClazzTaskInfo> entry : taskClazzInfo.clazzInfoMap().entrySet()) {
            String taskUid = entry.getKey();
            TaskClazzInfo.ClazzTaskInfo value = entry.getValue();
            ITaskInfo<?> task = checkManager.taskInfoByUid(taskUid);
            if (task == null || !task.canLoadWithoutCheckClazz()) {
                continue;
            }
            IMods bindMod = task.getBindMod();
            TaskClazzInfo.ClazzInfo clazzInfo = value.clazzInfo();
            ClassAnalysisResult result = new ClassAnalysisResult(taskUid, bindMod.modId(), ModUtil.getModVersion(bindMod.modId()));
            result.classes.addAll(clazzInfo.classes());
            result.methods.addAll(clazzInfo.methods());
            result.fields.addAll(clazzInfo.fields());
            result.mixins.addAll(mixinList.getOrDefault(taskUid, List.of()));
            boolean verifyResult = verify(result, allClazzInfo);
            multiClassAnalysisResult.addClassResult(result);
            taskResult.put(taskUid, verifyResult);
            if (!verifyResult) {
                checkManager.addErrorTask(taskUid);
                errorClassResults.add(result);
            }
        }

        Path path = multiClassAnalysisResult.exportToFile(allClazzInfo, errorClassResults, checkManager);
        ModClazzChecker.LOGGER.info("The task analysis report has been exported to: {}", path.toAbsolutePath());
        return taskResult;
    }

    // 验证类、方法和字段的存在性以及mixin成功与否
    public static boolean verify(ClassAnalysisResult result, Map<String, ClazzInfo> allClazzInfo) {
        boolean result0 = true;
        for (String mixin : result.mixins) {
            boolean applied = IMskMixinInterface.applyInterfaceMixin(mixin, result);
            if (applied) {
                result.mixinExistence.put(mixin, true);
            } else {
                result0 = false;
                result.mixinExistence.put(mixin, false);
                result.addLog(mixin, new LogEntry(LogLevel.ERROR, "Mixin failed: " + mixin));
            }
        }

        for (String className : result.classes) {
            ClazzInfo clazzInfo = allClazzInfo.get(className);
            if (clazzInfo != null) {
                result.classExistence.put(className, true);
            } else {
                result0 = false;
                result.classExistence.put(className, false);
                result.addLog(className, new LogEntry(LogLevel.WARNING, "The class does not exist: " + className));
            }
        }

        // 验证方法存在性（修复泛型匹配逻辑）
        for (String methodSignature : result.methods) {
            String[] parts = methodSignature.split("#");
            if (parts.length != 2) {
                result.methodExistence.put(methodSignature, false);
                result.addLog(methodSignature, new LogEntry(LogLevel.WARNING, "Invalid method signature: " + methodSignature));
                continue;
            }

            String className = parts[0];
            String methodAllName = parts[1];

            ClazzInfo clazzInfo = allClazzInfo.get(className);
            if (clazzInfo != null) {
                List<String> allMethods = clazzInfo.methods;
                boolean contains = allMethods.stream()
                        .anyMatch(m -> {
                            // 匹配标准签名 或 泛型签名转换后的标准格式
                            if (m.endsWith(methodAllName)) {
                                return true;
                            }
                            // 处理泛型签名格式：类名#方法名 泛型签名
                            if (m.contains("#") && m.contains(" ")) {
                                String[] mParts = m.split("#");
                                if (mParts.length == 2) {
                                    String methodPart = mParts[1];
                                    String[] methodSigParts = methodPart.split(" ", 2);
                                    if (methodSigParts.length == 2) {
                                        // 将泛型签名转换为标准描述符
                                        String standardDesc = genericSignatureToDesc(methodSigParts[1]);
                                        String fullMethodDesc = methodSigParts[0] + standardDesc;
                                        return fullMethodDesc.equals(methodAllName);
                                    }
                                }
                            }
                            return false;
                        });

                result.methodExistence.put(methodSignature, contains);
                if (!contains) {
                    result0 = false;
                    result.addLog(methodSignature, new LogEntry(LogLevel.WARNING, "The method does not exist: " + methodSignature));
                }
            } else {
                result0 = false;
                result.classExistence.put(className, false);
                result.addLog(className, new LogEntry(LogLevel.WARNING, "The class does not exist: " + className));
            }
        }

        // 验证字段存在性
        for (String fieldSignature : result.fields) {
            String[] parts = fieldSignature.split("#");
            if (parts.length != 2) {
                result.fieldExistence.put(fieldSignature, false);
                result.addLog(fieldSignature, new LogEntry(LogLevel.WARNING, "Invalid field signature: " + fieldSignature));
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
                    result.addLog(fieldSignature, new LogEntry(LogLevel.WARNING, "The field does not exist: " + fieldSignature));
                }
            } else {
                result0 = false;
                result.fieldExistence.put(fieldSignature, false);
                result.addLog(fieldSignature, new LogEntry(LogLevel.WARNING, "The class doesn't exist and the field can't be validated:" + className));
            }
        }

        return result0;
    }

    /**
     * 将ASM泛型签名转换为标准的ASM方法描述符（擦除泛型）。
     * 修复：移除了不存在的visitMethodType()方法调用
     */
    private static String genericSignatureToDesc(String genericSignature) {
        SignatureWriter sw = new SignatureWriter();
        SignatureReader sr = new SignatureReader(genericSignature);

        sr.accept(new SignatureVisitor(ASM_VERSION) {
            @Override
            public void visitFormalTypeParameter(String name) {
                // 忽略泛型参数定义
            }

            @Override
            public SignatureVisitor visitReturnType() {
                return new TypeErasureSignatureVisitor(sw.visitReturnType());
            }

            @Override
            public SignatureVisitor visitParameterType() {
                return new TypeErasureSignatureVisitor(sw.visitParameterType());
            }
        });

        return sw.toString();
    }

    /**
     * 签名访问器：执行类型擦除，忽略泛型参数（如 <T>）。
     * 修复：构造器只保留ASM版本参数
     */
    private static class TypeErasureSignatureVisitor extends SignatureVisitor {
        private final SignatureVisitor delegate;

        public TypeErasureSignatureVisitor(SignatureVisitor delegate) {
            super(ASM_VERSION);
            this.delegate = delegate;
        }

        @Override
        public void visitBaseType(char descriptor) {
            delegate.visitBaseType(descriptor);
        }

        @Override
        public void visitClassType(String name) {
            delegate.visitClassType(name);
        }

        @Override
        public SignatureVisitor visitArrayType() {
            return delegate.visitArrayType();
        }

        @Override
        public SignatureVisitor visitTypeArgument(char wildcard) {
            // 返回空的SignatureVisitor，忽略泛型参数
            return new SignatureVisitor(ASM_VERSION) {};
        }

        @Override
        public void visitEnd() {
            delegate.visitEnd();
        }
    }

    /**
     * 替换泛型签名中的类型变量。
     * 修复：SignatureVisitor构造器只传ASM_VERSION参数
     */
    private static String replaceGenericVariables(String signature, Map<String, String> mapping) {
        if (mapping.isEmpty()) return signature;

        SignatureWriter sw = new SignatureWriter();
        SignatureReader sr = new SignatureReader(signature);

        sr.accept(new SignatureVisitor(ASM_VERSION) {
            @Override
            public void visitTypeVariable(String name) {
                String mapped = mapping.get(name);
                if (mapped != null) {
                    // 替换为实际类型
                    visitClassType(mapped);
                    visitEnd();
                } else {
                    super.visitTypeVariable(name);
                }
            }

            // 转发所有其他方法调用到SignatureWriter
            @Override
            public void visitBaseType(char descriptor) {
                sw.visitBaseType(descriptor);
            }

            @Override
            public void visitClassType(String name) {
                sw.visitClassType(name);
            }

            @Override
            public SignatureVisitor visitArrayType() {
                sw.visitArrayType();
                return sw.visitArrayType();
            }

            @Override
            public SignatureVisitor visitTypeArgument(char wildcard) {
                return sw.visitTypeArgument(wildcard);
            }

            @Override
            public void visitFormalTypeParameter(String name) {
                sw.visitFormalTypeParameter(name);
            }

            @Override
            public SignatureVisitor visitReturnType() {
                return sw.visitReturnType();
            }

            @Override
            public SignatureVisitor visitParameterType() {
                return sw.visitParameterType();
            }

            @Override
            public void visitEnd() {
                sw.visitEnd();
            }
        });

        return sw.toString();
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
     * 获取目标类及其所有父类、接口的所有方法和构造器（支持泛型）
     */
    private static List<String> getAllMethodsIncludingInherited1(Class<?> targetClass) {
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

            String className = currentClass.getName().replace('.', '/');
            Map<String, String> genericMapping = parseClassGenericMapping(currentClass);

            boolean read = true;

            try (InputStream is = targetClass.getClassLoader().getResourceAsStream(className + ".class")) {
                if (is != null) {
                    ClassReader classReader = new ClassReader(is);
                    classReader.accept(new ClassVisitor(ASM_VERSION) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            String methodSignature;
                            if (signature != null && !genericMapping.isEmpty()) {
                                String resolvedSignature = replaceGenericVariables(signature, genericMapping);
                                methodSignature = className + "#" + name + " " + resolvedSignature;
                            } else if (signature != null) {
                                methodSignature = className + "#" + name + " " + signature;
                            } else {
                                methodSignature = className + "#" + name + descriptor;
                            }
                            members.add(methodSignature);
                            return null;
                        }
                    }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                } else {
                    read = false;
                }
            } catch (IOException e) {
                read = false;
            }

            if (!read) {
                try {
                    ClassReader classReader = new ClassReader(className);
                    classReader.accept(new ClassVisitor(ASM_VERSION) {
                        @Override
                        public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
                            String methodSignature;
                            if (signature != null && !genericMapping.isEmpty()) {
                                String resolvedSignature = replaceGenericVariables(signature, genericMapping);
                                methodSignature = className + "#" + name + " " + resolvedSignature;
                            } else if (signature != null) {
                                methodSignature = className + "#" + name + " " + signature;
                            } else {
                                methodSignature = className + "#" + name + descriptor;
                            }
                            members.add(methodSignature);
                            return null;
                        }
                    }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
                } catch (IOException e) {
                    MaidsoulKitchen.LOGGER.error(MARKER, "{} class not found", className);
                    e.printStackTrace();
                }
            }

            if (currentClass.getSuperclass() != null) {
                classesToProcess.add(currentClass.getSuperclass());
            }
            classesToProcess.addAll(Arrays.asList(currentClass.getInterfaces()));
        }

        return new ArrayList<>(members);
    }

    /**
     * 解析类的泛型参数映射关系（如 R -> CookingPotRecipe）
     */
    private static Map<String, String> parseClassGenericMapping(Class<?> clazz) {
        Map<String, String> genericMapping = new HashMap<>();
        String className = clazz.getName().replace('.', '/');
//
//        try (InputStream is = clazz.getClassLoader().getResourceAsStream(className + ".class")) {
//            if (is == null) {
//                return genericMapping;
//            }

        ClassReader classReader = null;
        try {
            classReader = new ClassReader(clazz.getName());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        classReader.accept(new ClassVisitor(ASM_VERSION) {
                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    if (signature == null) {
                        return;
                    }

                    SignatureReader sr = new SignatureReader(signature);
                    sr.accept(new SignatureVisitor(ASM_VERSION) {
                        @Override
                        public SignatureVisitor visitInterface() {
                            return new InterfaceGenericParser(genericMapping);
                        }
                    });
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
//        } catch (IOException e) {
//            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to parse generic mapping for {}", className, e);
//        }

        return genericMapping;
    }

    /**
     * 解析一个接口的正式泛型参数名（如 IFdCbeAccessor<R> 中的 R）。
     * 修复：visitFormalTypeParameter 返回正确的 SignatureVisitor 类型
     */
    private static List<String> parseInterfaceFormalParams(String interfaceInternalName) {
        List<String> params = new ArrayList<>();
        try (InputStream is = VerifyExistence.class.getClassLoader().getResourceAsStream(interfaceInternalName + ".class")) {
            if (is == null) return params;

            ClassReader cr = new ClassReader(is);
            cr.accept(new ClassVisitor(ASM_VERSION) {
                @Override
                public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                    if (signature == null) return;
                    SignatureReader sr = new SignatureReader(signature);
                    sr.accept(new SignatureVisitor(ASM_VERSION) {
                        @Override
                        public void visitFormalTypeParameter(String name) {
                            params.add(name);
                            // 返回空的SignatureVisitor，避免返回值类型不匹配
                        }
                    });
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        } catch (IOException e) {
            MaidsoulKitchen.LOGGER.error(MARKER, "Failed to parse formal params for {}", interfaceInternalName, e);
        }
        return params;
    }

    /**
     * 专门用于解析接口泛型参数的 Visitor。
     */
    private static class InterfaceGenericParser extends SignatureVisitor {
        private final Map<String, String> resultMapping;
        private String currentInterfaceName;
        private final List<String> typeArguments = new ArrayList<>();

        public InterfaceGenericParser(Map<String, String> resultMapping) {
            super(ASM_VERSION);
            this.resultMapping = resultMapping;
        }

        @Override
        public void visitClassType(String name) {
            this.currentInterfaceName = name;
        }

        @Override
        public SignatureVisitor visitTypeArgument(char wildcard) {
            return new SignatureVisitor(ASM_VERSION) {
                @Override
                public void visitClassType(String name) {
                    typeArguments.add(name);
                }

                @Override
                public void visitTypeVariable(String name) {
                    typeArguments.add(name);
                }
            };
        }

        @Override
        public void visitEnd() {
            if (currentInterfaceName == null || typeArguments.isEmpty()) return;

            List<String> interfaceParamNames = parseInterfaceFormalParams(currentInterfaceName);
            if (interfaceParamNames.size() != typeArguments.size()) return;

            for (int i = 0; i < interfaceParamNames.size(); i++) {
                resultMapping.put(interfaceParamNames.get(i), typeArguments.get(i));
            }
        }
    }

    /**
     * 获取目标类及其所有父类的所有属性（包括私有、受保护）
     */
    private static List<String> getAllFieldsIncludingInherited(Class<?> targetClass) {
        Set<String> fields = new LinkedHashSet<>();
        Class<?> currentClass = targetClass;
        while (currentClass != null) {
            boolean read = true;
            String className = currentClass.getName().replace('.', '/');
            try (InputStream is = targetClass.getClassLoader().getResourceAsStream(className + ".class")) {
                if (is != null) {
                    ClassReader classReader = new ClassReader(is);
                    classReader.accept(new ClassVisitor(ASM_VERSION) {
                        @Override
                        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                            fields.add(name);
                            return super.visitField(access, name, descriptor, signature, value);
                        }
                    }, ClassReader.SKIP_DEBUG);
                } else {
                    read = false;
                }
            } catch (IOException e) {
                read = false;
            }

            if (!read) {
                try {
                    ClassReader classReader = new ClassReader(className);
                    classReader.accept(new ClassVisitor(ASM_VERSION) {
                        @Override
                        public FieldVisitor visitField(int access, String name, String descriptor, String signature, Object value) {
                            fields.add(name);
                            return super.visitField(access, name, descriptor, signature, value);
                        }
                    }, ClassReader.SKIP_DEBUG);
                } catch (IOException e) {
                    MaidsoulKitchen.LOGGER.error(MARKER, "{} class not found", className);
                    e.printStackTrace();
                }
            }

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