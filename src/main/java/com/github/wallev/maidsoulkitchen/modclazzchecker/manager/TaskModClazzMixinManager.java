package com.github.wallev.maidsoulkitchen.modclazzchecker.manager;

import com.github.wallev.maidsoulkitchen.modclazzchecker.core.classana.ModTaskMixinMap;

import java.io.IOException;

public class TaskModClazzMixinManager {
    protected static ModTaskMixinMap mixinData;

    private static void startReadMixinClazz() {
        initMixinData();
    }

    protected static void initMixinData() {
        if (mixinData == null) {
            mixinData = TaskMixinManager.readModTaskMixinClazzFromFile();
        }
    }

    public static boolean canMixin(String sourceMixinClazz) {
        return mixinData.canMixin(sourceMixinClazz);
    }

    public static void init() throws IOException {
        startReadMixinClazz();
    }
}
