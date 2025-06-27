package com.github.wallev.maidsoulkitchen.util.classana.clazz;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import java.util.*;

public class McMethodOrFieldVerify {
    private static final Set<String> MINECRAFT_GROUP = Sets.newHashSet("net.minecraft", "net.minecraftforge", "net.neoforged");

    private static final Map<String, Map<String, List<String>>> METHOD_MAP = new HashMap<>();
    private static final Map<String, Map<String, List<String>>> FIELD_MAP = new HashMap<>();

    static boolean isMcMethod(String targetClazzName, String methodName) throws ClassNotFoundException {
        Map<String, List<String>> allMethodsIncludingInherited = getAllMethodsIncludingInherited(targetClazzName);
        for (Map.Entry<String, List<String>> entry : allMethodsIncludingInherited.entrySet()) {
            String clazz = entry.getKey();
            List<String> value = entry.getValue();
            for (String s : value) {
                if (s.equals(methodName) && isMinecraftMethodOrField(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取目标类及其所有父类、接口的所有方法和构造器
     */
    static Map<String, List<String>> getAllMethodsIncludingInherited(String targetClass) throws ClassNotFoundException {
        String targetClassName = targetClass;
        if (METHOD_MAP.containsKey(targetClassName)) {
            return METHOD_MAP.get(targetClassName);
        }

        Map<String, List<String>> map = new HashMap<>();

        Set<String> members = new LinkedHashSet<>();
        Deque<Class<?>> classesToProcess = new LinkedList<>();
        Set<Class<?>> processedClasses = new HashSet<>();

        classesToProcess.add(Class.forName(targetClass));

        while (!classesToProcess.isEmpty()) {
            Class<?> currentClass = classesToProcess.pop();
            String name = currentClass.getName();
            if (map.containsKey(name)) {
                continue;
            }

            if (currentClass == null || processedClasses.contains(currentClass)) {
                continue;
            }
            processedClasses.add(currentClass);

            Set<String> sets = new HashSet<>();
            // 添加构造器
            List<String> list = Arrays.stream(currentClass.getDeclaredConstructors())
                    .map(c -> SignatureConverter.toASMStringWithoutClazz(c))
                    .toList();
            sets.addAll(list);

            // 添加方法
            List<String> list1 = Arrays.stream(currentClass.getDeclaredMethods())
                    .map(m -> SignatureConverter.toASMStringWithoutClazz(m))
                    .toList();
            sets.addAll(list1);
            map.put(name, Lists.newArrayList(sets));

            // 添加父类
            if (currentClass.getSuperclass() != null) {
                classesToProcess.add(currentClass.getSuperclass());
            }

            // 添加接口
            classesToProcess.addAll(Arrays.asList(currentClass.getInterfaces()));
        }

        METHOD_MAP.put(targetClassName, map);

        return map;
    }


    static boolean isMcField(String targetClazzName, String fieldName) throws ClassNotFoundException {
        Map<String, List<String>> allMethodsIncludingInherited = getAllFieldsIncludingInherited(targetClazzName);
        for (Map.Entry<String, List<String>> entry : allMethodsIncludingInherited.entrySet()) {
            String clazz = entry.getKey();
            List<String> value = entry.getValue();
            for (String s : value) {
                if (s.equals(fieldName) && isMinecraftMethodOrField(clazz)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取目标类及其所有父类、接口的所有方法和构造器
     */
    static Map<String, List<String>> getAllFieldsIncludingInherited(String targetClass) throws ClassNotFoundException {
        String targetClassName = targetClass;
        if (FIELD_MAP.containsKey(targetClassName)) {
            return FIELD_MAP.get(targetClassName);
        }

        Map<String, List<String>> map = new HashMap<>();

        Set<String> members = new LinkedHashSet<>();
        Deque<Class<?>> classesToProcess = new LinkedList<>();
        Set<Class<?>> processedClasses = new HashSet<>();

        classesToProcess.add(Class.forName(targetClass));

        while (!classesToProcess.isEmpty()) {
            Class<?> currentClass = classesToProcess.pop();
            String name = currentClass.getName();
            if (map.containsKey(name)) {
                continue;
            }

            if (currentClass == null || processedClasses.contains(currentClass)) {
                continue;
            }
            processedClasses.add(currentClass);

            Set<String> sets = new HashSet<>();
            // 添加方法
            List<String> list1 = Arrays.stream(currentClass.getFields())
                    .map(field -> {
                        return field.getName();
                    })
                    .toList();
            sets.addAll(list1);
            map.put(name, Lists.newArrayList(sets));

            // 添加父类
            if (currentClass.getSuperclass() != null) {
                classesToProcess.add(currentClass.getSuperclass());
            }

            // 添加接口
            classesToProcess.addAll(Arrays.asList(currentClass.getInterfaces()));
        }

        FIELD_MAP.put(targetClassName, map);

        return map;
    }

    private static boolean isMinecraftMethodOrField(String declaredClazz) {
        for (String s : MINECRAFT_GROUP) {
            if (declaredClazz.startsWith(s)) {
                return true;
            }
        }
        return false;
    }
}
