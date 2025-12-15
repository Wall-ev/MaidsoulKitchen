package com.github.wallev.maidsoulkitchen.task.handler;

import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.legacy.mixin.LegacyTaskMixin;
import com.github.wallev.maidsoulkitchen.legacy.task.AutoLegacyCookTaskRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CookTaskRegisterHandler {


    public static void autoRegisterLegacyCompat() {
        Map<TaskInfo, List<String>> legacyMixinData = new HashMap<>();
        AnnotationHelper.read(LegacyTaskMixin.class, data -> {
            String task = AnnotationHelper.getEnumHolderValue(data, "task");
            TaskInfo taskInfo = TaskInfo.by(task);
            String mixinClazz = data.memberName();
            legacyMixinData.computeIfAbsent(taskInfo, k -> new ArrayList<>()).add(mixinClazz);
        });

        AnnotationHelper.read(AutoLegacyCookTaskRegister.class, data -> {
            String taskVal = AnnotationHelper.getEnumHolderValue(data, "value");
            TaskInfo taskInfo = TaskInfo.by(taskVal);
            List<String> mixinClazzList = legacyMixinData.getOrDefault(taskInfo, List.of());

            String clazzName = data.memberName();
            try {
                Class<?> asmClazz = Class.forName(clazzName);
                Constructor<?> constructor = asmClazz.getConstructor();
                ICookTask<?, ?> task = (ICookTask<?, ?>) constructor.newInstance();

                TaskRegister.addLegacyCookTask(
                        taskInfo::getUid,
                        taskInfo::getBindMod,
                        taskInfo::canLoad,
                        () -> task,
                        mixinClazzList
                );

            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException e) {
                e.printStackTrace(System.err);
            }


        });
    }

}
