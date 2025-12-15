package com.github.wallev.maidsoulkitchen.legacy.annotation;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface LegacyAutoCraftGuideGeneratorRegister {
    TaskInfo value() default TaskInfo.NONE;
}
