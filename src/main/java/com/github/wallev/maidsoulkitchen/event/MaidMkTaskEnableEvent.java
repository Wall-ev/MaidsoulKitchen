package com.github.wallev.maidsoulkitchen.event;

import com.github.tartaricacid.touhoulittlemaid.entity.passive.EntityMaid;
import com.github.wallev.maidsoulkitchen.api.IMaidsoulKitchenTask;
import com.mojang.datafixers.util.Pair;
import net.minecraftforge.eventbus.api.Event;

import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

public class MaidMkTaskEnableEvent extends Event {
    private final EntityMaid maid;
    private final IMaidsoulKitchenTask maidsoulKitchenTask;
    private boolean isEnable = true;
    private List<Pair<String, Predicate<EntityMaid>>> enableConditionDesc = Collections.emptyList();

    public MaidMkTaskEnableEvent(EntityMaid maid, IMaidsoulKitchenTask maidsoulKitchenTask) {
        this.maid = maid;
        this.maidsoulKitchenTask = maidsoulKitchenTask;
    }

    public EntityMaid getMaid() {
        return maid;
    }

    public IMaidsoulKitchenTask getMaidsoulKitchenTask() {
        return maidsoulKitchenTask;
    }

    public boolean isEnable() {
        return isEnable;
    }

    public void setEnable(boolean enable) {
        isEnable = enable;
    }

    public void setEnableConditionDesc(List<Pair<String, Predicate<EntityMaid>>> enableConditionDesc) {
        this.enableConditionDesc = enableConditionDesc;
    }

    public List<Pair<String, Predicate<EntityMaid>>> getEnableConditionDesc() {
        return enableConditionDesc;
    }
}
