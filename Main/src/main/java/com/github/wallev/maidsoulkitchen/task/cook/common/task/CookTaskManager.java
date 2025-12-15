package com.github.wallev.maidsoulkitchen.task.cook.common.task;

import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.api.task.cook.ICookTask;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.TaskRegister;
import com.github.wallev.maidsoulkitchen.legacy.mixin.LegacyTaskMixin;
import com.github.wallev.maidsoulkitchen.legacy.task.AutoLegacyCookTaskRegister;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.type.DevModule;
import com.github.wallev.maidsoulkitchen.util.AnnotationHelper;
import com.github.wallev.maidsoulkitchen.util.ModUtil;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
        this.loadNewTask();
        this.loadLegacyTask();
        this.makeImmutable();
    }

    public static Map<String, List<ResourceLocation>> getAllTaskUid() {
        return AnnotationHelper.map(AutoCookTaskRegister.class)
                .map(data -> AnnotationHelper.getEnumHolderValue(data, "value"))
                .map(TaskInfo::by)
                .filter(taskInfo -> !taskInfo.getClass().isAnnotationPresent(DevModule.class))
                .map(TaskInfo::getUid)
                // 提取uid和Mods的modId相同的部分合成一个组，也就是Map<modId, List<uid>>
                .collect(ImmutableMap.toImmutableMap(
                        uid -> {
                            return Arrays.stream(Mods.values())
                                    .filter(modId -> modId != Mods.MSK)
                                    .map(Mods::modId)
                                    .filter(Objects::nonNull)
                                    .filter(modId -> uid != null && uid.toString().contains(modId))
                                    .findFirst()
                                    .orElse(Mods.MSK.getModId());
                        },
                        Lists::newArrayList,
                        ((rec1, rec2) -> {
                            return Lists.newArrayList(rec1, rec2).stream().filter(Objects::nonNull).flatMap(Collection::stream).distinct().toList();
                        })
                ));
    }

    private void loadNewTask() {
        Map<Float, List<ICookTask<?, ?>>> taskMap = new HashMap<>();

        AnnotationHelper.read(AutoCookTaskRegister.class, data -> {
            String taskVal = AnnotationHelper.getEnumHolderValue(data, "value");
            TaskInfo cookTask = TaskInfo.by(taskVal);
            if (!cookTask.canLoad())
                return;

            String clazzName = data.memberName();
            try {
                Class<?> asmClazz = Class.forName(clazzName);
                Constructor<?> constructor = asmClazz.getConstructor();
                ICookTask<?, ?> task = (ICookTask<?, ?>) constructor.newInstance();

                task.setBindModName(ModUtil.getModName(cookTask.getBindMod().modId()));

                taskMap.computeIfAbsent(task.index(), k -> new ArrayList<>()).add(task);
            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
        });

        // 按照key自然排序
        taskMap.keySet().stream().sorted().forEach(key -> {
            taskMap.get(key).forEach(this::add);
        });
    }


    private void loadLegacyTask() {
        Map<TaskInfo, List<String>> legacyMixinData = new HashMap<>();

        AnnotationHelper.read(LegacyTaskMixin.class, data -> {
            String task = AnnotationHelper.getEnumHolderValue(data, "task");
            TaskInfo taskInfo = TaskInfo.by(task);
            String mixinClazz = data.memberName();
            legacyMixinData.computeIfAbsent(taskInfo, k -> new ArrayList<>()).add(mixinClazz);
        });

        AnnotationHelper.read(AutoLegacyCookTaskRegister.class, data -> {
            String taskVal = AnnotationHelper.getEnumHolderValue(data, "value");
            TaskInfo cookTask = TaskInfo.by(taskVal);
            List<String> mixinClazzList = legacyMixinData.getOrDefault(cookTask, List.of());

            if (!cookTask.canLoad())
                return;


            String clazzName = data.memberName();
            try {
                Class<?> asmClazz = Class.forName(clazzName);
                Constructor<?> constructor = asmClazz.getConstructor();
                ICookTask<?, ?> task = (ICookTask<?, ?>) constructor.newInstance();
                task.setBindModName(ModUtil.getModName(cookTask.getBindMod().modId()));

                TaskRegister.addLegacyCookTask(
                        cookTask::getUid,
                        cookTask::getBindMod,
                        cookTask::canLoad,
                        () -> task,
                        mixinClazzList
                );

            } catch (ClassNotFoundException | InvocationTargetException | NoSuchMethodException |
                     InstantiationException | IllegalAccessException e) {
                e.printStackTrace(System.err);
            }
        });

        for (LegacyCookTaskInfo legacy : LEGACY_TASK) {
            if (legacy.canLoad()) {
                ICookTask<?, ?> task = legacy.bindTask.get();
                task.setBindModName(ModUtil.getModName(legacy.bindMod.get().modId()));
                add(task);
            }
        }
    }

    public static void init() {
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

    public static ICookTask<?, ?> getTask(ICookTask.Index index) {
        return INSTANCE.TASK_INDEX.get(index.ordinal());
    }

    public static ICookTask<?, ?> getTask(ResourceLocation resourceLocation) {
        return INSTANCE.TASK_MAP.get(resourceLocation);
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
