package com.github.wallev.maidsoulkitchen.util.classana;

import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.ClassAnalyzerManager;
import com.github.wallev.maidsoulkitchen.util.classana.clazz.TaskClazzInfo;
import net.minecraft.resources.ResourceLocation;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

public class TaskModClazzManager {
    private static TaskClazzInfo taskClazzInfo = readModTaskClazzFile();
    private static Map<ResourceLocation, Boolean> modTaskClazzResult;

    public static void writeModTaskClazzFile(Path rootOutputFolder) throws Exception {
        ClassAnalyzerManager.writeModTaskClazz(rootOutputFolder);
    }

    private static TaskClazzInfo readModTaskClazzFile() {
        return ClassAnalyzerManager.readModTaskClazzFromFile();
    }

    public static void startReadTaskClazz() throws IOException {
        modTaskClazzResult = ClassAnalyzerManager.readModTaskClazz(taskClazzInfo);
    }

    public static boolean canMixin(String sourceMixinClazz) {
        return taskClazzInfo.taskMixinMap().canMixin(sourceMixinClazz);
    }

    public static boolean isApplyMixin(ResourceLocation task) {
        return taskClazzInfo.taskMixinMap().isApplyMixin(task);
    }

    public static boolean clazzLoad(TaskInfo taskInfo) {
        return clazzLoad(taskInfo.getUid());
    }

    public static boolean clazzLoad(ResourceLocation taskUid) {
        return modTaskClazzResult.getOrDefault(taskUid, true);
    }

    public static void init() {
    }

    public static void clear() {
        taskClazzInfo = null;
    }
}
