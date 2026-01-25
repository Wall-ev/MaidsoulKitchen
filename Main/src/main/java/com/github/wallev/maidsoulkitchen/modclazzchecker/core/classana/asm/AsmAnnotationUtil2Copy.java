package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.asm;

import org.objectweb.asm.*;

import java.io.IOException;
import java.util.*;

/**
 * ASM通用工具类 - 完整解析类上的【类注解】、【所有字段+字段注解】、【所有方法+方法注解】
 * ✅ 完整支持：基础类型、枚举类型、数组类型、嵌套注解 所有注解值解析
 * ✅ 字段/方法注解附带对应变量名/方法名
 * ✅ 提供超便捷的取值API，一行获取任意注解值
 * ✅ 不触发类初始化、高性能、无侵入
 */
public class AsmAnnotationUtil2Copy {
    // 固定ASM版本，兼容JDK8~JDK21
    private static final int ASM_VERSION = Opcodes.ASM9;

    /**
     * 核心返回实体：封装 一个类的【所有注解信息】，严格分门别类
     * 1. 类上的注解：key=注解全类名，value=注解的参数键值对
     * 2. 所有字段：key=字段名，value=该字段上的所有注解
     * 3. 所有方法：key=方法名，value=该方法上的所有注解
     */
    public static class ClassAllAnnotation {
        // 类注解：key=注解全类名，value={参数名:参数值}
        private final Map<String, Map<String, Object>> classAnnotations = new HashMap<>();
        // 字段注解：key=字段名称，value={注解全类名: {参数名:参数值}}
        private final Map<String, Map<String, Map<String, Object>>> fieldAnnotations = new HashMap<>();
        // 方法注解：key=方法名称，value={注解全类名: {参数名:参数值}}
        private final Map<String, Map<String, Map<String, Object>>> methodAnnotations = new HashMap<>();

        // ======================== 【便捷取值API - 核心】  ========================
        /**
         * 获取【类】上指定注解的指定参数值
         */
        public <T> T getClassAnnoValue(String annoFullName, String paramName, Class<T> type) {
            return getValue(classAnnotations, annoFullName, paramName, type);
        }

        /**
         * 获取【指定字段】上指定注解的指定参数值
         */
        public <T> T getFieldAnnoValue(String fieldName, String annoFullName, String paramName, Class<T> type) {
            Map<String, Map<String, Object>> fieldAnnoMap = fieldAnnotations.get(fieldName);
            return getValue(Optional.ofNullable(fieldAnnoMap).orElse(Collections.emptyMap()), annoFullName, paramName, type);
        }

        /**
         * 获取【指定方法】上指定注解的指定参数值
         */
        public <T> T getMethodAnnoValue(String methodName, String annoFullName, String paramName, Class<T> type) {
            Map<String, Map<String, Object>> methodAnnoMap = methodAnnotations.get(methodName);
            return getValue(Optional.ofNullable(methodAnnoMap).orElse(Collections.emptyMap()), annoFullName, paramName, type);
        }

        // 私有通用取值方法，做空指针安全校验，避免空指针异常
        private <T> T getValue(Map<String, Map<String, Object>> annoMap, String annoFullName, String paramName, Class<T> type) {
            Map<String, Object> paramMap = annoMap.get(annoFullName);
            if (paramMap == null || paramMap.get(paramName) == null) {
                return null;
            }
            return type.cast(paramMap.get(paramName));
        }

        // ========================  内部添加注解的方法  ========================
        void addClassAnnotation(String annoName, Map<String, Object> paramMap) {
            classAnnotations.put(annoName, paramMap);
        }

        void addFieldAnnotation(String fieldName, String annoName, Map<String, Object> paramMap) {
            fieldAnnotations.computeIfAbsent(fieldName, k -> new HashMap<>()).put(annoName, paramMap);
        }

        void addMethodAnnotation(String methodName, String annoName, Map<String, Object> paramMap) {
            methodAnnotations.computeIfAbsent(methodName, k -> new HashMap<>()).put(annoName, paramMap);
        }

        // ========================  Getter 方便查看完整数据  ========================
        public Map<String, Map<String, Object>> getClassAnnotations() {
            return Collections.unmodifiableMap(classAnnotations);
        }

        public Map<String, Map<String, Map<String, Object>>> getFieldAnnotations() {
            return Collections.unmodifiableMap(fieldAnnotations);
        }

        public Map<String, Map<String, Map<String, Object>>> getMethodAnnotations() {
            return Collections.unmodifiableMap(methodAnnotations);
        }
    }

    // ========================  核心：通用注解解析器，抽离公共逻辑，复用所有场景  ========================
    private static class AnnotationParseVisitor extends AnnotationVisitor {
        private final Map<String, Object> paramMap;

        public AnnotationParseVisitor(Map<String, Object> paramMap) {
            super(ASM_VERSION);
            this.paramMap = paramMap;
        }

        // 解析【基础类型】注解值：String/Boolean/Integer/Long/Class 等
        @Override
        public void visit(String name, Object value) {
            paramMap.put(name, value);
            super.visit(name, value);
        }

        // ✅ 补充1：解析【枚举类型】的注解值
        @Override
        public void visitEnum(String name, String descriptor, String value) {
            paramMap.put(name, value);
            super.visitEnum(name, descriptor, value);
        }

        // ✅ 补充2：解析【嵌套注解】类型的注解值
        @Override
        public AnnotationVisitor visitAnnotation(String name, String descriptor) {
            Map<String, Object> nestedAnnoMap = new HashMap<>();
            paramMap.put(name, nestedAnnoMap);
            return new AnnotationParseVisitor(nestedAnnoMap);
        }

        // ✅ 补充3：解析【数组类型】的注解值
        @Override
        public AnnotationVisitor visitArray(String name) {
            List<Object> arrayList = new ArrayList<>();
            paramMap.put(name, arrayList);
            return new AnnotationVisitor(ASM_VERSION) {
                @Override
                public void visit(String itemName, Object value) {
                    arrayList.add(value);
                    super.visit(itemName, value);
                }

                @Override
                public void visitEnum(String itemName, String descriptor, String value) {
                    arrayList.add(value);
                    super.visitEnum(itemName, descriptor, value);
                }

                @Override
                public AnnotationVisitor visitAnnotation(String itemName, String descriptor) {
                    Map<String, Object> nestedAnnoMap = new HashMap<>();
                    arrayList.add(nestedAnnoMap);
                    return new AsmAnnotationUtil2Copy.AnnotationParseVisitor(nestedAnnoMap);
                }

                @Override
                public AnnotationVisitor visitArray(String itemName) {
                    List<Object> nestedArrayList = new ArrayList<>();
                    arrayList.add(nestedArrayList);
                    return new AsmAnnotationUtil2Copy.AnnotationParseVisitor(new HashMap<>() {{ put(itemName, nestedArrayList); }});
                }
            };
        }
    }

    // ========================  对外提供的唯一核心解析方法  ========================
    /**
     * 解析指定类的【所有注解信息】- 类注解、字段注解、方法注解全部解析
     * @param targetClassFullName 目标类的全类名 (如 com.xxx.CreateStockKeeperScreenMixin)
     * @return 封装好的所有注解信息实体 ClassAllAnnotation
     * @throws IOException 字节码读取异常
     */
    public static ClassAllAnnotation parseClassAllAnnotation(String targetClassFullName) throws IOException {
        ClassAllAnnotation result = new ClassAllAnnotation();
        ClassReader cr = new ClassReader(targetClassFullName);

        cr.accept(new ClassVisitor(ASM_VERSION) {
            // ========== 1. 解析【类级别】的注解 ==========
            @Override
            public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
                String annoFullName = getAnnotationFullNameByDesc(desc);
                Map<String, Object> paramMap = new HashMap<>();
                return new AnnotationParseVisitor(paramMap) {
                    @Override
                    public void visitEnd() {
                        result.addClassAnnotation(annoFullName, paramMap);
                        super.visitEnd();
                    }
                };
            }

            // ========== 2. 解析【字段】+ 字段上的注解 ==========
            @Override
            public FieldVisitor visitField(int access, String fieldName, String desc, String signature, Object value) {
                return new FieldVisitor(ASM_VERSION) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String annoDesc, boolean visible) {
                        String annoFullName = getAnnotationFullNameByDesc(annoDesc);
                        Map<String, Object> paramMap = new HashMap<>();
                        return new AnnotationParseVisitor(paramMap) {
                            @Override
                            public void visitEnd() {
                                result.addFieldAnnotation(fieldName, annoFullName, paramMap);
                                super.visitEnd();
                            }
                        };
                    }
                };
            }

            // ========== 3. 解析【方法】+ 方法上的注解 ==========
            @Override
            public MethodVisitor visitMethod(int access, String methodName, String desc, String signature, String[] exceptions) {
                return new MethodVisitor(ASM_VERSION) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String annoDesc, boolean visible) {
                        String annoFullName = getAnnotationFullNameByDesc(annoDesc);
                        Map<String, Object> paramMap = new HashMap<>();
                        return new AnnotationParseVisitor(paramMap) {
                            @Override
                            public void visitEnd() {
                                result.addMethodAnnotation(methodName, annoFullName, paramMap);
                                super.visitEnd();
                            }
                        };
                    }
                };
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        return result;
    }

    // ========================  核心工具方法  ========================
    /**
     * ASM注解描述符 转 注解全类名
     * ASM中注解的desc格式固定为：L全类名(点换斜杠);  例：Lorg/spongepowered/asm/mixin/Shadow;
     */
    private static String getAnnotationFullNameByDesc(String annotationDesc) {
        if (annotationDesc == null || !annotationDesc.startsWith("L") || !annotationDesc.endsWith(";")) {
            return annotationDesc;
        }
        return annotationDesc.substring(1, annotationDesc.length() - 1).replace('/', '.');
    }

    // ========================  你的业务场景【测试+使用示例】- 重点参考  ========================
    public static void main(String[] args) throws IOException {
        // 你的业务类全类名
        String targetClass = "com.xxx.CreateStockKeeperScreenMixin";
        ClassAllAnnotation annotationInfo = AsmAnnotationUtil2Copy.parseClassAllAnnotation(targetClass);

        // 1. 获取类上@Mixin注解的value值 (适配单值/数组值)
        String mixinAnno = "org.spongepowered.asm.mixin.Mixin";
        Object mixinTarget = annotationInfo.getClassAnnoValue(mixinAnno, "value", Object.class);
        System.out.println("类上@Mixin注解的value值：" + mixinTarget);

        // 2. 获取字段stockKeeper上@Shadow注解的remap值
        String shadowAnno = "org.spongepowered.asm.mixin.Shadow";
        Boolean stockKeeperRemap = annotationInfo.getFieldAnnoValue("stockKeeper", shadowAnno, "remap", Boolean.class);
        System.out.println("字段stockKeeper的@Shadow(remap)值：" + stockKeeperRemap);

        // 3. 获取字段blockEntity上@Shadow注解的remap值
        Boolean blockEntityRemap = annotationInfo.getFieldAnnoValue("blockEntity", shadowAnno, "remap", Boolean.class);
        System.out.println("字段blockEntity的@Shadow(remap)值：" + blockEntityRemap);

        // 调试查看完整数据
        System.out.println("类上的所有注解：" + annotationInfo.getClassAnnotations());
        System.out.println("所有字段的注解：" + annotationInfo.getFieldAnnotations());
    }
}