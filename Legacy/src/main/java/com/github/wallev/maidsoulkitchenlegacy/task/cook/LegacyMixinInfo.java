package com.github.wallev.maidsoulkitchenlegacy.task.cook;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LegacyMixinInfo {
    private final Map<String, Mods> targetBindInfo = new HashMap<>();
    private final Map<ResourceLocation, List<String>> taskMixinList = new HashMap<>();
    private static final LegacyMixinInfo INSTANCE = new LegacyMixinInfo();
    protected LegacyMixinInfo() {
    }

    public static void putMixin(String target, Mods bindMod, ResourceLocation taskId) {
        INSTANCE.targetBindInfo.put(target, bindMod);
        INSTANCE.taskMixinList.computeIfAbsent(taskId, (task) -> {
            return new ArrayList<>();
        }).add(target);
    }

    public static boolean canMixin(String target) {
        return INSTANCE.targetBindInfo.get(target).versionLoad();
    }
}
