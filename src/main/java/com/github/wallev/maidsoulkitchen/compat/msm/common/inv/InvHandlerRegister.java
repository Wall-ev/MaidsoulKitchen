package com.github.wallev.maidsoulkitchen.compat.msm.common.inv;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface InvHandlerRegister {
    TaskInfo value() default TaskInfo.NONE;
}
