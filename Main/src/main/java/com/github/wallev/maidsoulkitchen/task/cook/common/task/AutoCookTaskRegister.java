package com.github.wallev.maidsoulkitchen.task.cook.common.task;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoCookTaskRegister {
    TaskInfo value();
}
