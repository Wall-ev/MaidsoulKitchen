package com.github.wallev.maidsoulkitchen.task;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.task.farm.*;
import com.github.wallev.maidsoulkitchen.task.farm.advancefarm2.TaskAdvanceFarm2;
import com.github.wallev.maidsoulkitchen.task.other.TaskFeedAnimalT;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public enum MaidsoulKitchenTask {
    COMPAT_MELON_FARM(TaskInfo.COMPAT_MELON_FARM, TaskCompatMelonFarm::new),
    BERRY_FARM(TaskInfo.BERRY_FARM, TaskBerryFarm::new),
    FRUIT_FARM(TaskInfo.FRUIT_FARM, TaskFruitFarm::new),

    FEED_ANIMAL_T(TaskInfo.FEED_ANIMAL_T, TaskFeedAnimalT::new),

    SERENESEASONS_FARM(TaskInfo.SERENESEASONS_FARM, TaskSsFarm::new),

    ECLIPTICSSEASONS_FARM(TaskInfo.ECLIPTICSSEASONS_FARM, TaskEsFarm::new),

    COOK(TaskInfo.COOK, TaskCook::new);

    ;

    public final TaskInfo taskInfo;
    private final Supplier<IMaidsoulKitchenTask> bindTask;

    public final ResourceLocation uid;

    MaidsoulKitchenTask(TaskInfo taskInfo, Supplier<IMaidsoulKitchenTask> bindTask) {
        this.taskInfo = taskInfo;
        this.bindTask = bindTask;

        this.uid = taskInfo.getUid();
    }

    public ResourceLocation getUid() {
        return taskInfo.getUid();
    }

    public static void init() {
    }

    private void initIfCanLoad(TaskManager taskManager) {
    }

    public static void register(TaskManager taskManager) {
        for (MaidsoulKitchenTask value : values()) {
            if (value.taskInfo.canLoad()) {
                value.initIfCanLoad(taskManager);
                taskManager.add(value.bindTask.get());
            }
        }
        taskManager.add(new TaskAdvanceFarm2());
    }
}
