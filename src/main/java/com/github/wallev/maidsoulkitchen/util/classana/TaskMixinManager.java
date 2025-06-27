package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.ClassAnalyzerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClazzInfo;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.util.Map;

public class TaskMixinManager {
    private static TaskClazzInfo TASK_CLAZZ_INFO = ClassAnalyzerManager.readModTaskClazzFromFile();
    private static Map<ResourceLocation, Boolean> modTaskClazzResult;

    public static void startReadTask() throws IOException {
        modTaskClazzResult = ClassAnalyzerManager.readModTaskClazz(TASK_CLAZZ_INFO);
    }

    public static boolean canMixin(String sourceMixinClazz) {
        return TASK_CLAZZ_INFO.taskMixinMap().canMixin(sourceMixinClazz);
    }

    public static boolean isApplyMixin(ResourceLocation task) {
        return TASK_CLAZZ_INFO.taskMixinMap().isApplyMixin(task);
    }

    public static boolean clazzLoad(TaskInfo taskInfo) {
        return clazzLoad(taskInfo.uid);
    }

    public static boolean clazzLoad(ResourceLocation taskUid) {
        return modTaskClazzResult.getOrDefault(taskUid, true);
    }

    public static void init() {
    }

    public static void clear() {
        TASK_CLAZZ_INFO = null;
    }
}
