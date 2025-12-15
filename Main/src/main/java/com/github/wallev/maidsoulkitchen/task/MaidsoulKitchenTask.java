package com.github.wallev.maidsoulkitchen.task;

import com.github.tartaricacid.touhoulittlemaid.entity.task.TaskManager;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.task.farm.*;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.BerryHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.fruit.FruitHandlerManager;
import com.github.wallev.maidsoulkitchen.task.other.TaskFeedAnimalT;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public enum MaidsoulKitchenTask {
    COMPAT_MELON_FARM(TaskInfo.COMPAT_MELON_FARM, TaskCompatMelonFarm::new),
    BERRY_FARM(TaskInfo.BERRY_FARM, TaskBerryFarm::new) {
        @Override
        protected void initIfCanLoad(TaskManager taskManager) {
            List<IFarmHandlerManager<?>> handlers = new ArrayList<>();
            for (BerryHandlerManager value : BerryHandlerManager.VALUES) {
                if (value.getBindMod().versionLoad() && TaskModClazzManager.clazzLoad(value.getUid().toString())) {
                    handlers.add(value);
                }
            }
            IFarmHandlerManager.registerHandler(this.taskInfo.getUid(), ImmutableList.copyOf(handlers));
        }

    },
    FRUIT_FARM(TaskInfo.FRUIT_FARM, TaskFruitFarm::new) {
        @Override
        protected void initIfCanLoad(TaskManager taskManager) {
            List<IFarmHandlerManager<?>> handlers = new ArrayList<>();
            for (FruitHandlerManager value : FruitHandlerManager.VALUES) {
                if (value.getBindMod().versionLoad() && TaskModClazzManager.clazzLoad(value.getUid().toString())) {
                    handlers.add(value);
                }
            }
            IFarmHandlerManager.registerHandler(this.taskInfo.getUid(), ImmutableList.copyOf(handlers));
        }
    },

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

    protected void initIfCanLoad(TaskManager taskManager) {
    }

    public static void register(TaskManager taskManager) {
        for (MaidsoulKitchenTask value : values()) {
            if (value.taskInfo.canLoad()) {
                value.initIfCanLoad(taskManager);
                taskManager.add(value.bindTask.get());
            }
        }
    }
}
