package com.github.wallev.maidsoulkitchen.task.cook.common.task;

import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.task.CookTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.util.ModUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

import java.util.*;
import java.util.function.Supplier;

public class CookTaskManager {
    private static final List<LegacyCookTaskInfo> LEGACY_TASK = new ArrayList<>();
    private static CookTaskManager INSTANCE;

    private final ICookTask<?, ?> IDLE_TASK;
    private Map<ResourceLocation, ICookTask<?, ?>> TASK_MAP = new HashMap<>();
    private List<ICookTask<?, ?>> TASK_INDEX = new ArrayList<>();

    private CookTaskManager() {
        IDLE_TASK = new TaskCookIdle();
        for (CookTask cookTask : CookTask.values()) {
            if (cookTask.canLoad.get()) {
                ICookTask<?, ?> task = cookTask.bindTask.get();
                task.setBindModName(ModUtil.getModName(cookTask.modId));
                add(task);
            }
        }
        for (LegacyCookTaskInfo legacy : LEGACY_TASK) {
            if (legacy.canLoad()) {
                ICookTask<?, ?> task = legacy.bindTask.get();
                task.setBindModName(ModUtil.getModName(legacy.bindMod.get().modId()));
                add(task);
            }
        }
        this.makeImmutable();
    }

    public static void init() {
        CookTask.init();
        INSTANCE = new CookTaskManager();
    }

    /**
     * 获取 Task
     */
    public static Optional<ICookTask<?, ?>> findTask(ResourceLocation uid) {
        return Optional.ofNullable(INSTANCE.TASK_MAP.get(uid));
    }

    /**
     * 默认 Task
     */
    public static ICookTask<?, ?> getIdleTask() {
        return INSTANCE.IDLE_TASK;
    }

    public static Map<ResourceLocation, ICookTask<?, ?>> getTaskMap() {
        return INSTANCE.TASK_MAP;
    }

    public static List<ICookTask<?, ?>> getTaskIndex() {
        return INSTANCE.TASK_INDEX;
    }

    /**
     * 注册 Task
     */
    public void add(ICookTask<?, ?> task) {
        TASK_MAP.put(task.getUid(), task);

        TASK_INDEX.removeIf(ins -> ins.getUid().equals(task.getUid()));
        TASK_INDEX.add(task);
    }

    private void makeImmutable() {
        TASK_MAP = ImmutableMap.copyOf(TASK_MAP);
        TASK_INDEX = ImmutableList.copyOf(TASK_INDEX);
    }

    public static void addLegacyTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<ICookTask<?, ?>> task, String... mixinClz) {
        addLegacyTask(uid, bindMod, bindConfig, task, Lists.newArrayList(mixinClz));
    }

    public static void addLegacyTask(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<ICookTask<?, ?>> task, List<String> mixinClz) {
        LEGACY_TASK.add(new LegacyCookTaskInfo(uid, bindMod, bindConfig, task, mixinClz));
    }

    private record LegacyCookTaskInfo(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod,
                                      Supplier<Boolean> bindConfig,
                                      Supplier<ICookTask<?, ?>> bindTask, List<String> mixinClz) {
        public LegacyCookTaskInfo(Supplier<ResourceLocation> uid, Supplier<Mods> bindMod, Supplier<Boolean> bindConfig, Supplier<ICookTask<?, ?>> bindTask, String... mixinClz) {
            this(uid, bindMod, bindConfig, bindTask, Lists.newArrayList(mixinClz));
        }

        public boolean canLoad() {
            return bindModLoad() && bindConfigLoad() && IMaidsoulKitchenTask.TaskMixinMap.isApplyMixin(getUid());
        }

        private ResourceLocation getUid() {
            return uid.get();
        }

        private boolean bindConfigLoad() {
            return bindConfig.get();
        }

        private boolean bindModLoad() {
            return bindMod.get().versionLoad();
        }
    }
}
