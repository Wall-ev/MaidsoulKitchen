package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

import java.io.IOException;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * 改造后：通过ASM解析字节码获取内部类，替换原有的反射方式 clazz.getDeclaredClasses()
 * 核心优势：不加载类、不初始化类，能获取所有类型内部类，性能更高
 */
public class ASMInnerClassScanner {

    public static Set<String> findAllInnerClasses(String clazzName) throws IOException, ClassNotFoundException {
        Set<String> innerClasses = new LinkedHashSet<>();

        // ========== 核心改造：ASM方式 替换 原 clazz.getDeclaredClasses() ==========
        Set<String> innerClassNames = findInnerClassNamesByASM(clazzName);
        for (String innerClassName : innerClassNames) {
            innerClasses.add(innerClassName);
            // 递归获取 内部类的内部类（多层嵌套），逻辑和原代码一致
            innerClasses.addAll(findAllInnerClasses(innerClassName));
        }

        // ========== 保留原有的 Jar包内匿名内部类扫描逻辑 ==========
        String className = clazzName.replace('.', '/');
        URL resource = ClassLoader.getSystemResource(className + ".class");

        if (resource != null && resource.toString().startsWith("jar:")) {
            String jarPath = resource.toString().substring(4, resource.toString().indexOf("!"));
            try (JarFile jarFile = new JarFile(jarPath.substring(5))) {
                Enumeration<JarEntry> entries = jarFile.entries();
                while (entries.hasMoreElements()) {
                    JarEntry entry = entries.nextElement();
                    String entryName = entry.getName();
                    if (entryName.startsWith(className) && entryName.contains("$") && entryName.endsWith(".class")) {
                        String innerClassName = entryName.replace('/', '.').replace(".class", "");
                        innerClasses.add(innerClassName);
                    }
                }
            }
        }

        return innerClasses;
    }

    // ========== 新增核心方法：基于ASM解析class字节码，获取所有内部类的全限定名 ==========
    public static Set<String> findInnerClassNamesByASM(String clazzName) throws IOException {
        Set<String> innerClassNames = new HashSet<>();

        ClassReader classReader = new ClassReader(clazzName);
        // 自定义ASM的ClassVisitor，解析内部类表
        classReader.accept(new ClassVisitor(Opcodes.ASM9) {
            /**
             * 核心回调方法：ASM解析到 class文件的【内部类表】时，会自动调用此方法
             * 所有内部类的信息，都会通过这个方法回调返回
             * @param innerClassName 内部类的全限定名（格式：com.xxx.Outer$Inner）
             * @param outerClassName 外部类的全限定名（格式：com.xxx.Outer）
             * @param innerName      内部类的简单名，匿名内部类为null
             * @param access         内部类的访问修饰符(public/private/static等)
             */
            @Override
            public void visitInnerClass(String innerClassName, String outerClassName, String innerName, int access) {
                super.visitInnerClass(innerClassName, outerClassName, innerName, access);
                // 过滤：只保留【当前类】的直接内部类，排除其他无关内部类
                if (outerClassName != null && clazzName.equals(outerClassName.replaceAll("/", "."))) {
                    // 将ASM的路径格式 转为 类的全限定名格式（/ → .）
                    String fullInnerClassName = innerClassName.replace('/', '.');
                    innerClassNames.add(fullInnerClassName);
                }
            }
        }, 0);

        return innerClassNames;
    }
}