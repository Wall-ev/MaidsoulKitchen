package com.github.wallev.maidsoulkitchen.util;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.CookDataV1;
import com.github.wallev.maidsoulkitchen.entity.data.inner.task.cook.v1.KitchenData;
import com.github.wallev.maidsoulkitchen.init.touhoulittlemaid.DataRegister;
import com.github.wallev.maidsoulkitchen.task.TaskInfo;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.resources.ResourceLocation;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@SuppressWarnings("deprecation")
public class OldDataHelper {
    private static final String TASK_DATA_TAG_NAME = "MaidTaskDataMaps";
    private static final String MAID_TASK_TAG_NAME = "MaidTask";
    private static final List<OldTaskInfo> WILL_TRANS_TASK_INFO;
    private static final List<String> WILL_TRANS_TASK_STR;
    private static final TaskDataKey<KitchenData> DATA_KEY = DataRegister.COOK;
    private static final String COOK_DATA_KEY_STRING = DATA_KEY.getKey().toString();
    private static final Map<String, ResourceLocation> WILL_TRANS_TASK_STR_MAP;

    static {
        List<TaskInfo> willTransTaskInfo = ImmutableList.of(
                TaskInfo.FURNACE,
                TaskInfo.FD_COOK_POT,
                TaskInfo.FD_CUTTING_BOARD,
                TaskInfo.CD_CUISINE_SKILLET,
                TaskInfo.MD_COOK_POT,
                TaskInfo.BNC_KEY,
                TaskInfo.BD_BASIN,
                TaskInfo.BD_GRILL,
                TaskInfo.YHC_MOKA,
                TaskInfo.YHC_TEA_KETTLE,
                TaskInfo.YHC_DRYING_RACK,
                TaskInfo.YHC_FERMENTATION_TANK,
                TaskInfo.CP_CROCK_POT,
                TaskInfo.DB_BEER,
                TaskInfo.KK_BREW_BARREL,
                TaskInfo.KK_AIR_COMPRESSOR
        );

        WILL_TRANS_TASK_INFO = willTransTaskInfo.stream()
                .map(OldTaskInfo::new)
                .toList();

        WILL_TRANS_TASK_STR = willTransTaskInfo.stream()
                .map(TaskInfo::getUid)
                .map(ResourceLocation::toString)
                .toList();

        WILL_TRANS_TASK_STR_MAP = willTransTaskInfo.stream()
                .collect(Collectors.toUnmodifiableMap(
                        TaskInfo::getOldName,
                        TaskInfo::getUid
                ));
    }

    public static void transOldKitchenData(CompoundTag entityTag) {
        // 应该不会有女仆没有 MaidTaskDataMaps Tag数据吧...
        if (!entityTag.contains(TASK_DATA_TAG_NAME)) {
            return;
        }

        CompoundTag taskDataTag = entityTag.getCompound(TASK_DATA_TAG_NAME);
        if (taskDataTag.isEmpty() || taskDataTag.contains(COOK_DATA_KEY_STRING)) {
            return;
        }

        Map<ResourceLocation, CookDataV1> taskInfoCookDataMap = collectOldKitchenData(taskDataTag);
        if (taskInfoCookDataMap.isEmpty()) {
            return;
        }

        // 旧版本数据迁移
        {
            CompoundTag taskDataTagCompound = taskDataTag.getCompound(COOK_DATA_KEY_STRING);
            KitchenData kitchenData = taskDataTagCompound.isEmpty() ? new KitchenData() : DATA_KEY.readSaveData(taskDataTagCompound);
            taskInfoCookDataMap.forEach((taskInfo, cookData) -> {
                kitchenData.setCookData(taskInfo, cookData);
                taskDataTag.remove(taskInfo.toString());
            });
            entityTag.putString(MAID_TASK_TAG_NAME, TaskInfo.COOK.getUid().toString());

            String maidTask = entityTag.getString(MAID_TASK_TAG_NAME);
            if (WILL_TRANS_TASK_STR.contains(maidTask)) {
                kitchenData.setCookName(WILL_TRANS_TASK_STR_MAP.get(maidTask));
            }

            CompoundTag kitchenDataTag = DATA_KEY.writeSaveData(kitchenData);
            taskDataTag.put(COOK_DATA_KEY_STRING, kitchenDataTag);
        }
    }

    private static Map<ResourceLocation, CookDataV1> collectOldKitchenData(CompoundTag taskDataTag) {
        Map<ResourceLocation, CookDataV1> taskInfoCookDataMap = Maps.newHashMap();
        for (OldTaskInfo taskInfo : WILL_TRANS_TASK_INFO) {
            CompoundTag compound = taskDataTag.getCompound(taskInfo.str());
            if (compound.isEmpty()) {
                continue;
            }
            CookDataV1.CODEC.parse(NbtOps.INSTANCE, compound).result().ifPresent(cookData -> {
                taskInfoCookDataMap.put(taskInfo.uid(), cookData);
            });
        }
        return taskInfoCookDataMap;
    }

    private record OldTaskInfo(ResourceLocation uid, String str) {
        public OldTaskInfo(TaskInfo taskInfo) {
            this(taskInfo.getUid(), taskInfo.getOldName());
        }
    }

}
