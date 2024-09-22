package com.github.catbert.tlma.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.List;

public final class MaidTaskDataUtil {
    public static final String COOK_TASK_TAG = "Cook";
    public static final String COOK_TASK_MODE_TAG = "Mode";
    public static final String COOK_TASK_RECS_TAG = "Recs";
    public static final String FARM_TASK_TAG = "Farm";
    public static final String FARM_TASK_RULES_TAG = "Rules";
    public static final String FRUIT_FARM_SEARCH_YOFFSET_TAG = "SearchYOffset";
    public static final int FRUIT_FARM_SEARCH_YOFFSET_DEFAULT = 3;

    public static CompoundTag getTaskData(EntityMaid maid) {
        return maid.getTaskData();
    }

    public static void setTaskData(EntityMaid maid, CompoundTag compoundTag) {
        maid.setTaskData(compoundTag);
    }

    /**
     * -------------------------FarmTaskData-----------------------------------------
     **/
    public static CompoundTag getFarmTaskInfos(EntityMaid maid) {
        CompoundTag compound = getTaskData(maid).getCompound(FARM_TASK_TAG);
        if (compound.isEmpty()) {
            getTaskData(maid).put(FARM_TASK_TAG, compound);
        }
        return compound;
    }

    public static CompoundTag getFarmTaskInfo(EntityMaid maid, String taskUid) {
        CompoundTag compound = getFarmTaskInfos(maid).getCompound(taskUid);
        if (compound.isEmpty()) {
            getFarmTaskInfos(maid).put(taskUid, compound);
        }
        return compound;
    }

    public static List<String> getFarmTaskRules(CompoundTag compound) {
        ListTag list = compound.getList(FARM_TASK_RULES_TAG, Tag.TAG_STRING);
        return list.stream().map(Tag::getAsString).toList();
    }

    public static ListTag getFarmTaskRules(EntityMaid maid, String taskUid) {
        ListTag list = getFarmTaskInfo(maid, taskUid).getList(FARM_TASK_RULES_TAG, Tag.TAG_STRING);
        if (list.isEmpty()) {
            getFarmTaskInfo(maid, taskUid).put(FARM_TASK_RULES_TAG, list);
        }
        return list;
    }

    public static List<String> getFarmTaskRulesList(EntityMaid maid, String taskUid) {
        return getFarmTaskRules(maid, taskUid).stream().map(Tag::getAsString).toList();
    }

    public static void addFarmTaskRule(EntityMaid maid, String taskUid, String ruleUid) {
        ListTag cookTaskMode4 = getFarmTaskRules(maid, taskUid);
        cookTaskMode4.add(StringTag.valueOf(ruleUid));
    }

    public static void removeFarmTaskRule(EntityMaid maid, String taskUid, String ruleUid) {
        ListTag cookTaskMode4 = getFarmTaskRules(maid, taskUid);
        cookTaskMode4.removeIf(tag -> tag.getAsString().equals(ruleUid));
    }

    // fruitFarm
    public static int getFruitFarmSearchYOffset(EntityMaid maid, String taskUid) {
        CompoundTag farmTaskInfo = getFarmTaskInfo(maid, taskUid);
        if (!farmTaskInfo.contains(FRUIT_FARM_SEARCH_YOFFSET_TAG, Tag.TAG_INT)) {
            farmTaskInfo.putInt(FRUIT_FARM_SEARCH_YOFFSET_TAG, FRUIT_FARM_SEARCH_YOFFSET_DEFAULT);
        }
        return farmTaskInfo.getInt(FRUIT_FARM_SEARCH_YOFFSET_TAG);
    }

    public static void setFruitFarmSearchYOffset(EntityMaid maid, String taskUid, int offset) {
        getFarmTaskInfo(maid, taskUid).putInt(FRUIT_FARM_SEARCH_YOFFSET_TAG, offset);
    }

    public static void decreaseFruitFarmSearchYOffset(EntityMaid maid, String taskUid) {
        int fruitFarmSearchYOffset = getFruitFarmSearchYOffset(maid, taskUid);
        setFruitFarmSearchYOffset(maid, taskUid, ++fruitFarmSearchYOffset);
    }

    public static void increaseFruitFarmSearchYOffset(EntityMaid maid, String taskUid) {
        int fruitFarmSearchYOffset = getFruitFarmSearchYOffset(maid, taskUid);
        setFruitFarmSearchYOffset(maid, taskUid, --fruitFarmSearchYOffset);
    }

    public static void inDeFruitFarmSearchYOffset(EntityMaid maid, String taskUid, boolean decrease) {
        if (decrease) {
            decreaseFruitFarmSearchYOffset(maid, taskUid);
        }else {
            increaseFruitFarmSearchYOffset(maid, taskUid);
        }
    }
    /* ------------------------FarmTaskDataEnd---------------------------------------- */

    /**
     * -------------------------CookTaskData-----------------------------------------
     **/
    public static CompoundTag getCookTaskInfos(EntityMaid maid) {
        CompoundTag compound = getTaskData(maid).getCompound(COOK_TASK_TAG);
        if (compound.isEmpty()) {
            getTaskData(maid).put(COOK_TASK_TAG, compound);
        }
        return compound;
    }

    public static CompoundTag getCookTaskInfo(EntityMaid maid, String taskUid) {
        CompoundTag compound = getCookTaskInfos(maid).getCompound(taskUid);
        if (compound.isEmpty()) {
            getCookTaskInfos(maid).put(taskUid, compound);
        }
        return compound;
    }

    public static String getCookTaskMode(CompoundTag compound) {
        return compound.getString(COOK_TASK_MODE_TAG);
    }

    public static List<String> getCookTaskRecs(CompoundTag compound) {
        ListTag list = compound.getList(COOK_TASK_RECS_TAG, Tag.TAG_STRING);
        return list.stream().map(Tag::getAsString).toList();
    }

    public static String getCookTaskMode(EntityMaid maid, String taskUid) {
        return getCookTaskInfo(maid, taskUid).getString(COOK_TASK_MODE_TAG);
    }

    public static void setCookTaskMode(EntityMaid maid, String taskUid, String mode) {
        getCookTaskInfo(maid, taskUid).putString(COOK_TASK_MODE_TAG, mode);
    }

    public static ListTag getCookTaskRecs(EntityMaid maid, String taskUid) {
        ListTag list = getCookTaskInfo(maid, taskUid).getList(COOK_TASK_RECS_TAG, Tag.TAG_STRING);
        if (list.isEmpty()) {
            getCookTaskInfo(maid, taskUid).put(COOK_TASK_RECS_TAG, list);
        }
        return list;
    }

    public static void addCookTaskRec(EntityMaid maid, String taskUid, String rec) {
        ListTag cookTaskMode4 = getCookTaskRecs(maid, taskUid);
        cookTaskMode4.add(StringTag.valueOf(rec));
    }

    public static void removeCookTaskRec(EntityMaid maid, String taskUid, String rec) {
        ListTag cookTaskMode4 = getCookTaskRecs(maid, taskUid);
        cookTaskMode4.removeIf(tag -> tag.getAsString().equals(rec));
    }

//    public static void addOrRemoveCookTaskRec(EntityMaid maid, String taskUid, String rec) {
//        ListTag cookTaskMode4 = getCookTaskRecs(maid, taskUid);
//        StringTag recTag = StringTag.valueOf(rec);
//        if (cookTaskMode4.contains(recTag)) {
//            cookTaskMode4.remove(recTag);
//        } else {
//            cookTaskMode4.add(recTag);
//        }
//    }
    /* ------------------------CookTaskDataEnd---------------------------------------- */

}
