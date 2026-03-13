//package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz2;
//
//import org.objectweb.asm.ClassReader;
//import org.objectweb.asm.ClassVisitor;
//import org.objectweb.asm.Opcodes;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * 使用ASM替代反射，生成与SignatureConverter格式完全一致的类成员签名
// */
//public class ASMMemberSignatureCollector {
//
//    // 存储收集到的成员签名（对应原代码的members集合）
//    private final List<String> members = new ArrayList<>();
//    // 当前解析的类名（内部名格式，如java/lang/String）
//    private String currentClassInternalName;
//
//    /**
//     * 使用ASM解析类的字节码，收集构造器和方法的签名（带类名）
//     * 对应原代码：SignatureConverter.toASMString(Constructor/Method)
//     * @param clazz 要解析的类
//     * @return 包含构造器和方法签名的列表
//     * @throws IOException IO异常
//     */
//    public List<String> collectMemberSignatures(Class<?> clazz) throws IOException {
//        // 重置集合
//        members.clear();
//
//        // 转换为ASM内部类名（如java.lang.String -> java/lang/String）
//        currentClassInternalName = clazz.getName().replace('.', '/');
//
////         获取类的字节码输入流
//        String classResourceName = currentClassInternalName + ".class";
//        try (InputStream is = clazz.getClassLoader().getResourceAsStream(classResourceName)) {
//            if (is == null) {
//                throw new IOException("无法找到类文件: " + clazz.getName());
//            }
//
//            // 使用ASM解析字节码
//            ClassReader classReader = new ClassReader(is);
//            ClassVisitor classVisitor = createClassVisitor();
//
//            // 仅解析类结构（跳过方法体、调试信息等，提升性能）
//            classReader.accept(classVisitor, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
//        }
//
//        return members;
//    }
//
//    /**
//     * 使用ASM解析类的字节码，收集构造器和方法的签名（不带类名）
//     * 对应原代码：SignatureConverter.toASMStringWithoutClazz(Constructor/Method)
//     * @param clazz 要解析的类
//     * @return 包含构造器和方法签名的列表
//     * @throws IOException IO异常
//     */
//    public List<String> collectMemberSignaturesWithoutClazz(Class<?> clazz) throws IOException {
//        // 先收集带类名的签名，再移除类名部分
//        List<String> fullSignatures = collectMemberSignatures(clazz);
//        List<String> withoutClazzSignatures = new ArrayList<>();
//
//        String classPrefix = currentClassInternalName + "#";
//        for (String signature : fullSignatures) {
//            if (signature.startsWith(classPrefix)) {
//                withoutClazzSignatures.add(signature.substring(classPrefix.length()));
//            } else {
//                withoutClazzSignatures.add(signature);
//            }
//        }
//
//        return withoutClazzSignatures;
//    }
//
//    /**
//     * 创建自定义ClassVisitor来收集构造器和方法信息
//     */
//    private ClassVisitor createClassVisitor() {
//        return new ClassVisitor(Opcodes.ASM9) {
//            /**
//             * 访问方法/构造器（ASM中构造器的name固定为<init>）
//             * @param access 访问修饰符
//             * @param name 方法/构造器名称（构造器为<init>）
//             * @param desc 方法描述符（如(Ljava/lang/String;)V）
//             * @param signature 泛型签名（本次无需处理）
//             * @param exceptions 异常类型内部名数组
//             */
//            @Override
//            public org.objectweb.asm.MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
//                String memberSignature;
//
//                if ("<init>".equals(name)) {
//                    // 处理构造器
//                    // 对应 SignatureConverter.toASMString(Constructor)
//                    memberSignature = currentClassInternalName + "#<init>" + desc;
//                } else {
//                    // 处理普通方法
//                    // 对应 SignatureConverter.toASMString(Method)
//                    memberSignature = currentClassInternalName + "#" + name + desc;
//                }
//
//                // 添加到成员列表（与原代码逻辑一致）
//                members.add(memberSignature);
//
//                // 返回null表示不访问方法内部（仅需签名信息）
//                return null;
//            }
//        };
//    }
//
//    // ------------------- 核心工具方法（与原SignatureConverter对齐） -------------------
//    /**
//     * 将Java类型转换为ASM类型描述符（完全复刻原SignatureConverter.typeToDescriptor）
//     */
//    public static String typeToDescriptor(Class<?> type) {
//        if (type == void.class) return "V";
//        if (type == boolean.class) return "Z";
//        if (type == byte.class) return "B";
//        if (type == char.class) return "C";
//        if (type == short.class) return "S";
//        if (type == int.class) return "I";
//        if (type == long.class) return "J";
//        if (type == float.class) return "F";
//        if (type == double.class) return "D";
//        if (type.isArray()) return "[" + typeToDescriptor(type.getComponentType());
//        return "L" + type.getName().replace('.', '/') + ";";
//    }
//
//    /**
//     * 构建方法描述符（完全复刻原SignatureConverter.descriptorToASM）
//     */
//    public static String descriptorToASM(Method method) {
//        StringBuilder sb = new StringBuilder("(");
//        for (Class<?> paramType : method.getParameterTypes()) {
//            sb.append(typeToDescriptor(paramType));
//        }
//        sb.append(")");
//        sb.append(typeToDescriptor(method.getReturnType()));
//        return sb.toString();
//    }
//
//    // ------------------- 测试示例 -------------------
//    public static void main(String[] args) throws IOException {
//        ASMMemberSignatureCollector collector = new ASMMemberSignatureCollector();
//
//        // 测试解析String类的签名（带类名）
//        System.out.println("=== 带类名的签名 ===");
//        List<String> fullSignatures = collector.collectMemberSignatures(String.class);
//        fullSignatures.forEach(System.out::println);
//
//        // 测试解析String类的签名（不带类名）
//        System.out.println("\n=== 不带类名的签名 ===");
//        List<String> withoutClazzSignatures = collector.collectMemberSignaturesWithoutClazz(String.class);
//        withoutClazzSignatures.forEach(System.out::println);
//    }
//}