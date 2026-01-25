package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.asm;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * ASM通用工具类 - 获取类上的注解及注解参数值
 */
public class AsmAnnotationUtil {

    /**
     * 核心方法：获取指定类上，指定注解的所有参数键值对
     * @param targetClass 目标类（如CreateStockKeeperScreenMixin.class）
     * @param annotationFullName 注解全类名（如org.spongepowered.asm.mixin.Mixin）
     * @return 注解的参数键值对 Map<参数名, 参数值>
     * @throws IOException 类字节码读取异常
     */
    public static Map<String, Object> getClassAnnotationValues(String targetClass, String annotationFullName) throws IOException, ClassNotFoundException {
        Map<String, Object> annotationValues = new HashMap<>();
        ClassReader cr = new ClassReader(targetClass);

//        // 2. 创建ClassVisitor，重写注解解析方法
//        cr.accept(new ClassVisitor(Opcodes.ASM9) {
//            @Override
//            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
//                // 只处理我们目标的@Mixin注解
//                if (annotationDesc.equals(desc)) {
//                    return new AnnotationVisitor(Opcodes.ASM9) {
//                        // 解析注解的参数（包括默认参数）
//                        @Override
//                        public void visit(String name, Object value) {
//                            annotationValues.put(name, value);
//                            super.visit(name, value);
//                        }
//                    };
//                }
//                return super.visitAnnotation(desc, visible);
//            }
//        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return annotationValues;
    }

//    // ========== 快捷工具方法 ==========
//    /**
//     * 获取指定类上@Mixin注解的 value 值（你的业务专属快捷方法）
//     * @param mixinClass 被@Mixin标注的类 如 CreateStockKeeperScreenMixin.class
//     * @return @Mixin的入参 如 StockKeeperRequestScreen.class 的全类名
//     */
//    public static String getMixinTargetClassName(Class<?> mixinClass) throws IOException, ClassNotFoundException {
//        // 注意：这里替换成你项目中 @Mixin 注解的真实全类名！！！
//        String mixinAnnotationFullName = "org.spongepowered.asm.mixin.Mixin";
//        Map<String, Object> values = getClassAnnotationValues(mixinClass, mixinAnnotationFullName);
//        // @Mixin(XXX.class) 本质是 @Mixin(value=XXX.class)，参数名固定是 value
//        return (String) values.get("value");
//    }

    public record AnnotationParam(Map<String, Object> classAnnotationValues, Map<String, Object> fieldAnnotationValues,
                                  Map<String, Object> methodAnnotationValues) {

        public <T> T getClassAnnotationValue(String name, Class<T> type) {
                return type.cast(classAnnotationValues.get(name));
            }

        public <T> T getFieldAnnotationValue(String name, Class<T> type) {
                return type.cast(fieldAnnotationValues.get(name));
            }

        public <T> T getMethodAnnotationValue(String name, Class<T> type) {
                return type.cast(methodAnnotationValues.get(name));
            }
        }

//    // 测试调用
//    public static void main(String[] args) throws IOException, ClassNotFoundException {
//        // 调用：获取你的Mixin类标注的目标类
//        String targetClassName = AsmAnnotationUtil.getMixinTargetClassName(CreateStockKeeperScreenMixin.class);
//        System.out.println("@Mixin标注的目标类全类名：" + targetClassName);
//        // 输出结果：xxx.xxx.StockKeeperRequestScreen
//    }
}