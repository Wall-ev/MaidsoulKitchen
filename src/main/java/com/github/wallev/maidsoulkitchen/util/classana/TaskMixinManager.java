package com.github.wallev.maidsoulkitchen.util.classana;

import net.minecraft.resources.ResourceLocation;

public class TaskMixinManager {
    private static final TaskMixinAnalyzer.ModTaskMixinMap MOD_TASK_MIXIN = TaskMixinAnalyzer.readModTaskClazz();

    public static boolean canMixin(String sourceMixinClazz) {
        return MOD_TASK_MIXIN.canMixin(sourceMixinClazz);
    }

    public static boolean isApplyMixin(ResourceLocation task) {
        return MOD_TASK_MIXIN.isApplyMixin(task);
    }

    public static void init(){
    }
}
