package com.github.catbert.tlma.init;

import com.github.catbert.tlma.entity.data.inner.CookData;
import com.github.catbert.tlma.task.cook.v1.fd.TaskFDCookPot;
import com.github.tartaricacid.touhoulittlemaid.api.entity.data.TaskDataKey;
import com.github.tartaricacid.touhoulittlemaid.entity.data.TaskDataRegister;

public final class InitTaskData {
    private InitTaskData() {}
    public static TaskDataKey<CookData> FD_COOK_DATA;

    public static void registerAll(TaskDataRegister register) {
        FD_COOK_DATA = register.register(TaskFDCookPot.NAME, CookData.CODEC);
    }
}