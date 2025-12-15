package com.github.wallev.maidsoulkitchen.compat.msm.common.autocraftguide.base;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface AutoCraftGuideGeneratorRegister {
    TaskInfo value() default TaskInfo.NONE;
}
