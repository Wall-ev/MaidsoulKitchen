package com.github.wallev.maidsoulkitchen.debug.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface StackTraceRecord {
    IAspectAnnotation base() default @IAspectAnnotation;
}
