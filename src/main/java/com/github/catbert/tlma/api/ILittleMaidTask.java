package com.github.catbert.tlma.api;

import com.github.tartaricacid.touhoulittlemaid.api.task.IMaidTask;

public interface ILittleMaidTask extends IMaidTask {

    default TaskBookEntryType getBookEntryType() {
        return TaskBookEntryType.OTHER;
    }

}
