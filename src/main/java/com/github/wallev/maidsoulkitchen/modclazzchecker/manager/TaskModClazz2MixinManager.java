package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.TaskMixinAnalyzer;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.clazz.ClassAnalyzerManager;
import com.github.wallev.maidsoulkitchen.modclazzchecker.core.manager.BaseClazzCheckManager;

import java.io.IOException;
import java.util.Map;

public class TaskModClazz2MixinManager {
    protected static MKClazzCheck2MixinManager<?> checkManager = new MKClazzCheck2MixinManager<>();

    protected static Map<String, Boolean> modTaskClazzResult;
    protected static TaskMixinAnalyzer.ModTaskMixinMap mixinData;

    private static void startReadMixinClazz() {
        initMixinData();
    }

    protected static void initMixinData() {
        if (mixinData == null) {
            mixinData = ClassAnalyzerManager.readModTaskMixinClazzFromFile(checkManager);
        }
    }

    public static boolean canMixin(String sourceMixinClazz) {
        return mixinData.canMixin(sourceMixinClazz);
    }

    public static void init() throws IOException {
        startReadMixinClazz();
    }

    public static BaseClazzCheckManager<?, ?> getCheckManager() {
        return checkManager;
    }
}
