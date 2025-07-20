package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.TaskClazzInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.ReportErrorEvent;

import java.io.IOException;
import java.nio.file.Path;

public class TaskModClazzManager extends TaskModClazz2MixinManager {

    public static void writeModTaskClazzFile(Path rootOutputFolder) throws Exception {
        initCheckManager();
        checkManager.writeModTaskClazz(rootOutputFolder);
    }

    private static void startReadTaskClazz() throws IOException {
        initCheckManager();
        initMixinData();

        TaskClazzInfo taskClazzInfo = checkManager.readModTaskClazzFromFile();
        taskClazzInfo.setTaskMixinMap(mixinData);
        modTaskClazzResult = checkManager.readModTaskClazz(taskClazzInfo);
    }

    public static boolean clazzLoad(String taskUid) {
        return modTaskClazzResult == null || modTaskClazzResult.getOrDefault(taskUid, true);
    }

    public static void init() throws IOException {
        startReadTaskClazz();
        if (checkManager != null) {
            ReportErrorEvent.init(checkManager);
        }
    }

    private static void initCheckManager() {
        checkManager = new MKClazzCheckManager();
    }
}
