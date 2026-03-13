package com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.asm;

import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.*;

import java.io.IOException;
import java.util.*;

/**
 * ASM通用工具类 - 完整解析类上的【类注解】、【所有字段+字段注解】、【所有方法+方法注解】
 * ✅ 严格匹配格式：类名/字段(全类名#字段名)/方法(全类名#方法名+描述符)
 * ✅ 完整支持：基础类型、枚举、数组、嵌套注解 所有注解值解析
 * ✅ 提供超便捷的取值API，一行获取任意注解值
 * ✅ 不触发类初始化、高性能、无侵入
 */
public class AsmAnnotationUtil2 {
    // 固定ASM版本，兼容JDK8~JDK21
    private static final int ASM_VERSION = Opcodes.ASM9;
    private static final Map<String, ClassAllAnnotation> CACHE = new HashMap<>();

    /**
     * 核心返回实体：封装 一个类的【所有注解信息】，严格分门别类
     * 1. 类注解：key=类的全限定名 如 studio.fantasyit.maid_storage_manager.Config
     * 2. 字段注解：key=字段全限定名 如 类全名#字段名
     * 3. 方法注解：key=方法全限定签名 如 类全名#方法名(描述符)
     */
    public static class ClassAllAnnotation {
        // 当前解析的类的全限定名
        private String currentClassFullName;
        // 类上的注解：key=注解全类名，value=注解的参数键值对
        private final Map<String, Map<String, Object>> classAnnotations = new HashMap<>();
        // 字段的注解：key=全限定字段名 类全名#字段名，value={注解全类名: {参数名:参数值}}
        private final Map<String, Map<String, Map<String, Object>>> fieldAnnotations = new HashMap<>();
        // 方法的注解：key=全限定方法签名 类全名#方法名(描述符)，value={注解全类名: {参数名:参数值}}
        private final Map<String, Map<String, Map<String, Object>>> methodAnnotations = new HashMap<>();

        // ======================== 【便捷取值API - 核心 一行取值】  ========================
        /**
         * 获取【类】上指定注解的指定参数值
         * @param annoFullName 注解全类名 (如 org.spongepowered.asm.mixin.Mixin)
         * @param paramName    注解的参数名 (如 value/remap)
         * @param type         返回值类型
         */
        public <T> T getClassAnnoValue(String annoFullName, String paramName, Class<T> type) {
            return getValue(classAnnotations, annoFullName, paramName, type);
        }

        @Nullable
        public Map<String, Object> getClassAnnoValue(String annoFullName) {
            return classAnnotations.getOrDefault(annoFullName, null);
        }

        @Nullable
        public Map<String, Object> getFieldAnnoValue(String annoFullName, String annotationKey) {
            if (fieldAnnotations.containsKey(annoFullName)) {
                return fieldAnnotations.get(annoFullName).getOrDefault(annotationKey, null);
            }
            return null;
        }

        @Nullable
        public Map<String, Object> getMethodAnnoValue(String annoFullName, String annotationKey) {
            if (methodAnnotations.containsKey(annoFullName)) {
                return methodAnnotations.get(annoFullName).getOrDefault(annotationKey, null);
            }
            return null;
        }

        /**
         * 获取【指定字段】上指定注解的指定参数值
         * 支持两种传参方式：1.传纯字段名 2.传全限定字段名(类全名#字段名)
         */
        public <T> T getFieldAnnoValue(String fieldName, String annoFullName, String paramName, Class<T> type) {
            String targetFieldKey = fieldName.contains("#") ? fieldName : (currentClassFullName + "#" + fieldName);
            Map<String, Map<String, Object>> fieldAnnoMap = fieldAnnotations.get(targetFieldKey);
            return getValue(Optional.ofNullable(fieldAnnoMap).orElse(Collections.emptyMap()), annoFullName, paramName, type);
        }

        /**
         * 获取【指定方法】上指定注解的指定参数值
         * 支持两种传参方式：1.传纯方法名 2.传全限定方法签名(类全名#方法名+描述符)
         */
        public <T> T getMethodAnnoValue(String methodKey, String annoFullName, String paramName, Class<T> type) {
            String targetMethodKey = methodKey.contains("#") ? methodKey : (currentClassFullName + "#" + methodKey);
            Map<String, Map<String, Object>> methodAnnoMap = methodAnnotations.get(targetMethodKey);
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

        // ========================  内部赋值&添加注解的方法  ========================
        void setCurrentClassFullName(String currentClassFullName) {
            this.currentClassFullName = currentClassFullName;
        }

        void addClassAnnotation(String annoName, Map<String, Object> paramMap) {
            classAnnotations.put(annoName, paramMap);
        }

        void addFieldAnnotation(String fieldSimpleName, String annoName, Map<String, Object> paramMap) {
            String fullFieldName = currentClassFullName + "#" + fieldSimpleName;
            fieldAnnotations.computeIfAbsent(fullFieldName, k -> new HashMap<>()).put(annoName, paramMap);
        }

        void addMethodAnnotation(String methodSimpleName, String desc, String annoName, Map<String, Object> paramMap) {
            String fullMethodSign = currentClassFullName + "#" + methodSimpleName + desc;
            methodAnnotations.computeIfAbsent(fullMethodSign, k -> new HashMap<>()).put(annoName, paramMap);
        }

        // ========================  Getter 方便查看完整数据  ========================
        public String getCurrentClassFullName() {
            return currentClassFullName;
        }

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

        // 解析【枚举类型】的注解值
        @Override
        public void visitEnum(String name, String descriptor, String value) {
            paramMap.put(name, value);
            super.visitEnum(name, descriptor, value);
        }

        // 解析【嵌套注解】类型的注解值
        @Override
        public AnnotationVisitor visitAnnotation(String name, String descriptor) {
            Map<String, Object> nestedAnnoMap = new HashMap<>();
            paramMap.put(name, nestedAnnoMap);
            return new AnnotationParseVisitor(nestedAnnoMap);
        }

        // 解析【数组类型】的注解值
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
                    return new AnnotationParseVisitor(nestedAnnoMap);
                }

                @Override
                public AnnotationVisitor visitArray(String itemName) {
                    List<Object> nestedArrayList = new ArrayList<>();
                    arrayList.add(nestedArrayList);
                    return new AnnotationParseVisitor(new HashMap<>() {{ put(itemName, nestedArrayList); }});
                }
            };
        }
    }

    // ========================  对外提供的唯一核心解析方法  ========================
    /**
     * 解析指定类的【所有注解信息】- 类注解、字段注解、方法注解全部解析
     * @param targetClassFullName 目标类的全类名 (如 studio.fantasyit.maid_storage_manager.Config)
     * @return 封装好的所有注解信息实体 ClassAllAnnotation
     * @throws IOException 字节码读取异常
     */
    public static ClassAllAnnotation parseClassAllAnnotation(String targetClassFullName) throws IOException {
        // 从缓存中获取
        if (CACHE.containsKey(targetClassFullName)) {
            return CACHE.get(targetClassFullName);
        }

        ClassAllAnnotation result = new ClassAllAnnotation();
        ClassReader cr = new ClassReader(targetClassFullName);

        cr.accept(new ClassVisitor(ASM_VERSION) {
            // 第一步：获取当前类的全限定名，赋值到返回实体中
            @Override
            public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                // ASM内部类名是斜杠分隔，转为点分隔的标准全类名
                String classFullName = name.replace("/", ".");
                result.setCurrentClassFullName(classFullName);
                super.visit(version, access, name, signature, superName, interfaces);
            }

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

            // ========== 2. 解析【字段】+ 字段上的注解 生成格式：类全名#字段名 ==========
            @Override
            public FieldVisitor visitField(int access, String fieldSimpleName, String desc, String signature, Object value) {
                return new FieldVisitor(ASM_VERSION) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String annoDesc, boolean visible) {
                        String annoFullName = getAnnotationFullNameByDesc(annoDesc);
                        Map<String, Object> paramMap = new HashMap<>();
                        return new AnnotationParseVisitor(paramMap) {
                            @Override
                            public void visitEnd() {
                                result.addFieldAnnotation(fieldSimpleName, annoFullName, paramMap);
                                super.visitEnd();
                            }
                        };
                    }
                };
            }

            // ========== 3. 解析【方法】+ 方法上的注解 生成格式：类全名#方法名(描述符) ==========
            @Override
            public MethodVisitor visitMethod(int access, String methodSimpleName, String desc, String signature, String[] exceptions) {
                return new MethodVisitor(ASM_VERSION) {
                    @Override
                    public AnnotationVisitor visitAnnotation(String annoDesc, boolean visible) {
                        String annoFullName = getAnnotationFullNameByDesc(annoDesc);
                        Map<String, Object> paramMap = new HashMap<>();
                        return new AnnotationParseVisitor(paramMap) {
                            @Override
                            public void visitEnd() {
                                result.addMethodAnnotation(methodSimpleName, desc, annoFullName, paramMap);
                                super.visitEnd();
                            }
                        };
                    }
                };
            }
        }, ClassReader.SKIP_CODE | ClassReader.SKIP_DEBUG | ClassReader.SKIP_FRAMES);

        // 缓存结果
        CACHE.put(targetClassFullName, result);
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

    // ========================  你的业务场景【测试+使用示例】- 严格匹配你的格式  ========================
    public static void main(String[] args) throws IOException {
        // 测试1：你的类名格式示例
        String testClass = "studio.fantasyit.maid_storage_manager.Config";
        // 测试2：你的字段格式示例
        String testField = "studio.fantasyit.maid_storage_manager.integration.create.StockManagerInteract#lastInteractedMaidId";
        // 测试3：你的方法签名格式示例
        String testMethod = "studio.fantasyit.maid_storage_manager.integration.create.StockManagerInteract#setInteractedMaidId(I)V";

        // 解析类
        ClassAllAnnotation annotationInfo = AsmAnnotationUtil2.parseClassAllAnnotation(testClass);

        // 1. 获取类注解值
        String mixinAnno = "org.spongepowered.asm.mixin.Mixin";
        Object mixinTarget = annotationInfo.getClassAnnoValue(mixinAnno, "value", Object.class);
        System.out.println("类注解值：" + mixinTarget);

        // 2. 获取字段注解值 - 两种方式都可以✅
        String shadowAnno = "org.spongepowered.asm.mixin.Shadow";
        Boolean fieldValue1 = annotationInfo.getFieldAnnoValue("lastInteractedMaidId", shadowAnno, "remap", Boolean.class);
        Boolean fieldValue2 = annotationInfo.getFieldAnnoValue(testField, shadowAnno, "remap", Boolean.class);
        System.out.println("字段注解值：" + fieldValue1);

        // 3. 获取方法注解值 - 两种方式都可以✅
        Boolean methodValue = annotationInfo.getMethodAnnoValue(testMethod, "org.xxx.Anno", "param", Boolean.class);
        System.out.println("方法注解值：" + methodValue);

        // 调试查看完整格式化数据
        System.out.println("当前解析的类名：" + annotationInfo.getCurrentClassFullName());
        System.out.println("全格式字段注解：" + annotationInfo.getFieldAnnotations());
        System.out.println("全格式方法注解：" + annotationInfo.getMethodAnnotations());
    }

    /**
     * ASM标准类描述符 转 Java全限定类名 (万能容错版，生产推荐)
     * 支持：标准描述符、空值、null、非L开头/非;结尾的非法值，均安全返回
     * @param asmDesc ASM类描述符 如 Lcom/simibubi/create/content/logistics/stockTicker/StockKeeperRequestScreen;
     * @return 标准Java全类名 | 原字符串(非法值时) | ""(空值时)
     */
    public static String asmDescToClassName(String asmDesc) {
        if (asmDesc == null || asmDesc.isBlank()) {
            return "";
        }
        String className = asmDesc;
        // 去掉开头的 L
        if (className.startsWith("L")) {
            className = className.substring(1);
        }
        // 去掉结尾的 ;
        if (className.endsWith(";")) {
            className = className.substring(0, className.length() - 1);
        }
        // 斜杠替换为点
        return className.replace('/', '.');
    }
}