package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class ASMMcMethodOrFieldVerify {
    private static final Map<String, Map<String, List<String>>> METHOD_MAP = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> FIELD_MAP = new HashMap<>();

    // 用于缓存已解析的类继承关系，避免重复解析
    private static final Map<String, Set<String>> CLASS_HIERARCHY_CACHE = new HashMap<>();

    /**
     * 纯ASM方式判断是否是MC方法（不加载类）
     */
    static boolean isMcMethodByBytecode(String targetClazzName, String methodName, BaseClazzCheckManager<?, ?> checkManager) {
        try {
            Map<String, List<String>> allMethodsIncludingInherited = getAllMethodsIncludingInheritedByASM(targetClazzName);
            for (Map.Entry<String, List<String>> entry : allMethodsIncludingInherited.entrySet()) {
                String clazz = entry.getKey();
                if (!isMinecraftClazz(clazz, checkManager)) {
                    continue;
                }
                List<String> value = entry.getValue();
                for (String s : value) {
                    if (s.equals(methodName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // 类字节码不存在时返回false
            return false;
        }
        return false;
    }

    /**
     * 纯ASM方式获取目标类及其所有父类、接口的所有方法和构造器（不加载类）
     */
    static Map<String, List<String>> getAllMethodsIncludingInheritedByASM(String targetClass) throws IOException {
        // 检查缓存
        if (METHOD_MAP.containsKey(targetClass)) {
            return METHOD_MAP.get(targetClass);
        }

        Map<String, List<String>> map = new HashMap<>();
        Deque<String> classesToProcess = new LinkedList<>();
        Set<String> processedClasses = new HashSet<>();

        classesToProcess.add(targetClass);

        while (!classesToProcess.isEmpty()) {
            String currentClassName = classesToProcess.pop();

            // 跳过已处理的类或无法获取字节码的类
            if (map.containsKey(currentClassName) || processedClasses.contains(currentClassName)) {
                continue;
            }

            byte[] classBytes = getClassBytes(currentClassName);
            if (classBytes == null) {
                processedClasses.add(currentClassName);
                continue;
            }

            processedClasses.add(currentClassName);
            Set<String> methodSignatures = new HashSet<>();

            // 使用ASM解析类的方法和构造器
            ClassReader cr = new ClassReader(classBytes);
            cr.accept(new ClassVisitor(Opcodes.ASM9) {
                // 解析构造器
                @Override
                public MethodVisitor visitMethod(int access, String name, String descriptor,
                                                 String signature, String[] exceptions) {
                    // <init> 是构造器，<clinit> 是静态初始化块
                    if ("<init>".equals(name)) {
                        // 构造器签名：方法名+描述符
                        methodSignatures.add(name + descriptor);
                    } else if (!"<clinit>".equals(name)) {
                        // 普通方法签名：方法名+描述符
                        methodSignatures.add(name + descriptor);
                    }
                    return super.visitMethod(access, name, descriptor, signature, exceptions);
                }

                // 解析类的父类和接口，用于递归处理
                @Override
                public void visit(int version, int access, String name, String signature,
                                  String superName, String[] interfaces) {
                    // 处理父类
                    if (superName != null) {
                        String superClassName = superName.replace('/', '.');
                        if (!processedClasses.contains(superClassName)) {
                            classesToProcess.add(superClassName);
                        }
                    }

                    // 处理接口
                    if (interfaces != null) {
                        for (String interfaceName : interfaces) {
                            String interfaceClassName = interfaceName.replace('/', '.');
                            if (!processedClasses.contains(interfaceClassName)) {
                                classesToProcess.add(interfaceClassName);
                            }
                        }
                    }
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            // 将解析到的方法签名存入map
            map.put(currentClassName, new ArrayList<>(methodSignatures));
        }

        // 缓存结果
        METHOD_MAP.put(targetClass, map);
        return map;
    }

    /**
     * 纯ASM方式判断是否是MC字段（不加载类）
     */
    static boolean isMcFieldByBytecode(String targetClazzName, String fieldName, BaseClazzCheckManager<?, ?> checkManager) {
        // 提取字段名（去掉类名前缀）
        String pureFieldName = fieldName.contains("#") ? fieldName.split("#")[1] : fieldName;

        try {
            Map<String, List<String>> allFieldsIncludingInherited = getAllFieldsIncludingInheritedByASM(targetClazzName);
            for (Map.Entry<String, List<String>> entry : allFieldsIncludingInherited.entrySet()) {
                String clazz = entry.getKey();
                if (!isMinecraftClazz(clazz, checkManager)) {
                    continue;
                }
                List<String> value = entry.getValue();
                for (String s : value) {
                    if (s.equals(pureFieldName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            // 类字节码不存在时返回false
            return false;
        }
        return false;
    }

    /**
     * 纯ASM方式获取目标类及其所有父类、接口的所有字段（不加载类）
     */
    static Map<String, List<String>> getAllFieldsIncludingInheritedByASM(String targetClass) throws IOException {
        // 检查缓存
        if (FIELD_MAP.containsKey(targetClass)) {
            return FIELD_MAP.get(targetClass);
        }

        Map<String, List<String>> map = new HashMap<>();
        Deque<String> classesToProcess = new LinkedList<>();
        Set<String> processedClasses = new HashSet<>();

        classesToProcess.add(targetClass);

        while (!classesToProcess.isEmpty()) {
            String currentClassName = classesToProcess.pop();

            // 跳过已处理的类或无法获取字节码的类
            if (map.containsKey(currentClassName) || processedClasses.contains(currentClassName)) {
                continue;
            }

            byte[] classBytes = getClassBytes(currentClassName);
            if (classBytes == null) {
                processedClasses.add(currentClassName);
                continue;
            }

            processedClasses.add(currentClassName);
            Set<String> fieldNames = new HashSet<>();

            // 使用ASM解析类的字段
            ClassReader cr = new ClassReader(classBytes);
            cr.accept(new ClassVisitor(Opcodes.ASM9) {
                // 解析字段
                @Override
                public FieldVisitor visitField(int access, String name, String descriptor,
                                               String signature, Object value) {
                    fieldNames.add(name);
                    return super.visitField(access, name, descriptor, signature, value);
                }

                // 解析类的父类和接口，用于递归处理
                @Override
                public void visit(int version, int access, String name, String signature,
                                  String superName, String[] interfaces) {
                    // 处理父类
                    if (superName != null) {
                        String superClassName = superName.replace('/', '.');
                        if (!processedClasses.contains(superClassName)) {
                            classesToProcess.add(superClassName);
                        }
                    }

                    // 处理接口
                    if (interfaces != null) {
                        for (String interfaceName : interfaces) {
                            String interfaceClassName = interfaceName.replace('/', '.');
                            if (!processedClasses.contains(interfaceClassName)) {
                                classesToProcess.add(interfaceClassName);
                            }
                        }
                    }
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

            // 将解析到的字段名存入map
            map.put(currentClassName, new ArrayList<>(fieldNames));
        }

        // 缓存结果
        FIELD_MAP.put(targetClass, map);
        return map;
    }

    /**
     * 获取类的字节码（从类路径/JAR包读取，不加载类）
     */
    private static byte[] getClassBytes(String className) throws IOException {
        String classResource = className.replace('.', '/') + ".class";
        try (InputStream is = ClassLoader.getSystemClassLoader().getResourceAsStream(classResource)) {
            if (is == null) {
                return null;
            }
            return is.readAllBytes();
        }
    }

    /**
     * 判断类是否是Minecraft类（原有逻辑保留）
     */
    private static boolean isMinecraftClazz(String declaredClazz, BaseClazzCheckManager<?, ?> checkManager) {
        if (checkManager == null || checkManager.getMcGroups() == null) {
            return false;
        }
        for (String s : checkManager.getMcGroups()) {
            if (declaredClazz.startsWith(s)) {
                return true;
            }
        }
        return false;
    }

    // 兼容原有方法名（可选，用于过渡）
    @Deprecated
    static boolean isMcMethod(String targetClazzName, String methodName, BaseClazzCheckManager<?, ?> checkManager) {
        return isMcMethodByBytecode(targetClazzName, methodName, checkManager);
    }

    // 兼容原有方法名（可选，用于过渡）
    @Deprecated
    static boolean isMcField(String targetClazzName, String fieldName, BaseClazzCheckManager<?, ?> checkManager) {
        return isMcFieldByBytecode(targetClazzName, fieldName, checkManager);
    }
}