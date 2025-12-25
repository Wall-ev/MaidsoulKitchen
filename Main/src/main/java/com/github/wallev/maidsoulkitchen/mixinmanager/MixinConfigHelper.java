package com.github.wallev.maidsoulkitchen.mixinmanager;

import org.spongepowered.asm.mixin.extensibility.IMixinConfig;

import java.lang.reflect.Method;
import java.util.List;

public class MixinConfigHelper {

    /**
     * 通过反射调用 MixinConfig 的 getClasses() 方法
     * @param config IMixinConfig 接口实例（实际是 MixinConfig 对象）
     * @return getClasses() 方法的返回值
     */
    public static List<String> getMixinClasses(IMixinConfig config) {
        // 安全校验：确保传入的实例确实是 MixinConfig 类型
        if (config == null) {
            throw new IllegalArgumentException("config 不能为 null");
        }

        Class<?> mixinConfigClass = config.getClass();
        try {
            // 1. 获取 getClasses 方法（方法名+参数类型，这里无参数）
            Method getClassesMethod = mixinConfigClass.getDeclaredMethod("getClasses");

            // 2. 突破访问权限限制（关键步骤）
            getClassesMethod.setAccessible(true);

            // 3. 调用方法并返回结果
            return (List<String>) getClassesMethod.invoke(config);

        } catch (NoSuchMethodException e) {
            throw new RuntimeException("找不到 getClasses() 方法", e);
        } catch (Exception e) {
            throw new RuntimeException("调用 getClasses() 方法失败", e);
        }
    }

    // 你的使用示例
    public static void main(String[] args) {
//        // 假设你已经拿到了 config 实例
//        IMixinConfig config = mixinConfig.getConfig();
//
//        // 调用反射方法获取结果
//        List<String> mixinClasses = getMixinClasses(config);
//
//        // 输出结果验证
//        System.out.println("Mixin 类列表：");
//        for (String clazz : mixinClasses) {
//            System.out.println(clazz);
//        }
    }
}