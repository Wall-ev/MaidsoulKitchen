package com.github.catbert.tlma.entity.passive;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;

import java.util.*;

public final class CookTaskData {
    private final Map<String, TaskRule> taskRules = new HashMap<>();

    public CookTaskData() {
    }

    public void addOrRemoveTaskRecipe(String taskId, String recipeId) {
        if (this.getTaskRule(taskId).recipeIds.contains(recipeId)) {
            removeTaskRecipe(taskId, recipeId);
        } else {
            addTaskRecipe(taskId, recipeId);
        }
    }

    public boolean containsRecipe(String taskId, String recipeId) {
        return this.getTaskRule(taskId).recipeIds.contains(recipeId);
    }

    public void removeTaskRecipe(String taskId, String recipeId) {
        this.getTaskRule(taskId).recipeIds.remove(recipeId);
    }

    public void addTaskRecipe(String taskId, String recipeId) {
        this.getTaskRule(taskId).recipeIds.add(recipeId);
    }

    public void setTaskMode(String taskId, Mode mode) {
        this.getTaskRule(taskId).setMode(mode);
    }

    public void addTaskRule(String taskId, TaskRule taskRule) {
        taskRules.put(taskId, taskRule);
    }

    public TaskRule getTaskRule(String taskId) {
        TaskRule taskRule = taskRules.get(taskId);
        if (taskRule != null) {
            return taskRule;
        }else {
            TaskRule taskRule1 = new TaskRule(Mode.RANDOM, new ArrayList<>());
            taskRules.put(taskId, taskRule1);
            return taskRule1;
        }
    }

    public Map<String, TaskRule> getTaskRules() {
        return taskRules;
    }

    public void save(CompoundTag compound) {
        CompoundTag data = new CompoundTag();
        taskRules.forEach((taskId, taskRule) -> {
            CompoundTag compoundTag = new CompoundTag();
            compoundTag.putString("Mode", taskRule.getMode().getUid());
            ListTag listTag = new ListTag();
            for (String recipeId : taskRule.recipeIds) {
                listTag.add(StringTag.valueOf(recipeId));
            }
            compoundTag.put("Recs", listTag);
            data.put(taskId, compoundTag);
        });
        compound.put("CookTask", data);
    }

    public void load(CompoundTag compound) {
        if (compound.contains("CookTask", Tag.TAG_COMPOUND)) {
            taskRules.clear();

            CompoundTag data = compound.getCompound("CookTask");
            Set<String> allKeys = data.getAllKeys();

            for (String key : allKeys) {
                CompoundTag tag = data.getCompound(key);
                String mode = tag.getString("Mode");
                ListTag recsTag = tag.getList("Recs", Tag.TAG_STRING);

                TaskRule taskRule = new TaskRule(Mode.valueOf(mode.toUpperCase()), recsTag.stream().map(Tag::getAsString).toList());
                taskRules.put(key, taskRule);
            }
        }
    }

    public void clear() {
        taskRules.clear();
    }

    public enum Mode {
        RANDOM("random"),
        SELECT("select");

        private final String uid;

        Mode(String select) {
            this.uid = select;
        }

        public String getUid() {
            return uid;
        }
    }

    public static class TaskRule {
        private Mode mode;
        private List<String> recipeIds = new ArrayList<>();

        public TaskRule(Mode mode, List<String> recipeIds) {
            this.mode = mode;
            this.recipeIds = recipeIds;
        }

        public Mode getMode() {
            return mode;
        }

        public void setMode(Mode mode) {
            this.mode = mode;
        }

        public List<String> getRecipeIds() {
            return recipeIds;
        }

        public void setRecipeIds(List<String> recipeIds) {
            this.recipeIds = recipeIds;
        }
    }
}
