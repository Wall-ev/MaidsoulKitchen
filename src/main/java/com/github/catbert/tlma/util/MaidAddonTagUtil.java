package com.github.catbert.tlma.util;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;

import java.util.List;

public final class MaidAddonTagUtil {
    public static final EntityDataAccessor<CompoundTag> DATA_COOK_INFO = SynchedEntityData.defineId(EntityMaid.class, EntityDataSerializers.COMPOUND_TAG);
    public static final String COOK_TASK_INFO_TAG = "CookTaskInfo";
    public static final String COOK_TASK_MODE_TAG = "Mode";
    public static final String COOK_TASK_RECS_TAG = "Recs";

    public static CompoundTag getEdCookInfo(EntityMaid maid) {
        return maid.getEntityData().get(DATA_COOK_INFO);
    }

    public static void setEdCookInfo(EntityMaid maid, CompoundTag compoundTag) {
        maid.getEntityData().set(DATA_COOK_INFO, compoundTag);
    }

    public static CompoundTag getCookTaskInfos(EntityMaid maid) {
        CompoundTag compound = getEdCookInfo(maid).getCompound(COOK_TASK_INFO_TAG);
        if (compound.isEmpty()) {
            getEdCookInfo(maid).put(COOK_TASK_INFO_TAG, compound);
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

}
