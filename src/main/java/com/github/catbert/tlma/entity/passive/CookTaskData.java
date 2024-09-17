package com.github.catbert.tlma.entity.passive;

import com.github.catbert.tlma.config.subconfig.TaskConfig;
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
        List<String> recipeIds = this.getTaskRule(taskId).recipeIds;
        if (recipeIds.contains(recipeId)) {
            removeTaskRecipe(taskId, recipeId);
        } else if (recipeIds.size() < TaskConfig.COOK_SELECTED_RECIPES.get()) {
            addTaskRecipe(taskId, recipeId);
        }
    }

    public boolean containsRecipe(String taskId, String recipeId) {
        return this.getTaskRule(taskId).recipeIds.contains(recipeId);
    }

    public void removeTaskRecipe(String taskId, String recipeId) {
        this.getTaskRule(taskId).recipeIds.remove(recipeId);
        this.getTaskRule(taskId).setNeedUpdate(true);
    }

    public void addTaskRecipe(String taskId, String recipeId) {
        this.getTaskRule(taskId).recipeIds.add(recipeId);
        this.getTaskRule(taskId).setNeedUpdate(true);
    }

    public void toggleMode(String taskId) {
        TaskRule taskRule = this.getTaskRule(taskId);
        if (taskRule.getMode() == Mode.RANDOM) {
            taskRule.setMode(Mode.SELECT);
        } else {
            taskRule.setMode(Mode.RANDOM);
        }
    }

    public void setTaskMode(String taskId, Mode mode) {
        this.getTaskRule(taskId).setMode(mode);
        this.getTaskRule(taskId).setNeedUpdate(true);
    }

    public void addTaskRule(String taskId, TaskRule taskRule) {
        taskRules.put(taskId, taskRule);
    }

    public void setTaskRule(String taskId, TaskRule taskRule) {
        if (taskRules.get(taskId) == null) {
            addTaskRule(taskId, taskRule);
        }else {
            taskRules.replace(taskId, taskRule);
        }
    }

    public TaskRule getTaskRule(String taskId) {
        TaskRule taskRule = taskRules.get(taskId);
        if (taskRule != null) {
            return taskRule;
        } else {
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
                List<String> list = recsTag.stream().map(Tag::getAsString).toList();
                List<String> arrayList = new ArrayList<>(list);

                TaskRule taskRule = new TaskRule(Mode.valueOf(mode.toUpperCase()), arrayList);
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
        private boolean needUpdate = true;

        public TaskRule(Mode mode, List<String> recipeIds) {
            this.mode = mode;
            this.recipeIds = recipeIds;
        }

        public void toggleMode() {
            if (mode == Mode.RANDOM) {
                setMode(Mode.SELECT);
            } else {
                setMode(Mode.RANDOM);
            }
        }

        public void toggleRecipe(String recipeId) {
            if (recipeIds.size() < TaskConfig.COOK_SELECTED_RECIPES.get() && !recipeIds.contains(recipeId)) {
                recipeIds.add(recipeId);
            } else {
                recipeIds.remove(recipeId);
            }
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

        public boolean isNeedUpdate() {
            return needUpdate;
        }

        public void setNeedUpdate(boolean needUpdate) {
            this.needUpdate = needUpdate;
        }
    }
}
