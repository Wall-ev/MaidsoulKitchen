package com.github.wallev.maidsoulkitchen.util.debug.annotation;

import com.github.wallev.maidsoulkitchen.util.debug.AspectConfig;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.ANNOTATION_TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface IAspectAnnotation {
    String name() default "";

    boolean run() default AspectConfig.RUN;

    // 支持基本类型的默认值配置
    String stringVal() default "";

    int intVal() default 0;

    boolean booleanVal() default false;

    float floatVal() default 0;

    double doubleVal() default 0;

    long longVal() default 0;

    // 其他基本类型...

    // 对于复杂对象，使用特殊标记或类引用
    Class<?> objectType() default Void.class;
}
