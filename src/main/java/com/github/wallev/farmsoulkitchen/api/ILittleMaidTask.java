package com.github.wallev.farmsoulkitchen.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;

public interface ILittleMaidTask extends IMaidTask {

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

    default String getBookEntry() {
        return this.getBookEntryType().name;
    }
}
