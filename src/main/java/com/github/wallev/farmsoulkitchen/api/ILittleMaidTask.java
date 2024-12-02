package com.github.wallev.farmsoulkitchen.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;

public interface ILittleMaidTask extends IMaidTask {

    public static IMaidTask getInstance() {
        throw new RuntimeException("Cannot instantiate this class");
    }

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

}
