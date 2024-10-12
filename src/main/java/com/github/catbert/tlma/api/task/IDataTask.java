package com.github.catbert.tlma.api.task;

import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;

public interface IDataTask<D> {
    TaskDataKey<D> getCookDataKey();
}
