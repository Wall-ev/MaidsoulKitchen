package com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.condition;

import com.github.wallev.maidsoulkitchen.modclazzchecker.manager.taskinfo.TaskInfo0;

public record TaskInfoCondition(TaskInfo0 taskInfo) implements LoadCondition<TaskInfo0> {

    @Override
    public boolean canLoad() {
        return taskInfo.canLoad();
    }

    @Override
    public TaskInfo0 condition() {
        return taskInfo;
    }
}
