package com.github.wallev.maidsoulkitchen.task;

import com.github.wallev.maidsoulkitchen.MaidsoulKitchen;
import com.github.wallev.maidsoulkitchen.api.task.IMaidsoulKitchenTask;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskInfo;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.TaskModClazzManager;
import com.github.wallev.maidsoulkitchen.task.cook.common.task.TaskCook;
import com.github.wallev.maidsoulkitchen.task.farm.*;
import com.github.wallev.maidsoulkitchen.task.farm.handler.IFarmHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.berry.BerryHandlerManager;
import com.github.wallev.maidsoulkitchen.task.farm.handler.fruit.FruitHandlerManager;
import com.github.wallev.maidsoulkitchen.task.other.TaskFeedAnimalT;
import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.Mods;
import com.github.wallev.maidsoulkitchen.vhelper.client.resources.VResourceLocation;
import com.google.common.collect.ImmutableList;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.ForgeConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public enum MaidsoulKitchenTask {
    COMPAT_MELON_FARM(TaskInfo.COMPAT_MELON_FARM, TaskCompatMelonFarm::new),
    BERRY_FARM(TaskInfo.BERRY_FARM, TaskBerryFarm::new) {
        @Override
        protected void putTask(TaskInfo taskInfo, Supplier<IMaidsoulKitchenTask> bindTask) {
            IMaidsoulKitchenTask.putTask(this.uid, () -> {
                boolean taskCanLoad = taskInfo.canLoad();
                if (taskCanLoad) {
                    List<IFarmHandlerManager<?>> handlers = new ArrayList<>();
                    for (BerryHandlerManager value : BerryHandlerManager.VALUES) {
                        if (value.getBindMod().versionLoad() && TaskModClazzManager.clazzLoad(value.getUid().toString())) {
                            handlers.add(value);
                        }
                    }
                    IFarmHandlerManager.registerHandler(this.uid, ImmutableList.copyOf(handlers));
                }
                return taskCanLoad;
            }, bindTask);
        }
    },
    FRUIT_FARM(TaskInfo.FRUIT_FARM, TaskFruitFarm::new) {
        @Override
        protected void putTask(TaskInfo taskInfo, Supplier<IMaidsoulKitchenTask> bindTask) {
            IMaidsoulKitchenTask.putTask(this.uid, () -> {
                boolean taskCanLoad = taskInfo.canLoad();
                if (taskCanLoad) {
                    List<IFarmHandlerManager<?>> handlers = new ArrayList<>();
                    for (FruitHandlerManager value : FruitHandlerManager.VALUES) {
                        if (value.getBindMod().versionLoad() && TaskModClazzManager.clazzLoad(value.getUid().toString())) {
                            handlers.add(value);
                        }
                    }
                    IFarmHandlerManager.registerHandler(this.uid, ImmutableList.copyOf(handlers));
                }
                return taskCanLoad;
            }, bindTask);
        }
    },

    FEED_ANIMAL_T(TaskInfo.FEED_ANIMAL_T, TaskFeedAnimalT::new),

    SERENESEASONS_FARM(TaskInfo.SERENESEASONS_FARM, TaskSsFarm::new),

    ECLIPTICSSEASONS_FARM(TaskInfo.ECLIPTICSSEASONS_FARM, TaskEsFarm::new),

    COOK(TaskInfo.COOK, TaskCook::new);

    ;
    public final ResourceLocation uid;
    public final String modId;

    /**
     * @param uid        任务ID标识符
     * @param bindMod    对应的模组信息
     * @param bindConfig 对应的配置
     * @param bindTask   对应的任务
     */
    MaidsoulKitchenTask(String uid, Mods bindMod, ForgeConfigSpec.BooleanValue bindConfig, Supplier<IMaidsoulKitchenTask> bindTask) {
        this.uid = VResourceLocation.create(MaidsoulKitchen.MOD_ID, uid);
        this.modId = bindMod.modId();
        this.putTask(uid, bindMod, bindConfig, bindTask);
    }

    MaidsoulKitchenTask(TaskInfo taskInfo, Supplier<IMaidsoulKitchenTask> bindTask) {
        this.uid = taskInfo.getUid();
        this.modId = taskInfo.getBindMod().modId();
        this.putTask(taskInfo, bindTask);
    }

    public static void init() {
    }

    protected void putTask(String uid, Mods bindMod, ForgeConfigSpec.BooleanValue bindConfig, Supplier<IMaidsoulKitchenTask> bindTask) {
        IMaidsoulKitchenTask.putTask(this.uid, () -> {
            return bindMod.versionLoad() && bindConfig.get() && TaskModClazzManager.clazzLoad(this.uid.toString());
        }, bindTask);
    }

    protected void putTask(TaskInfo taskInfo, Supplier<IMaidsoulKitchenTask> bindTask) {
        IMaidsoulKitchenTask.putTask(this.uid, () -> {
            return taskInfo.canLoad();
        }, bindTask);
    }
}
