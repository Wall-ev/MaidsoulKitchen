package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.task.ModGroup;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface TaskImport {
    TaskInfo value() default TaskInfo.NONE;
    ModGroup extraGroup() default ModGroup.NONE;
}