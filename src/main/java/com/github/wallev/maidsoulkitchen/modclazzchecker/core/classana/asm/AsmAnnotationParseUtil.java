package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.asm;

import org.objectweb.asm.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * ASM通用工具类 - 支持解析【类注解】+【字段注解】的所有值
 * 可复用：解析 @Mixin(类上)、@Shadow(字段上) 全部场景
 */
public class AsmAnnotationParseUtil {
    // ASM版本，固定用ASM9 兼容JDK8~JDK21
    private static final int ASM_VERSION = Opcodes.ASM9;

    /**
     * 核心方法：获取【指定类】中【指定字段】上【指定注解】的所有属性值
     * @param targetClass 目标类 (如 CreateStockKeeperScreenMixin.class)
     * @param fieldName   目标字段名 (如 "stockKeeper" / "blockEntity" / "blaze")
     * @param annoFullName 注解全类名 (如 "org.spongepowered.asm.mixin.Shadow")
     * @return 注解的所有属性键值对 例：{remap=false}
     */
    public static Map<String, Object> getFieldAnnotationValues(Class<?> targetClass, String fieldName, String annoFullName) throws IOException {
        Map<String, Object> annotationValues = new HashMap<>(2);
        // ASM注解描述符固定格式：L + 全类名(点换斜杠) + ;
        String targetAnnoDesc = "L" + annoFullName.replace('.', '/') + ";";

        // 读取类的字节码流
        try (InputStream is = targetClass.getClassLoader().getResourceAsStream(
                targetClass.getName().replace('.', '/') + ".class"
        )) {
            ClassReader cr = new ClassReader(is);
            cr.accept(new ClassVisitor(ASM_VERSION) {
                /**
                 * 解析类中的【成员变量/字段】的核心方法
                 * @param access 字段修饰符 (public/private/protected/abstract等)
                 * @param name   字段名 （核心匹配值）
                 * @param desc   字段类型描述符
                 * @param signature 泛型签名
                 * @param value  字段默认值
                 */
                @Override
                public FieldVisitor visitField(int access, String name, String desc, String signature, Object value) {
                    // 只解析【我们指定的字段】 stockKeeper / blockEntity / blaze
                    if (fieldName.equals(name)) {
                        return new FieldVisitor(ASM_VERSION) {
                            /**
                             * 解析当前字段上的【注解】
                             * @param desc 注解的描述符
                             * @param visible 注解是否为运行时可见
                             */
                            @Override
                            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                                // 只解析【我们指定的注解】 @Shadow
                                if (targetAnnoDesc.equals(desc)) {
                                    return new AnnotationVisitor(ASM_VERSION) {
                                        /**
                                         * 解析注解的【属性键值对】
                                         * @param attrName 注解属性名 (如：remap)
                                         * @param attrValue 注解属性值 (如：false)
                                         */
                                        @Override
                                        public void visit(String attrName, Object attrValue) {
                                            annotationValues.put(attrName, attrValue);
                                            super.visit(attrName, attrValue);
                                        }
                                    };
                                }
                                return super.visitAnnotation(desc, visible);
                            }
                        };
                    }
                    return super.visitField(access, name, desc, signature, value);
                }
            }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);
        }
        return annotationValues;
    }
//
//    // ====================== 快捷业务方法（直接调用）======================
//    /**
//     * 快捷方法：获取你的类中 stockKeeper 字段的 @Shadow 注解所有值
//     */
//    public static Map<String, Object> getStockKeeperShadowAnnoValues() throws IOException {
//        // 1. 替换成你项目中 @Shadow 注解的【真实全类名】！！！必改
//        String shadowAnnoFullName = "org.spongepowered.asm.mixin.Shadow";
//        // 2. 目标类 + 目标字段名
//        return getFieldAnnotationValues(CreateStockKeeperScreenMixin.class, "stockKeeper", shadowAnnoFullName);
//    }
//
//    // ====================== 测试调用 ======================
//    public static void main(String[] args) throws IOException {
//        // 调用：解析stockKeeper字段的@Shadow注解
//        Map<String, Object> shadowAnnoValues = AsmAnnotationParseUtil.getStockKeeperShadowAnnoValues();
//        System.out.println("stockKeeper字段的@Shadow注解所有值：" + shadowAnnoValues);
//        // 输出结果：{remap=false} ✅ 完美解析到值
//
//        // 扩展：解析blockEntity字段的@Shadow注解
//        Map<String, Object> blockEntityAnno = getFieldAnnotationValues(CreateStockKeeperScreenMixin.class, "blockEntity", "org.spongepowered.asm.mixin.Shadow");
//        System.out.println("blockEntity字段的@Shadow注解所有值：" + blockEntityAnno);
//        // 输出结果：{remap=false}
//
//        // 扩展：解析blaze字段的@Shadow注解
//        Map<String, Object> blazeAnno = getFieldAnnotationValues(CreateStockKeeperScreenMixin.class, "blaze", "org.spongepowered.asm.mixin.Shadow");
//        System.out.println("blaze字段的@Shadow注解所有值：" + blazeAnno);
//        // 输出结果：{remap=false}
//    }
}