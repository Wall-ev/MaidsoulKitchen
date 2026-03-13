package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskErrorLang {
    String en_us() default "";
    String zh_cn() default "";
    String en_us_desc() default "";
    String zh_cn_desc() default "";
}
