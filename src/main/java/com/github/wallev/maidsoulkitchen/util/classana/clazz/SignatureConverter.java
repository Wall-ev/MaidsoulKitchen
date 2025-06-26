package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class SignatureConverter {
    /**
     * 将Constructor对象转换为ASM格式的构造器签名
     */
    public static String toASMString(Constructor<?> constructor) {
        String className = constructor.getDeclaringClass().getName().replace('.', '/');
        StringBuilder descriptor = new StringBuilder("(");

        // 添加参数类型描述符
        for (Class<?> paramType : constructor.getParameterTypes()) {
            descriptor.append(typeToDescriptor(paramType));
        }

        descriptor.append(")V"); // 构造器返回类型始终是void

        return className + "#<init>" + descriptor.toString();
    }

    /**
     * 将Method对象转换为ASM格式的方法签名
     */
    public static String toASMString(Method method) {
        // 获取类的全限定名并转换为内部名
        String className = method.getDeclaringClass().getName().replace('.', '/');

        // 构建方法描述符
        String descriptor = descriptorToASM(method);

        // 组合完整签名
        return className + "#" + method.getName() + descriptor;
    }

    /**
     * 构建方法描述符，格式为：(参数类型)返回类型
     */
    public static String descriptorToASM(Method method) {
        StringBuilder sb = new StringBuilder("(");

        // 添加参数类型描述符
        for (Class<?> paramType : method.getParameterTypes()) {
            sb.append(typeToDescriptor(paramType));
        }

        sb.append(")");

        // 添加返回类型描述符
        sb.append(typeToDescriptor(method.getReturnType()));

        return sb.toString();
    }

    /**
     * 将Java类型转换为ASM类型描述符
     */
    public static String typeToDescriptor(Class<?> type) {
        if (type == void.class) return "V";
        if (type == boolean.class) return "Z";
        if (type == byte.class) return "B";
        if (type == char.class) return "C";
        if (type == short.class) return "S";
        if (type == int.class) return "I";
        if (type == long.class) return "J";
        if (type == float.class) return "F";
        if (type == double.class) return "D";
        if (type.isArray()) return "[" + typeToDescriptor(type.getComponentType());
        return "L" + type.getName().replace('.', '/') + ";";
    }
}
